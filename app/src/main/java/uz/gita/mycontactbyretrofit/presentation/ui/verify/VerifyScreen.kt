package uz.gita.mycontactbyretrofit.presentation.ui.verify

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.databinding.ScreenVerifyBinding
import uz.gita.mycontactbyretrofit.domain.AppRepositoryImpl
import uz.gita.mycontactbyretrofit.presentation.ui.ContactScreen
import uz.gita.mycontactbyretrofit.presentation.ui.register.RegisterScreen
import uz.gita.mycontactbyretrofit.presentation.viewmodel.VerifyViewModel
import uz.gita.mycontactbyretrofit.utils.replaceScreenWithoutSave

class VerifyScreen(val phone: String): Fragment(R.layout.screen_verify) {
    val appRepository = AppRepositoryImpl.getAppRepository()
    val binding by viewBinding(ScreenVerifyBinding::bind)
    val viewModel: VerifyViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appRepository.phone = this.phone
        binding.verifyButton.setOnClickListener {
            viewModel.verify(binding.code.text.toString())
        }
        binding.createText.setOnClickListener {
            replaceScreenWithoutSave(RegisterScreen())
        }
        viewModel.verify.observe(viewLifecycleOwner){
            if (it){
                replaceScreenWithoutSave(ContactScreen())
            }
        }
    }
}