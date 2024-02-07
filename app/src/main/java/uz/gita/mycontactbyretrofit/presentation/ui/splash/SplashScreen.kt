package uz.gita.mycontactbyretrofit.presentation.ui.splash
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.domain.AppRepositoryImpl
import uz.gita.mycontactbyretrofit.presentation.ui.ContactScreen
import uz.gita.mycontactbyretrofit.presentation.ui.login.LoginScreen
import uz.gita.mycontactbyretrofit.presentation.ui.register.RegisterScreen
import uz.gita.mycontactbyretrofit.utils.replaceScreen
import uz.gita.mycontactbyretrofit.utils.replaceScreenWithoutSave
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreen: Fragment(R.layout.screen_splash) {
    @Inject
    lateinit var appRepository: AppRepository
    private val splashTimeout: Long = 1000
    private var fragment: Fragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.screen_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment = if (appRepository.token == ""){
            RegisterScreen()
        }else{
            ContactScreen()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, splashTimeout)
    }

    private fun navigateToNextScreen() {
        replaceScreenWithoutSave(fragment!!)
    }
}
