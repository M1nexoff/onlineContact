package uz.gita.mycontactbyretrofit.presentation.viewmodel.impl

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactadapterpattern.data.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import uz.gita.mycontactbyretrofit.data.remote.request.RegisterRequest
import uz.gita.mycontactbyretrofit.data.remote.response.RegisterResponse
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.navigation.AppNavigator
import uz.gita.mycontactbyretrofit.presentation.ui.register.RegisterScreenDirections
import uz.gita.mycontactbyretrofit.presentation.viewmodel.RegisterViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModelImpl @Inject constructor(
    private val appRepository: AppRepository,
    private val appNavController : AppNavigator
) : ViewModel(),RegisterViewModel {

    override val errorMessage: ((String) -> Unit)? = null
    override fun register(request: RegisterRequest) {
        appRepository.registerUser(request).onEach {
            when(it){
                is ResultData.Success-> {
                    val registerResponse = it.data
                    errorMessage?.invoke(registerResponse.message)
                    appRepository.phone = request.phone
                    appNavController.navigateTo(RegisterScreenDirections.actionRegisterScreenToVerifyScreen())
                }
                is ResultData.Failure -> {
                    errorMessage?.invoke(it.message)
                    Timber.d("${it.message}")
                }
            }
        }.launchIn(viewModelScope)
    }
}
