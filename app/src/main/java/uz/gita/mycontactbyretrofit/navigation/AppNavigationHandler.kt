package uz.gita.mycontactbyretrofit.navigation

import kotlinx.coroutines.flow.Flow
import uz.gita.mycontactbyretrofit.navigation.AppNavigation

interface AppNavigationHandler {
    val buffer : Flow<AppNavigation>
}