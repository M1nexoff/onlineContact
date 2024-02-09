package uz.gita.mycontactbyretrofit.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.mycontactbyretrofit.navigation.AppNavigationDispatcher
import uz.gita.mycontactbyretrofit.navigation.AppNavigationHandler
import uz.gita.mycontactbyretrofit.navigation.AppNavigator

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun bindAppNavigation(impl : AppNavigationDispatcher) : AppNavigator

    @Binds
    fun bindAppNavigationHandler(impl : AppNavigationDispatcher) : AppNavigationHandler
}