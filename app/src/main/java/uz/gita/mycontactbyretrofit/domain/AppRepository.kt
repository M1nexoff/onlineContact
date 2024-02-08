package uz.gita.mycontactbyretrofit.domain

import com.example.contactadapterpattern.data.ResultData
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
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
    fun registerUser(registerRequest: RegisterRequest): Flow<ResultData<RegisterResponse>>

    // 2. Verify SMS Code
    fun verifySmsCode(verifySmsRequest: VerifySmsRequest): Flow<ResultData<VerifySmsResponse>>

    // 3. Login User
    fun loginUser(loginRequest: LoginRequest): Flow<ResultData<LoginResponse>>

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
    fun getAllContacts(): Flow<ResultData<List<ContactUIData>>>

    fun syncWithServer(finishBlock: () -> Unit, errorBlock:(String) -> Unit)
}
