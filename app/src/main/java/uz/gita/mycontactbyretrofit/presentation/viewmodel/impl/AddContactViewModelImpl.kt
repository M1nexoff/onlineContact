package uz.gita.mycontactbyretrofit.presentation.viewmodel.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.navigation.AppNavigator
import uz.gita.mycontactbyretrofit.presentation.viewmodel.AddContactViewModel
import uz.gita.mycontactbyretrofit.utils.MyEventBus
import uz.gita.mycontactbyretrofit.utils.NetworkStatusValidator
import javax.inject.Inject

@HiltViewModel
class AddContactViewModelImpl @Inject constructor(
    private val appNavController: AppNavigator
) : ViewModel(), AddContactViewModel {
    @Inject
    lateinit var repository: AppRepository
    @Inject
    lateinit var networkStatusValidator: NetworkStatusValidator
    override val progressLiveData = MutableStateFlow<Boolean>(false)
    override var messageLiveData: ((String)->Unit)? = null

    override fun closeScreen() {

    }

    override fun addContact(firstName: String, lastName: String, phone:String) {
        progressLiveData.value = true
        repository.addContact(
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            successBlock = {
                progressLiveData.value = false
                if (networkStatusValidator.hasNetwork) messageLiveData?.invoke("Success!")
                else messageLiveData?.invoke("Save in local")

                viewModelScope.launch{
                    appNavController.back()
                }
                MyEventBus.reloadEvent?.invoke()
            },
            errorBlock = {
                progressLiveData.value = false
                messageLiveData?.invoke(it)
            }
        )
    }

}