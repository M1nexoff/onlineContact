package uz.gita.mycontactbyretrofit.presentation.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.databinding.ScreenSplashBinding
import uz.gita.mycontactbyretrofit.presentation.viewmodel.SplashViewModel
import javax.inject.Inject
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SplashScreen : Fragment() {

    private lateinit var binding: ScreenSplashBinding
    private val viewModel: SplashViewModel by viewModels()
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ScreenSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkAuthentication()
        observeNavigation()
    }

    private fun observeNavigation() {
        viewModel.navigateToNextScreen.observe(viewLifecycleOwner) { destination ->
            if (destination){
                navController.navigate(SplashScreenDirections.actionSplashScreenToContactScreen())
            }else{
                navController.navigate(SplashScreenDirections.actionSplashScreenToLoginScreen())
            }
        }
    }
}
