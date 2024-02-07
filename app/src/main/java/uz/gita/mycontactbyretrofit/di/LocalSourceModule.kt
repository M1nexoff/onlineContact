package uz.gita.mycontactbyretrofit.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.mycontactbyretrofit.data.local.AppDatabase
import uz.gita.mycontactbyretrofit.data.local.dao.ContactDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalSourceModule {

    @[Provides Singleton]
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "MyContact.db")
            .allowMainThreadQueries()
            .build()


    @[Provides Singleton]
    fun provideContactDao(database: AppDatabase) : ContactDao = database.getContactDao()

    @[Provides Singleton]
    fun providePref(@ApplicationContext context: Context): SharedPreferences = context.getSharedPreferences("AppPref",Context.MODE_PRIVATE)
}