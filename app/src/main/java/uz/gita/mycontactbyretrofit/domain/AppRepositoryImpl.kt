package uz.gita.mycontactbyretrofit.domain

import android.content.Context
import android.content.SharedPreferences
import retrofit2.Call
import uz.gita.mycontactbyretrofit.app.App
import uz.gita.mycontactbyretrofit.data.remote.api.Api
import uz.gita.mycontactbyretrofit.data.remote.request.CreateContactRequest
import uz.gita.mycontactbyretrofit.data.remote.request.EditContactRequest
import uz.gita.mycontactbyretrofit.data.remote.request.LoginRequest
import uz.gita.mycontactbyretrofit.data.remote.request.RegisterRequest
import uz.gita.mycontactbyretrofit.data.remote.request.VerifySmsRequest
import uz.gita.mycontactbyretrofit.data.remote.response.ContactResponse
import uz.gita.mycontactbyretrofit.data.remote.response.LoginResponse
import uz.gita.mycontactbyretrofit.data.remote.response.RegisterResponse
import uz.gita.mycontactbyretrofit.data.remote.response.VerifySmsResponse

class AppRepositoryImpl private constructor(val api:Api,val pref: SharedPreferences): AppRepository {
    override var token: String = pref.getString("token","")!!
        set(value) {
            field = value
            pref.edit().putString("token",value).apply()
        }
    override var phone: String = pref.getString("phone","")!!
        set(value) {
            field = value
            pref.edit().putString("phone",value).apply()
        }

    companion object {
        @Volatile
        private lateinit var instance: AppRepository

        fun init(api: Api, context: Context) {
            if (!(::instance.isInitialized)) {
                instance = AppRepositoryImpl(api,context.getSharedPreferences("Contact", Context.MODE_PRIVATE))
            }
        }

        fun getAppRepository(): AppRepository = instance
    }

    override fun registerUser(registerRequest: RegisterRequest): Call<RegisterResponse> {
        return api.registerUser(registerRequest)
    }

    override fun verifySmsCode(verifySmsRequest: VerifySmsRequest): Call<VerifySmsResponse> {
        return api.verifySmsCode(verifySmsRequest)
    }

    override fun loginUser(loginRequest: LoginRequest): Call<LoginResponse> {
        return api.loginUser(loginRequest)
    }

    override fun addContact(
        createContactRequest: CreateContactRequest
    ): Call<ContactResponse> {
        return api.addContact(token, createContactRequest)
    }

    override fun editContact(
        editContactRequest: EditContactRequest
    ): Call<ContactResponse> {
        return api.editContactByID(token,editContactRequest)
    }

    override fun deleteContact(contactId: Int): Call<Unit> {
        return api.deleteContactByID(token,contactId)
    }

    override fun getAllContacts(): Call<List<ContactResponse>> {
        return api.getAllContacts(token)
    }

}