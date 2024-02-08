package uz.gita.mycontactbyretrofit.app

import android.app.Application
import android.content.pm.ApplicationInfo
import com.google.gson.internal.GsonBuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}

