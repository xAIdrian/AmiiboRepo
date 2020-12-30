package com.amohnacs.amiiborepo.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amohnacs.amiiborepo.model.Amiibo

@Database(entities = [Amiibo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun amiiboDao(): AmiiboDao
}