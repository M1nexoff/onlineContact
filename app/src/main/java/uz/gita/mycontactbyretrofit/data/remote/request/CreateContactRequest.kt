package uz.gita.mycontactbyretrofit.data.remote.request

data class CreateContactRequest(
    val firstName: String,
    val lastName: String,
    val phone: String
)