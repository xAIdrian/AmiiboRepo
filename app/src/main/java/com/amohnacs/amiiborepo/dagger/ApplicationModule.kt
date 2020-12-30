package com.amohnacs.amiiborepo.dagger

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.amohnacs.amiiborepo.domain.AmiiboService
import com.amohnacs.amiiborepo.domain.AmiiboServiceFactory
import com.amohnacs.amiiborepo.local.AmiiboDao
import com.amohnacs.amiiborepo.local.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application = application

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideAmiiboRetrofitClient() = AmiiboServiceFactory.createAmiiboService(AmiiboService::class.java)

    @Provides
    @Singleton
    fun provideRoomInstance(context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "AppDatabase"
    ).build()

    @Provides
    @Singleton
    fun provideAmiiboDao(appDatabase: AppDatabase): AmiiboDao = appDatabase.amiiboDao()
}