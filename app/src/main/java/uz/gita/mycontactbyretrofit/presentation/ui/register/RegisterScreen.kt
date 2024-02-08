package uz.gita.mycontactbyretrofit.presentation.ui.register

import uz.gita.mycontactbyretrofit.presentation.viewmodel.RegisterViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.data.remote.request.RegisterRequest
import uz.gita.mycontactbyretrofit.databinding.ScreenSignBinding
import uz.gita.mycontactbyretrofit.domain.AppRepository
import javax.inject.Inject

@AndroidEntryPoint
class RegisterScreen: Fragment(R.layout.screen_sign) {
    @Inject lateinit var appRepository: AppRepository
    private val binding by viewBinding(ScreenSignBinding::bind)
    private val viewModel: RegisterViewModel by viewModels()
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginText.setOnClickListener {
            navController.navigateUp()
        }
        binding.create.setOnClickListener {
            viewModel.register(RegisterRequest(
                binding.firstName.text.toString(),
                binding.lastName.text.toString(),
                binding.phone.text.toString(),
                binding.password.text.toString()))
        }
        viewModel.registerEvent.observe(this){
            if (it){
                appRepository.phone = binding.phone.text.toString()
                navController.navigate(RegisterScreenDirections.actionRegisterScreenToVerifyScreen())
            }
        }
    }
}