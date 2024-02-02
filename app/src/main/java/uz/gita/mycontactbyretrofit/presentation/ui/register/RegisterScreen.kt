package uz.gita.mycontactbyretrofit.presentation.ui.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.data.remote.request.RegisterRequest
import uz.gita.mycontactbyretrofit.databinding.ScreenSignBinding
import uz.gita.mycontactbyretrofit.presentation.ui.login.LoginScreen
import uz.gita.mycontactbyretrofit.presentation.ui.verify.VerifyScreen
import uz.gita.mycontactbyretrofit.presentation.viewmodel.RegisterViewModel
import uz.gita.mycontactbyretrofit.utils.replaceScreen
import uz.gita.mycontactbyretrofit.utils.replaceScreenWithoutSave

class RegisterScreen : Fragment(R.layout.screen_sign) {
    val binding by viewBinding(ScreenSignBinding::bind)
    val viewModel: RegisterViewModel by viewModels()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginText.setOnClickListener {
            replaceScreenWithoutSave(LoginScreen())
        }
        binding.create.setOnClickListener {
            viewModel.register(RegisterRequest(
                binding.firstName.text.toString(),
                binding.lastName.text.toString(),
                binding.phone.text.toString(),
                binding.password.text.toString()))
        }
        viewModel.register.observe(this){
            if (it){
                replaceScreen(VerifyScreen(binding.phone.text.toString()))
            }
        }
    }
}