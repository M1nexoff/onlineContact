package uz.gita.mycontactbyretrofit.data.remote.request

data class VerifySmsRequest(
    val phone: String,
    val code: String
)