package uz.gita.mycontactbyretrofit.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.mycontactbyretrofit.domain.AppRepository
import uz.gita.mycontactbyretrofit.domain.AppRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun getContactRepository(impl: AppRepositoryImpl) : AppRepository
}
