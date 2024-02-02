package uz.gita.mycontactbyretrofit.data.remote

import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.gita.mycontactbyretrofit.app.App

object Client {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://35bd-82-215-107-8.ngrok-free.app")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}