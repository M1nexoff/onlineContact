package uz.gita.mycontactbyretrofit.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.gita.mycontactbyretrofit.data.remote.request.LoginRequest
import uz.gita.mycontactbyretrofit.data.remote.request.RegisterRequest
import uz.gita.mycontactbyretrofit.data.remote.response.LoginResponse
import uz.gita.mycontactbyretrofit.data.remote.response.RegisterResponse
import uz.gita.mycontactbyretrofit.domain.AppRepositoryImpl

class RegisterViewModel: ViewModel() {
    val appRepository = AppRepositoryImpl.getAppRepository()
    val errorMessage = MutableLiveData<String>()
    val register = MutableLiveData<Boolean>(false)
    fun register(request: RegisterRequest){
        appRepository.registerUser(request).enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful){
                    errorMessage.value = response.body()!!.message
                    this@RegisterViewModel.register.value = true
                }else{
                    if (response.code() in 400..499){
                        errorMessage.value = "Maydonlar xato kiritildi"
                    }else{
                        errorMessage.value = "Server ishlamayapti"
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                errorMessage.value = "Ulanishda xatolik"
            }
        })
    }
}