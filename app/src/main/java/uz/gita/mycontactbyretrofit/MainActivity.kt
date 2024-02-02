package uz.gita.mycontactbyretrofit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import uz.gita.mycontactbyretrofit.presentation.ui.ContactScreen
import uz.gita.mycontactbyretrofit.presentation.ui.splash.SplashScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openScreenWithoutSave(SplashScreen())
    }

    private fun openScreenWithoutSave(fm: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fm)
            .commit()
    }
}


