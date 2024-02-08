package uz.gita.mycontactbyretrofit.data.remote.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import uz.gita.mycontactbyretrofit.data.remote.request.CreateContactRequest
import uz.gita.mycontactbyretrofit.data.remote.request.EditContactRequest
import uz.gita.mycontactbyretrofit.data.remote.request.LoginRequest
import uz.gita.mycontactbyretrofit.data.remote.request.RegisterRequest
import uz.gita.mycontactbyretrofit.data.remote.request.VerifySmsRequest
import uz.gita.mycontactbyretrofit.data.remote.response.ContactResponse
import uz.gita.mycontactbyretrofit.data.remote.response.LoginResponse
import uz.gita.mycontactbyretrofit.data.remote.response.RegisterResponse
import uz.gita.mycontactbyretrofit.data.remote.response.VerifySmsResponse

interface Api {
    // 1. Register
    @POST("api/v1/register")
    suspend fun registerUser(@Body data: RegisterRequest): Response<RegisterResponse>

    // 2. Verify SMS Code
    @POST("api/v1/register/verify")
    suspend fun verifySmsCode(@Body data: VerifySmsRequest): Response<VerifySmsResponse>

    // 3. Login
    @POST("api/v1/login")
    suspend fun loginUser(@Body data: LoginRequest): Response<LoginResponse>

    // 4. Add Contact
    @POST("api/v1/contact")
    fun addContact(@Header("token") token:String, @Body data: CreateContactRequest): Call<ContactResponse>

    // 5. Edit Contact
    @PUT("api/v1/contact")
    fun editContactByID(@Header("token") token:String, @Body data: EditContactRequest): Call<ContactResponse>

    // 6. Delete Contact
    @DELETE("api/v1/contact")
    fun deleteContactByID(@Header("token") token:String, @Query("id") id: Int): Call<Unit>

    // 7. Get All Contacts
    @GET("api/v1/contact")
    suspend fun getAllContacts(@Header("token") token:String): Response<List<ContactResponse>>
}



