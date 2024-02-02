package uz.gita.mycontactbyretrofit.utils

import androidx.viewbinding.ViewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T : ViewBinding> T.myApply(block: T.() -> Unit) {
    block(this)
}



