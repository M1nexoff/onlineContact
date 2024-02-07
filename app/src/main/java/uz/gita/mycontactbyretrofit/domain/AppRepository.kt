package uz.gita.mycontactbyretrofit.domain

import retrofit2.Call
import uz.gita.mycontactbyretrofit.data.model.ContactUIData
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
    fun addContact(
        firstName: String,
        lastName: String,
        phone: String,
        successBlock: () -> Unit,
        errorBlock: (String) -> Unit
    )

    // 5. Edit Contact
    fun editContact(
        id: Int,
        firstName: String,
        lastName: String,
        phone: String,
        successBlock: () -> Unit,
        errorBlock: (String) -> Unit
    )

    // 6. Delete Contact
    fun deleteContact(
        id: Int,
        firstName: String,
        lastName: String,
        phone: String,
        successBlock: () -> Unit,
        errorBlock: (String) -> Unit
    )

    // 7. Get All Contacts
    fun getAllContacts(successBlock: (List<ContactUIData>) -> Unit, errorBlock: (String) -> Unit)

    fun syncWithServer(finishBlock: () -> Unit, errorBlock:(String) -> Unit)
}
