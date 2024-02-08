package uz.gita.mycontactbyretrofit.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import hilt_aggregated_deps._dagger_hilt_android_internal_lifecycle_HiltWrapper_DefaultViewModelFactories_ActivityModule
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStatusValidator @Inject constructor(@ApplicationContext val context: Context) {

    var hasNetwork: Boolean = false
    fun init(availableNetworkBlock: () -> Unit, lostConnection: () -> Unit) {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)   // wifi
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)  // sim
            .build()

        val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                hasNetwork = true
                availableNetworkBlock.invoke()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                lostConnection.invoke()
                hasNetwork = false
                Timber.tag("TTT").d("onLost")
            }
        }

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, connectivityCallback)
    }
}