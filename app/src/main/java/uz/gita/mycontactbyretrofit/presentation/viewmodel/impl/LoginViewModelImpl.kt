package uz.gita.mycontactbyretrofit.presentation.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactadapterpattern.data.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mycontactbyretrofit.data.remote.request.LoginRequest
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.navigation.AppNavigator
import uz.gita.mycontactbyretrofit.presentation.ui.login.LoginScreen
import uz.gita.mycontactbyretrofit.presentation.ui.login.LoginScreenDirections
import uz.gita.mycontactbyretrofit.presentation.viewmodel.LoginViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val appRepository: AppRepository,
    private val appNavController : AppNavigator
) : ViewModel(),
    LoginViewModel {
    override var errorMessage: ((String) -> Unit)? = null

    override fun login(login: String, password: String) {
        appRepository.loginUser(LoginRequest(login,password))
            .onEach {
                when (it) {
                    is ResultData.Failure -> {
                        errorMessage?.invoke(it.message)
                    }
                    is ResultData.Success -> {
                        appRepository.token = it.data.token
                        appNavController.navigateTo(LoginScreenDirections.actionLoginScreenToContactScreen())
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
