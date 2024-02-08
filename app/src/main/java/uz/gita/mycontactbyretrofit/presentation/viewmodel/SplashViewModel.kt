package uz.gita.mycontactbyretrofit.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.mycontactbyretrofit.domain.AppRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _navigateToNextScreen = MutableLiveData(false)
    val navigateToNextScreen: LiveData<Boolean> = _navigateToNextScreen

    fun checkAuthentication() {
        viewModelScope.launch {
            delay(1000)
            val destination = appRepository.token.isEmpty()
            _navigateToNextScreen.value = destination
        }
    }
}
