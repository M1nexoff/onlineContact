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
class EditContactViewModel @Inject constructor(private val repository: AppRepository, val networkStatusValidator: NetworkStatusValidator) : ViewModel() {
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