package uz.gita.mycontactbyretrofit

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.contextaware.withContextAvailable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.processor.internal.definecomponent.codegen._dagger_hilt_android_internal_builders_ServiceComponentBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.navigation.AppNavigationHandler
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

    @Inject
    lateinit var navigationHandler: AppNavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController

        navigationHandler.buffer.onEach {
            it.invoke(navController)
        }.launchIn(lifecycleScope)

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



