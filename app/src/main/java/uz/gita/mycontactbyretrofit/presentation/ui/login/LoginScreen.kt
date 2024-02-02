package uz.gita.mycontactbyretrofit.presentation.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.databinding.ScreenLoginBinding
import uz.gita.mycontactbyretrofit.presentation.ui.ContactScreen
import uz.gita.mycontactbyretrofit.presentation.ui.register.RegisterScreen
import uz.gita.mycontactbyretrofit.presentation.viewmodel.LoginViewModel
import uz.gita.mycontactbyretrofit.utils.replaceScreenWithoutSave

class LoginScreen: Fragment(R.layout.screen_login) {
    val binding by viewBinding(ScreenLoginBinding::bind)
    val viewModel: LoginViewModel by viewModels()
    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            viewModel.login(binding.phone.text.toString(),binding.password.text.toString())
        }
        viewModel.login.observe(this){
            if (it){
                replaceScreenWithoutSave(ContactScreen())
            }
        }
        viewModel.errorMessage.observe(this){
            Snackbar.make(requireView(),it,4000).show()
        }
        binding.createText.setOnClickListener {
            replaceScreenWithoutSave(RegisterScreen())
        }
    }
}