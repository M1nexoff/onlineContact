package uz.gita.mycontactbyretrofit.data.remote.request

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val password: String
)