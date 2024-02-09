package uz.gita.mycontactbyretrofit.navigation

import androidx.navigation.NavDirections

interface AppNavigator {
    suspend fun navigateTo(direction: NavDirections)
    suspend fun back()
}