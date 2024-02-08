package uz.gita.mycontactbyretrofit

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.utils.MyEventBus
import uz.gita.mycontactbyretrofit.utils.NetworkStatusValidator
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    @Inject
    lateinit var repository: AppRepository

    @Inject
    lateinit var networkStatusValidator: NetworkStatusValidator
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkStatusValidator.init(
            availableNetworkBlock = {
                executor.execute {
                    repository.syncWithServer(
                        finishBlock = {
                            this@MainActivity.runOnUiThread {
                                MyEventBus.reloadEvent?.invoke()
                            }
                        },
                        errorBlock = {
                            this@MainActivity.runOnUiThread {
                                Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                                MyEventBus.reloadEvent?.invoke()
                            }
                        }
                    )
                }
            },
            lostConnection = { Toast.makeText(this@MainActivity, "Not connection", Toast.LENGTH_SHORT).show() }
        )
    }

}



