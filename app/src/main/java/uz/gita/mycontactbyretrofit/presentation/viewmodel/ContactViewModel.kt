package uz.gita.mycontactbyretrofit.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.gita.mycontactbyretrofit.data.remote.Client
import uz.gita.mycontactbyretrofit.data.remote.api.Api
import uz.gita.mycontactbyretrofit.data.remote.request.CreateContactRequest
import uz.gita.mycontactbyretrofit.data.remote.request.EditContactRequest
import uz.gita.mycontactbyretrofit.data.remote.response.ContactResponse
import uz.gita.mycontactbyretrofit.domain.AppRepositoryImpl

class ContactViewModel : ViewModel() {
    private val appRepositoryImpl = AppRepositoryImpl.getAppRepository() //ApiClient.retrofit.create(ContactApi::class.java)

    val contactLiveData = MutableLiveData<List<ContactResponse>>()
    val progressLiveData = MutableLiveData<Boolean>()
    val openAddContactDialogLiveData = MutableLiveData<Unit>()
    val errorLiveData = MutableLiveData<String>()
    val logOut = MutableLiveData<Boolean>(false)
    fun loadAllContacts() {
        progressLiveData.value = true
        appRepositoryImpl.getAllContacts().enqueue(object : Callback<List<ContactResponse>> {
            override fun onResponse(call: Call<List<ContactResponse>>, response: Response<List<ContactResponse>>) {
                progressLiveData.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        contactLiveData.value = it
                    }
                } else {
                    response.errorBody().toString()
                }
            }

            override fun onFailure(call: Call<List<ContactResponse>>, t: Throwable) {
                progressLiveData.value = false
            }
        })

    }

    fun openAddContactDialog() {
        openAddContactDialogLiveData.value = Unit
    }
    fun addContact(firstName: String, lastName: String, phone: String) {
        progressLiveData.value = true

        val newContactRequest = CreateContactRequest(
            firstName = firstName,
            lastName = lastName,
            phone = phone
        )

        appRepositoryImpl.addContact(newContactRequest).enqueue(object : Callback<ContactResponse> {
            override fun onResponse(call: Call<ContactResponse>, response: Response<ContactResponse>) {
                progressLiveData.value = false
                if (response.isSuccessful) {
                    loadAllContacts()
                } else {
                    if (response.errorBody() != null) {
                        val gson = Gson()
                        val data = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                        errorLiveData.value = data.message

                    } else errorLiveData.value = "Unknown error!"                }
            }

            override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                progressLiveData.value = false
                errorLiveData.value = "${t.message}"
            }
        })
    }

    fun updateContact(id: Int, first: String, last: String, phone: String) {
        progressLiveData.value = true
        val editContact = EditContactRequest(id,first,last,phone)
        appRepositoryImpl.editContact(editContact).enqueue(object : Callback<ContactResponse>{
            override fun onResponse(call: Call<ContactResponse>, response: Response<ContactResponse>) {
                progressLiveData.value = false
                if (response.isSuccessful){
                    loadAllContacts()
                }else{
                    if (response.errorBody() != null) {
                        val gson = Gson()
                        val data = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                        errorLiveData.value = data.message

                    } else errorLiveData.value = "Unknown error!"                }
            }

            override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                progressLiveData.value = false
                errorLiveData.value = "${t.message}"
            }
        })
    }

    fun deleteContact(id: Int) {
        progressLiveData.value = true
        appRepositoryImpl.deleteContact(id).enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                progressLiveData.value = false
                if (response.isSuccessful){
                    loadAllContacts()
                }else{
                    if (response.errorBody() != null) {
                        val gson = Gson()
                        val data = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                        errorLiveData.value = data.message

                    } else errorLiveData.value = "Unknown error!"
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                progressLiveData.value = false
                errorLiveData.value = "${t.message}"
            }

        })
    }

    fun logOut() {
        appRepositoryImpl.token = ""
        logOut.value = true
    }

}
data class ErrorResponse(
    val message: String
)

