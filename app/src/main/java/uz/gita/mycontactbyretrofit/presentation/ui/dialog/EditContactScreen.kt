package uz.gita.mycontactbyretrofit.presentation.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.databinding.ScreenAddContactBinding
import uz.gita.mycontactbyretrofit.databinding.ScreenEditContactBinding
import uz.gita.mycontactbyretrofit.presentation.viewmodel.EditContactViewModel
import uz.gita.mycontactbyretrofit.utils.myAddTextChangedListener
import uz.gita.mycontactbyretrofit.utils.myApply
import uz.gita.mycontactbyretrofit.utils.popBackStack
import uz.gita.mycontactbyretrofit.utils.showToast
import uz.gita.mycontactbyretrofit.utils.text

class EditContactScreen(val aaa: Int,val firstName: String,val lastName: String,val phone: String) : Fragment(R.layout.screen_edit_contact) {
    private val binding by viewBinding(ScreenEditContactBinding::bind)
    private val viewModel: EditContactViewModel by viewModels()
    private var prepareFirstName = false
    private var prepareLastName = false
    private var preparePhone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.errorMessageLiveData.observe(this, errorMessageObserver)
        viewModel.messageLiveData.observe(this, messageObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        binding.myApply {
            inputFirstName.setText(firstName)
            inputLastName.setText(lastName)
            inputPhone.setText(phone)
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

        binding.buttonBack.setOnClickListener { popBackStack() }
        binding.buttonAdd.setOnClickListener {
            viewModel.editContact(aaa, inputFirstName.text(), inputLastName.text(), inputPhone.text())

        }
        viewModel.closeScreenLiveData.observe(viewLifecycleOwner, closeScreenObserver)
        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
    }

    private fun check() {
        binding.buttonAdd.isEnabled = !( prepareFirstName && prepareLastName && preparePhone)
    }

    private val closeScreenObserver = Observer<Unit> { popBackStack() }
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
