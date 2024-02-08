package uz.gita.mycontactbyretrofit.presentation.viewmodel

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
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: MutableLiveData<String>
        get() = _errorMessage

    private val _verifyEvent = MutableLiveData<Boolean>()
    val verifyEvent: MutableLiveData<Boolean>
        get() = _verifyEvent

    fun verify(code: String) {
        viewModelScope.launch {
            appRepository.verifySmsCode(VerifySmsRequest(appRepository.phone, code)).onEach {
                when(it){
                    is ResultData.Success-> {
                        val verifySmsResponse = it.data
                        appRepository.token = verifySmsResponse.token
                        _verifyEvent.value = true
                    }
                    is ResultData.Failure -> {
                        errorMessage.value = it.message
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}
