package uz.gita.mycontactbyretrofit.app

import android.app.Application
import uz.gita.mycontactbyretrofit.data.remote.Client
import uz.gita.mycontactbyretrofit.data.remote.api.Api
import uz.gita.mycontactbyretrofit.domain.AppRepositoryImpl

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppRepositoryImpl.init(Client.retrofit.create(Api::class.java),this)
    }
}

