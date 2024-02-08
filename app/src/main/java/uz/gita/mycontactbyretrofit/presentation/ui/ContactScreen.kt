package uz.gita.mycontactbyretrofit.presentation.ui

import uz.gita.mycontactbyretrofit.presentation.viewmodel.ContactViewModel
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.data.model.ContactUIData
import uz.gita.mycontactbyretrofit.databinding.ScreenContactBinding
import uz.gita.mycontactbyretrofit.presentation.ui.adapter.ContactAdapter
import uz.gita.mycontactbyretrofit.presentation.ui.dialog.EventDialog
import uz.gita.mycontactbyretrofit.utils.myApply
import uz.gita.mycontactbyretrofit.utils.showToast

@AndroidEntryPoint
class ContactScreen : Fragment(R.layout.screen_contact) {
    private val binding by viewBinding(ScreenContactBinding::bind)
    private val viewModel: ContactViewModel by viewModels()
    private val adapter= ContactAdapter()
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        adapter.clickListener = {
            val dialog = EventDialog()
            dialog.setClickEditButtonListener {
                navController.navigate(ContactScreenDirections.actionContactScreenToEditContactScreen(it.id,it.firstName,it.lastName,it.phone))
            }
            dialog.setClickDeleteButtonListener {
                viewModel.deleteContact(it.id, it.firstName, it.lastName, it.phone)
            }
            dialog.show(requireActivity().supportFragmentManager,null)
        }
        refreshLayout
        buttonRefresh.setOnClickListener {
            viewModel.loadAllContacts()
        }
        contactList.adapter = adapter
        contactList.layoutManager = LinearLayoutManager(requireContext())
        buttonAdd.setOnClickListener { viewModel.openAddContactDialog() }
        logOut.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Log Out")
                .setMessage("Do you want to log out?\nAll local changes will be deleted")
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Yes") { dialog, _ ->
                    viewModel.logOut()
                    dialog.dismiss()
                }
                .show()
        }

        viewModel.loadAllContacts()
        viewModel.logout.observe(this@ContactScreen,this@ContactScreen.logOut)
        viewModel.contactLiveData.observe(this@ContactScreen, contactObserver)
        viewModel.progressLiveData.observe(this@ContactScreen, progressObserver)
        viewModel.openAddContactDialogLiveData.observe(this@ContactScreen, openAddContactDialogObserver)
        viewModel.emptyStateLiveData.observe(viewLifecycleOwner, emptyStateObserver)
        viewModel.errorLiveData.observe(this@ContactScreen, errorObserver)
        viewModel.notConnectionLiveData.observe(this@ContactScreen, notConnectionObserver)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.refreshLayout.setOnRefreshListener {
            viewModel.loadAllContacts()
            refreshLayout.isRefreshing = false
        }
    }
    private val contactObserver = Observer<List<ContactUIData>> {
        binding.containerEmpty.visibility = View.GONE
        adapter.submitList(it)
    }

    private val progressObserver = Observer<Boolean> {
        binding.refreshLayout.isRefreshing = it
        binding.containerEmpty.visibility = View.GONE
    }
    private val emptyStateObserver = Observer<Unit> {
        binding.containerEmpty.visibility = View.VISIBLE
    }

    private val openAddContactDialogObserver = Observer<Unit> {
        navController.navigate(ContactScreenDirections.actionContactScreenToAddContactScreen())
    }

    private val errorObserver = Observer<String> {
        Snackbar.make(requireView(), it, Toast.LENGTH_SHORT).show()
    }

    private val notConnectionObserver = Observer<Unit> {
        showToast("Not connection")
    }
    private val logOut = Observer<Boolean> {
        if (it){
            navController.navigate(ContactScreenDirections.actionContactScreenToLoginScreen())
        }
    }

}

