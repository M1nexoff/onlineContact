package uz.gita.mycontactbyretrofit.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.contactadapterpattern.data.ResultData
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mycontactbyretrofit.data.model.ContactUIData

interface ContactViewModel {
    val contactLiveData: LiveData<List<ContactUIData>>
    val progressStateFlow : StateFlow<Boolean>
    val errorLiveData: LiveData<String>
    var logout: (()->Unit)?
    fun loadAllContacts()
    fun openAddContactDialog()
    fun deleteContact(id: Int, firstName: String, lastName: String, phone: String)
    fun logOut()
}
