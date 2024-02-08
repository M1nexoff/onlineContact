package uz.gita.mycontactbyretrofit.presentation.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.databinding.ScreenEditContactBinding
import uz.gita.mycontactbyretrofit.presentation.viewmodel.EditContactViewModel
import uz.gita.mycontactbyretrofit.utils.myAddTextChangedListener
import uz.gita.mycontactbyretrofit.utils.myApply
import uz.gita.mycontactbyretrofit.utils.showToast
import uz.gita.mycontactbyretrofit.utils.text

@AndroidEntryPoint
class EditContactScreen : Fragment(R.layout.screen_edit_contact) {
    private val binding by viewBinding(ScreenEditContactBinding::bind)
    private val viewModel: EditContactViewModel by viewModels()
    private var prepareFirstName = false
    private var prepareLastName = false
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    private var preparePhone = false
    private val navArgs : EditContactScreenArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.errorMessageLiveData.observe(this, errorMessageObserver)
        viewModel.messageLiveData.observe(this, messageObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        binding.myApply {
            inputFirstName.setText(navArgs.firstName)
            inputLastName.setText(navArgs.lastName)
            inputPhone.setText(navArgs.phone)
        }
        binding.inputFirstName.myAddTextChangedListener {
            prepareFirstName = it.length > 3
            check()
        }
        binding.inputLastName.myAddTextChangedListener {
            prepareLastName = it.length > 3
            check()
        }
        binding.inputPhone.myAddTextChangedListener {
            preparePhone = it.startsWith("+998") && it.length == 13
            check()
        }

        binding.buttonBack.setOnClickListener {
            navController.navigateUp()
        }
        binding.buttonAdd.setOnClickListener {
            viewModel.editContact(navArgs.id, inputFirstName.text(), inputLastName.text(), inputPhone.text())
        }
        viewModel.closeScreenLiveData.observe(viewLifecycleOwner, closeScreenObserver)
        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
    }

    private fun check() {
        binding.buttonAdd.isEnabled = !( prepareFirstName && prepareLastName && preparePhone)
    }

    private val closeScreenObserver = Observer<Unit> { navController.navigateUp() }
    private val errorMessageObserver = Observer<String> { showToast(it) }
    private val messageObserver = Observer<String> { showToast(it) }
    private val progressObserver = Observer<Boolean> {
        if (it) {
            binding.buttonAdd.visibility = View.GONE
            binding.frameLoading.visibility = View.VISIBLE
            binding.progress.show()
        } else {
            binding.buttonAdd.visibility = View.VISIBLE
            binding.frameLoading.visibility = View.GONE
            binding.progress.hide()
        }
    }
}
