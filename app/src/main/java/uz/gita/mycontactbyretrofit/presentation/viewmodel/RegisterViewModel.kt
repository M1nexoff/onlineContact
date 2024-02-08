package uz.gita.mycontactbyretrofit.presentation.viewmodel

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
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val registerEvent = MutableLiveData<Boolean>()

    fun register(request: RegisterRequest) {
        appRepository.registerUser(request).onEach {
            when(it){
                is ResultData.Success-> {
                    val registerResponse = it.data
                    errorMessage.value = registerResponse.message
                    registerEvent.value = true
                }
                is ResultData.Failure -> {
                    errorMessage.value = it.message
                    Timber.d("${it.message}")
                }
            }
        }.launchIn(viewModelScope)
    }
}
