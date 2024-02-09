package uz.gita.mycontactbyretrofit.presentation.ui.verify

import uz.gita.mycontactbyretrofit.presentation.viewmodel.VerifyViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.databinding.ScreenVerifyBinding
import uz.gita.mycontactbyretrofit.presentation.viewmodel.impl.VerifyViewModelImpl

@AndroidEntryPoint
class VerifyScreen: Fragment(R.layout.screen_verify) {
    private val binding by viewBinding(ScreenVerifyBinding::bind)
    private val viewModel: VerifyViewModel by viewModels<VerifyViewModelImpl>()
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.verifyButton.setOnClickListener {
            viewModel.verify(binding.code.text.toString())
        }
        binding.createText.setOnClickListener {
            navController.navigateUp()
        }
    }
}