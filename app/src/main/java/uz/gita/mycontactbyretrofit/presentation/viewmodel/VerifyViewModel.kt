package uz.gita.mycontactbyretrofit.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.gita.mycontactbyretrofit.data.remote.request.LoginRequest
import uz.gita.mycontactbyretrofit.data.remote.request.VerifySmsRequest
import uz.gita.mycontactbyretrofit.data.remote.response.LoginResponse
import uz.gita.mycontactbyretrofit.data.remote.response.VerifySmsResponse
import uz.gita.mycontactbyretrofit.domain.AppRepositoryImpl

class VerifyViewModel : ViewModel() {
    val appRepository = AppRepositoryImpl.getAppRepository()
    val errorMessage = MutableLiveData<String>()
    val verify = MutableLiveData<Boolean>(false)
    fun verify(code: String){
        appRepository.verifySmsCode(VerifySmsRequest(appRepository.phone, code)).enqueue(object :
            Callback<VerifySmsResponse> {
            override fun onResponse(call: Call<VerifySmsResponse>, response: Response<VerifySmsResponse>) {
                if (response.isSuccessful){
                    appRepository.token = response.body()!!.token
                    this@VerifyViewModel.verify.value = true
                }else{
                    if (response.code() in 400..499){
                        errorMessage.value = "Kod xato"
                    }else{
                        errorMessage.value = "Server ishlamayapti"
                    }
                }
            }

            override fun onFailure(call: Call<VerifySmsResponse>, t: Throwable) {
                errorMessage.value = "Ulanishda xatolik"
            }
        })
    }
}