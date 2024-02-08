package uz.gita.mycontactbyretrofit.utils

import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

fun <T> T.myApply(block: T.() -> Unit) {
    block(this)
}

fun Fragment.showToast(message :String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun logger(message: String, tag: String= "TTT") {
    Timber.tag(tag).d(message)
}

fun EditText.myAddTextChangedListener(block: (String) -> Unit) {
    this.addTextChangedListener {editable ->
        editable?.let { block.invoke(it.toString()) }
    }
}

fun EditText.text() = this.text.toString()


open class Event<out T>(private val content: T) {

    private var hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}
