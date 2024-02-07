package uz.gita.mycontactbyretrofit.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.mycontactbyretrofit.data.model.ContactUIData
import uz.gita.mycontactbyretrofit.domain.AppRepository
import javax.inject.Inject

class ContactViewModel : ViewModel() {
    val emptyStateLiveData = MutableLiveData(Unit)
    val notConnectionLiveData = MutableLiveData(Unit)
    @Inject
    lateinit var appRepository : AppRepository

    val contactLiveData = MutableLiveData<List<ContactUIData>>()
    val progressLiveData = MutableLiveData<Boolean>()
    val openAddContactDialogLiveData = MutableLiveData<Unit>()
    val errorLiveData = MutableLiveData<String>()
    val logOut = MutableLiveData(false)
    fun loadAllContacts() {
        progressLiveData.value = true
        appRepository.getAllContacts(
            successBlock = {
                progressLiveData.value = false
                if (it.isEmpty()) emptyStateLiveData.value = Unit
                contactLiveData.value = it
            },
            errorBlock = {
                errorLiveData.value = it
            }
        )
    }

    fun openAddContactDialog() {
        openAddContactDialogLiveData.value = Unit
    }

    fun deleteContact(id: Int, firstName: String, lastName: String, phone:String) {
        progressLiveData.value = true
        appRepository.deleteContact(
            id = id,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            successBlock = {
                loadAllContacts()
                progressLiveData.value = false
            },
            errorBlock = {
                errorLiveData.value = it
                progressLiveData.value = false
            }
        )
    }

    fun logOut() {
        appRepository.token = ""
        logOut.value = true
    }

}

