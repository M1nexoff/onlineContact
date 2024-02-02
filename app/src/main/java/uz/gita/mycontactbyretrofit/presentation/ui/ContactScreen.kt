package uz.gita.mycontactbyretrofit.presentation.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.data.remote.response.ContactResponse
import uz.gita.mycontactbyretrofit.databinding.ScreenContactBinding
import uz.gita.mycontactbyretrofit.presentation.ui.adapter.ContactAdapter
import uz.gita.mycontactbyretrofit.presentation.ui.dialog.AddContactDialog
import uz.gita.mycontactbyretrofit.presentation.ui.dialog.EditContactDialog
import uz.gita.mycontactbyretrofit.presentation.ui.dialog.EventDialog
import uz.gita.mycontactbyretrofit.presentation.ui.login.LoginScreen
import uz.gita.mycontactbyretrofit.presentation.viewmodel.ContactViewModel
import uz.gita.mycontactbyretrofit.utils.myApply
import uz.gita.mycontactbyretrofit.utils.replaceScreenWithoutSave

class ContactScreen : Fragment(R.layout.screen_contact) {
    private val binding by viewBinding(ScreenContactBinding::bind)
    private val viewModel: ContactViewModel by viewModels()
    private val adapter= ContactAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        adapter.clickListener = {
            val dialog = EventDialog()
            dialog.setClickEditButtonListener {
                val edit = EditContactDialog(requireContext(),it.firstName,it.lastName,it.phone)
                edit.setEditContactListener { first, last, phone ->
                    viewModel.updateContact(it.id,first, last, phone)
                }
                edit.show()
            }
            dialog.setClickDeleteButtonListener {
                viewModel.deleteContact(it.id)
            }
            dialog.show(requireActivity().supportFragmentManager,null)
        }
        contactList.adapter = adapter
        contactList.layoutManager = LinearLayoutManager(requireContext())
        buttonAdd.setOnClickListener { viewModel.openAddContactDialog() }
        logOut.setOnClickListener {viewModel.logOut()}

        viewModel.loadAllContacts()
        viewModel.logOut.observe(viewLifecycleOwner,this@ContactScreen.logOut)
        viewModel.contactLiveData.observe(viewLifecycleOwner, contactObserver)
        viewModel.progressLiveData.observe(viewLifecycleOwner, progressObserver)
        viewModel.openAddContactDialogLiveData.observe(viewLifecycleOwner, openAddContactDialogObserver)
        viewModel.errorLiveData.observe(viewLifecycleOwner, errorObserver)
    }
    private val contactObserver = Observer<List<ContactResponse>> {
        adapter.submitList(it)
    }

    private val progressObserver = Observer<Boolean> {
        if (it) binding.progress.show()
        else binding.progress.hide()
    }

    private val openAddContactDialogObserver = Observer<Unit> {
        val dialog = AddContactDialog()
        dialog.setAddContactListener { firstName, lastName, phone ->
            viewModel.addContact(firstName, lastName, phone)
        }
        dialog.show(childFragmentManager, "ADD_CONTACT")
    }

    private val errorObserver = Observer<String> {
        Snackbar.make(requireView(), it, Toast.LENGTH_SHORT).show()
    }
    private val logOut = Observer<Boolean> {
        if (it){
            replaceScreenWithoutSave(LoginScreen())
        }
    }
}

