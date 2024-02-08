package uz.gita.mycontactbyretrofit.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.domain.AppRepositoryImpl
import uz.gita.mycontactbyretrofit.utils.MyEventBus
import uz.gita.mycontactbyretrofit.utils.NetworkStatusValidator
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var repository: AppRepository
    @Inject
    lateinit var networkStatusValidator: NetworkStatusValidator
    val closeScreenLiveData = MutableLiveData<Unit>()
    val progressLiveData = MutableLiveData<Boolean>()
    val messageLiveData = MutableLiveData<String>()
    val errorMessageLiveData = MutableLiveData<String>()

    fun closeScreen() {
        closeScreenLiveData.value = Unit
    }

    fun addContact(firstName: String, lastName: String, phone:String) {
        progressLiveData.value = true
        repository.addContact(
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            successBlock = {
                progressLiveData.value = false
                if (networkStatusValidator.hasNetwork) messageLiveData.value = "Success!"
                else messageLiveData.value = "Save in local"

                closeScreenLiveData.value= Unit
                MyEventBus.reloadEvent?.invoke()
            },
            errorBlock = {
                progressLiveData.value = false
                errorMessageLiveData.value = it
            }
        )
    }

}