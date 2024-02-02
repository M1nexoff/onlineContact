package uz.gita.mycontactbyretrofit.domain

import retrofit2.Call
import uz.gita.mycontactbyretrofit.data.remote.request.CreateContactRequest
import uz.gita.mycontactbyretrofit.data.remote.request.EditContactRequest
import uz.gita.mycontactbyretrofit.data.remote.request.LoginRequest
import uz.gita.mycontactbyretrofit.data.remote.request.RegisterRequest
import uz.gita.mycontactbyretrofit.data.remote.request.VerifySmsRequest
import uz.gita.mycontactbyretrofit.data.remote.response.ContactResponse
import uz.gita.mycontactbyretrofit.data.remote.response.LoginResponse
import uz.gita.mycontactbyretrofit.data.remote.response.RegisterResponse
import uz.gita.mycontactbyretrofit.data.remote.response.VerifySmsResponse

interface AppRepository {
    var token: String
    var phone: String

    // 1. Register User
    fun registerUser(registerRequest: RegisterRequest): Call<RegisterResponse>

    // 2. Verify SMS Code
    fun verifySmsCode(verifySmsRequest: VerifySmsRequest): Call<VerifySmsResponse>

    // 3. Login User
    fun loginUser(loginRequest: LoginRequest): Call<LoginResponse>

    // 4. Add Contact
    fun addContact( createContactRequest: CreateContactRequest): Call<ContactResponse>

    // 5. Edit Contact
    fun editContact( editContactRequest: EditContactRequest): Call<ContactResponse>

    // 6. Delete Contact
    fun deleteContact( contactId: Int): Call<Unit>

    // 7. Get All Contacts
    fun getAllContacts(): Call<List<ContactResponse>>

}
