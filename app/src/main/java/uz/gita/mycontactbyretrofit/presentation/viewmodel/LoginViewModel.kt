package uz.gita.mycontactbyretrofit.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.gita.mycontactbyretrofit.data.remote.request.LoginRequest
import uz.gita.mycontactbyretrofit.data.remote.response.LoginResponse
import uz.gita.mycontactbyretrofit.domain.AppRepositoryImpl

class LoginViewModel: ViewModel() {
    val appRepository = AppRepositoryImpl.getAppRepository()
    val errorMessage = MutableLiveData<String>()
    val login = MutableLiveData<Boolean>(false)
    fun login(login: String, password: String){
        appRepository.loginUser(LoginRequest(login, password)).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    appRepository.token = response.body()!!.token
                    this@LoginViewModel.login.value = true
                }else{
                    if (response.code() in 400..499){
                        errorMessage.value = "Login yoki parol xato"
                    }else{
                        errorMessage.value = "Server ishlamayapti"
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                errorMessage.value = "Ulanishda xatolik"
            }
        })
    }
}