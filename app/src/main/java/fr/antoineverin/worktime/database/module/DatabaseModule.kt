package fr.antoineverin.worktime.database.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.antoineverin.worktime.database.WtDatabase
import fr.antoineverin.worktime.database.dao.TimeSpentDao
import fr.antoineverin.worktime.database.dao.VacationDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providesWtDatabase(@ApplicationContext appContext: Context): WtDatabase {
        return Room.databaseBuilder(appContext, WtDatabase::class.java, "wtdb").build()
    }

    @Provides
    fun providesTimeSpentDao(database: WtDatabase): TimeSpentDao {
        return database.timeSpentDao()
    }

    @Provides
    fun providesVacationDao(database: WtDatabase): VacationDao {
        return database.vacationDao()
    }

}
