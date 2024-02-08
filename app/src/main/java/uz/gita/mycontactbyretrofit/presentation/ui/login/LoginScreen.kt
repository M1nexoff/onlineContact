package uz.gita.mycontactbyretrofit.presentation.ui.login

import uz.gita.mycontactbyretrofit.presentation.viewmodel.LoginViewModel
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.databinding.ScreenLoginBinding

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.screen_login) {
    private val binding by viewBinding(ScreenLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels<LoginViewModel>()
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            viewModel.login(binding.phone.text.toString(), binding.password.text.toString())
        }

        viewModel.loginEvent.observe(viewLifecycleOwner) { success ->
            if (success) {
                navController.navigate(LoginScreenDirections.actionLoginScreenToContactScreen())
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show()
        }

        binding.createText.setOnClickListener {
            navController.navigate(LoginScreenDirections.actionLoginScreenToRegisterScreen())
        }


        binding.phone.addTextChangedListener {
            checkEnableButton()
        }
        binding.password.addTextChangedListener {
            checkEnableButton()
        }
    }

    private fun checkEnableButton() {
        binding.loginButton.isEnabled =
            binding.phone.text.toString().length == 13
                    && binding.password.text.toString().length > 7
    }
}
