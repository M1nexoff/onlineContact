package uz.gita.mycontactbyretrofit.presentation.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactadapterpattern.data.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.HttpException
import uz.gita.mycontactbyretrofit.data.remote.request.VerifySmsRequest
import uz.gita.mycontactbyretrofit.data.remote.response.VerifySmsResponse
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.navigation.AppNavigator
import uz.gita.mycontactbyretrofit.presentation.ui.verify.VerifyScreen
import uz.gita.mycontactbyretrofit.presentation.ui.verify.VerifyScreenDirections
import uz.gita.mycontactbyretrofit.presentation.viewmodel.VerifyViewModel
import javax.inject.Inject

@HiltViewModel
class VerifyViewModelImpl @Inject constructor(
    private val appRepository: AppRepository,
    private val appNavController : AppNavigator
) : ViewModel(),VerifyViewModel {

    override var errorMessage: ((String) -> Unit)? = null
    override fun verify(code: String) {
        viewModelScope.launch {
            appRepository.verifySmsCode(VerifySmsRequest(appRepository.phone, code)).onEach {
                when(it){
                    is ResultData.Success-> {
                        val verifySmsResponse = it.data
                        appRepository.token = verifySmsResponse.token
                        appNavController.navigateTo(VerifyScreenDirections.actionVerifyScreenToContactScreen())
                    }
                    is ResultData.Failure -> {
                        errorMessage?.invoke(it.message)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}
