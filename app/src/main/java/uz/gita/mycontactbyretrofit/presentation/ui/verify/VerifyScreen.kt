package uz.gita.mycontactbyretrofit.presentation.ui.verify

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.databinding.ScreenVerifyBinding
import uz.gita.mycontactbyretrofit.di.RepositoryModule
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.domain.AppRepositoryImpl
import uz.gita.mycontactbyretrofit.presentation.ui.ContactScreen
import uz.gita.mycontactbyretrofit.presentation.ui.register.RegisterScreen
import uz.gita.mycontactbyretrofit.presentation.viewmodel.VerifyViewModel
import uz.gita.mycontactbyretrofit.utils.popBackStack
import uz.gita.mycontactbyretrofit.utils.replaceScreenWithoutSave
import javax.inject.Inject

@AndroidEntryPoint
class VerifyScreen: Fragment(R.layout.screen_verify) {
    private val binding by viewBinding(ScreenVerifyBinding::bind)
    private val viewModel: VerifyViewModel by viewModels()
    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.verifyButton.setOnClickListener {
            viewModel.verify(binding.code.text.toString())
        }
        binding.createText.setOnClickListener {
            popBackStack()
        }
        viewModel.verify.observe(this){
            if (it){
                replaceScreenWithoutSave(ContactScreen())
            }
        }
    }
}