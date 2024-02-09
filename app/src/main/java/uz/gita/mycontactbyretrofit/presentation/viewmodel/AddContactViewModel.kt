package uz.gita.mycontactbyretrofit.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.utils.MyEventBus
import uz.gita.mycontactbyretrofit.utils.NetworkStatusValidator
import javax.inject.Inject

interface AddContactViewModel {
    val progressLiveData: StateFlow<Boolean>
    var messageLiveData: ((String)->Unit)?
    fun closeScreen()
    fun addContact(firstName: String, lastName: String, phone:String)
}