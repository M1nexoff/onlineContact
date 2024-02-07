package uz.gita.mycontactbyretrofit

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.mycontactbyretrofit.app.App
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.domain.AppRepositoryImpl
import uz.gita.mycontactbyretrofit.presentation.ui.ContactScreen
import uz.gita.mycontactbyretrofit.presentation.ui.splash.SplashScreen
import uz.gita.mycontactbyretrofit.utils.MyEventBus
import uz.gita.mycontactbyretrofit.utils.NetworkStatusValidator
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: AppRepository

    @Inject
    lateinit var networkStatusValidator: NetworkStatusValidator
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                            }
                        }
                    )
                }
            },
            lostConnection = { Toast.makeText(this@MainActivity, "Not connection", Toast.LENGTH_SHORT).show() }
        )
        openScreenWithoutSave(SplashScreen())
    }

    private fun openScreenWithoutSave(fm: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fm)
            .commit()
    }
}


