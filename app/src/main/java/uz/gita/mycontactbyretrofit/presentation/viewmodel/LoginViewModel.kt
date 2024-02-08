package uz.gita.mycontactbyretrofit.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactadapterpattern.data.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mycontactbyretrofit.data.remote.request.LoginRequest
import uz.gita.mycontactbyretrofit.domain.AppRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val loginEvent = MutableLiveData<Boolean>()

    fun login(login: String, password: String) {
        appRepository.loginUser(LoginRequest(login,password))
            .onEach {
                when (it) {
                    is ResultData.Failure -> {
                        errorMessage.value = it.message
                    }
                    is ResultData.Success -> {
                        appRepository.token = it.data.token
                        this@LoginViewModel.loginEvent.value = true
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
