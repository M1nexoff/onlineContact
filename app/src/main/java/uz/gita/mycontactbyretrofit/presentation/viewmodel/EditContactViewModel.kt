package uz.gita.mycontactbyretrofit.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.domain.AppRepositoryImpl
import uz.gita.mycontactbyretrofit.utils.MyEventBus
import uz.gita.mycontactbyretrofit.utils.NetworkStatusValidator
import javax.inject.Inject

class EditContactViewModel : ViewModel() {
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

    fun editContact(id: Int, firstName: String, lastName: String, phone:String) {
        progressLiveData.value = true
        repository.editContact(
            id = id,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            successBlock = {
                progressLiveData.value = false
                if (networkStatusValidator.hasNetwork) messageLiveData.value = "Success!"
                else messageLiveData.value = "Save in local"

                MyEventBus.reloadEvent?.invoke()
                closeScreenLiveData.value= Unit
            },
            errorBlock = {
                progressLiveData.value = false
                errorMessageLiveData.value = it
            }
        )
    }

}