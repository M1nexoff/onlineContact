package uz.gita.mycontactbyretrofit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.gita.mycontactbyretrofit.data.local.dao.ContactDao
import uz.gita.mycontactb7.data.source.local.entity.ContactEntity
import javax.inject.Singleton

@Database(entities = [ContactEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getContactDao(): ContactDao
}



