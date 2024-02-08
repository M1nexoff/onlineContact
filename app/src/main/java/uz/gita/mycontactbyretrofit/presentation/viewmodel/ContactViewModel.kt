package uz.gita.mycontactbyretrofit.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactadapterpattern.data.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mycontactbyretrofit.data.model.ContactUIData
import uz.gita.mycontactbyretrofit.domain.AppRepository
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {
    private val _contactLiveData = MutableLiveData<List<ContactUIData>>()
    val contactLiveData: LiveData<List<ContactUIData>> get() = _contactLiveData

    private val _progressLiveData = MutableLiveData<Boolean>()
    val progressLiveData: LiveData<Boolean> get() = _progressLiveData

    private val _openAddContactDialogLiveData = MutableLiveData<Unit>()
    val openAddContactDialogLiveData: LiveData<Unit> get() = _openAddContactDialogLiveData

    private val _emptyStateLiveData = MutableLiveData<Unit>()
    val emptyStateLiveData: LiveData<Unit> get() = _emptyStateLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    private val _notConnectionLiveData = MutableLiveData<Unit>()
    val notConnectionLiveData: LiveData<Unit> get() = _notConnectionLiveData

    private val _logout = MutableLiveData<Boolean>()
    val logout: LiveData<Boolean> get() = _logout

    fun loadAllContacts() {
        _progressLiveData.value = true
        appRepository.getAllContacts().onEach {
            when(it){
                is ResultData.Success -> {
                    _progressLiveData.value = false
                    if (it.data.isEmpty()) _emptyStateLiveData.value = Unit
                    _contactLiveData.value = it.data!!
                }
                is ResultData.Failure -> {
                    _progressLiveData.value = false
                    _errorLiveData.value = it.message
                }
            }
        }.launchIn(viewModelScope)
    }

    fun openAddContactDialog() {
        _openAddContactDialogLiveData.value = Unit
    }

    fun deleteContact(id: Int, firstName: String, lastName: String, phone: String) {
        _progressLiveData.value = true
        appRepository.deleteContact(
            id = id,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            successBlock = {
                loadAllContacts()
                _progressLiveData.value = false
            },
            errorBlock = {
                _errorLiveData.value = it
                _progressLiveData.value = false
            }
        )
    }

    fun logOut() {
        appRepository.token = ""
        _logout.value = true
    }
}
