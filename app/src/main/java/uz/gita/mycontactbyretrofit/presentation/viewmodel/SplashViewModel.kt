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

interface SplashViewModel {
    fun checkAuthentication()
}
