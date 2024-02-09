package uz.gita.mycontactbyretrofit.presentation.viewmodel.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.navigation.AppNavigator
import uz.gita.mycontactbyretrofit.presentation.viewmodel.EditContactViewModel
import uz.gita.mycontactbyretrofit.utils.MyEventBus
import uz.gita.mycontactbyretrofit.utils.NetworkStatusValidator
import javax.inject.Inject

@HiltViewModel
class EditContactViewModelImpl @Inject constructor(
    private val repository: AppRepository,
    val networkStatusValidator: NetworkStatusValidator,
    private val appNavController: AppNavigator
) : ViewModel(), EditContactViewModel {

    override val progress = MutableStateFlow<Boolean>(false)
    override var message: ((String) -> Unit)? = null
    override fun editContact(id: Int, firstName: String, lastName: String, phone: String) {
        progress.value = true
        repository.editContact(
            id = id,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            successBlock = {
                if (networkStatusValidator.hasNetwork) message?.invoke("Success!")
                else message?.invoke("Save in local")

                MyEventBus.reloadEvent?.invoke()
                viewModelScope.launch {
                    appNavController.back()
                }
            },
            errorBlock = {
                message?.invoke(it)
            }
        )
        progress.value = false
    }

}