package uz.gita.mycontactbyretrofit.data.remote.response

data class ContactResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: String
)