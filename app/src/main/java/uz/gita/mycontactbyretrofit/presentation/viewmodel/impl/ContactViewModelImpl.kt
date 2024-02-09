package uz.gita.mycontactbyretrofit.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactadapterpattern.data.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.mycontactbyretrofit.data.model.ContactUIData
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.navigation.AppNavigator
import uz.gita.mycontactbyretrofit.presentation.ui.ContactScreenDirections
import uz.gita.mycontactbyretrofit.presentation.viewmodel.ContactViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModelImpl @Inject constructor(
    private val appRepository: AppRepository,
    private val appNavController: AppNavigator
) : ViewModel(),ContactViewModel {
    override val contactLiveData = MutableLiveData<List<ContactUIData>>()
    override val progressStateFlow = MutableStateFlow<Boolean>(false)
    override val errorLiveData = MutableLiveData<String>()
    override var logout: (()->Unit)? = null

    override fun loadAllContacts() {
        progressStateFlow.value = true
        appRepository.getAllContacts().onEach {
            when(it){
                is ResultData.Success -> {
                    progressStateFlow.value = false
                    contactLiveData.value = it.data!!
                }
                is ResultData.Failure -> {
                    progressStateFlow.value = false
                    errorLiveData.value = it.message
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun openAddContactDialog() {
        viewModelScope.launch {
            appNavController.navigateTo(ContactScreenDirections.actionContactScreenToAddContactScreen())
        }
    }

    override fun deleteContact(id: Int, firstName: String, lastName: String, phone: String) {
        progressStateFlow.value = true
        appRepository.deleteContact(
            id = id,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            successBlock = {
                loadAllContacts()
                progressStateFlow.value = false
            },
            errorBlock = {
                errorLiveData.value = it
                progressStateFlow.value = false
            }
        )
    }

    override fun logOut() {
        appRepository.token = ""
        logout?.invoke()
    }
}
