package uz.gita.mycontactbyretrofit.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.navigation.AppNavigator
import uz.gita.mycontactbyretrofit.presentation.ui.splash.SplashScreenDirections
import uz.gita.mycontactbyretrofit.presentation.viewmodel.SplashViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModelImpl @Inject constructor(
    private val appRepository: AppRepository,
    private val appNavController : AppNavigator
) : ViewModel() , SplashViewModel {
    override fun checkAuthentication() {
        viewModelScope.launch {
            delay(1000)
            val destination = appRepository.token.isEmpty()
            val direction =
                if(destination) SplashScreenDirections.actionSplashScreenToContactScreen()
                else SplashScreenDirections.actionSplashScreenToLoginScreen()
            appNavController.navigateTo(direction)

        }
    }
}
