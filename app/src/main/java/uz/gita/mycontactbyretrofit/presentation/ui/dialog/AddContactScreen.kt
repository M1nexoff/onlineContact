package uz.gita.mycontactbyretrofit.presentation.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.databinding.ScreenAddContactBinding
import uz.gita.mycontactbyretrofit.presentation.viewmodel.AddContactViewModel
import uz.gita.mycontactbyretrofit.presentation.viewmodel.impl.AddContactViewModelImpl
import uz.gita.mycontactbyretrofit.utils.myAddTextChangedListener
import uz.gita.mycontactbyretrofit.utils.myApply
import uz.gita.mycontactbyretrofit.utils.showToast
import uz.gita.mycontactbyretrofit.utils.text

@AndroidEntryPoint
class AddContactScreen : Fragment(R.layout.screen_add_contact){
    private val binding by viewBinding(ScreenAddContactBinding::bind)
    private val viewModel : AddContactViewModel by viewModels<AddContactViewModelImpl>()
    private var prepareFirstName = false
    private var prepareLastName = false
    private var preparePhone = false
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.messageLiveData = {
            showToast(it)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
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
        binding.buttonAdd.isEnabled = false

        binding.buttonBack.setOnClickListener { viewModel.closeScreen() }
        binding.buttonAdd.setOnClickListener { viewModel.addContact(inputFirstName.text(), inputLastName.text(), inputPhone.text()) }
        viewModel.progressLiveData
            .onEach {
                binding.buttonAdd.visibility = if (it) View.GONE else View.VISIBLE
                binding.frameLoading.visibility = if (it) View.VISIBLE else View.GONE
                if (it) binding.progress.show() else binding.progress.hide()
            }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
    }

    private fun check() {
        binding.buttonAdd.isEnabled = prepareFirstName && prepareLastName && preparePhone
        val a = 1
        val b = 2
        val c = 3
        val d = 4
        val e = 5
        val f = 6
        val g = 7
        val h = 8
        val i = 9
        val j = 10
        val k = 11
        val l = 12
        val m = 13
        val n = 14
        val o = 15
        val p = 16
        val q = 17
    }
    private val progressObserver = Observer<Boolean> {
        if (it) {
            binding.buttonAdd.visibility = View.GONE
            binding.frameLoading.visibility = View.VISIBLE
            binding.progress.show()
        } else  {
            binding.buttonAdd.visibility = View.VISIBLE
            binding.frameLoading.visibility = View.GONE
            binding.progress.hide()
        }
    }
}





