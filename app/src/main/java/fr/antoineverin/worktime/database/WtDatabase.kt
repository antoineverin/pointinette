package fr.antoineverin.worktime.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.antoineverin.worktime.database.converter.DateConverter
import fr.antoineverin.worktime.database.dao.TimeSpentDao
import fr.antoineverin.worktime.database.dao.VacationDao
import fr.antoineverin.worktime.database.entities.TimeSpent
import fr.antoineverin.worktime.database.entities.Vacation

@Database(version = 2, exportSchema = true, entities = [
        TimeSpent::class,
        Vacation::class
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(DateConverter::class)
abstract class WtDatabase: RoomDatabase() {

    abstract fun timeSpentDao(): TimeSpentDao

    abstract fun vacationDao(): VacationDao

}
