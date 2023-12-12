package fr.antoineverin.worktime.database.dao

import androidx.room.Dao
import androidx.room.Query
import fr.antoineverin.worktime.database.entities.TimeSpent

@Dao
interface TimeSpentDao: WtDao<TimeSpent> {

    @Query("SELECT * FROM time_spent ORDER BY date DESC, `from` DESC")
    suspend fun getAllTimeSpent(): List<TimeSpent>

    @Query("SELECT * FROM time_spent WHERE id = :id")
    suspend fun getTimeSpent(id: Int): TimeSpent

    @Query("SELECT * FROM time_spent WHERE period = :period")
    suspend fun getTimeSpentFromPeriod(period: String): List<TimeSpent>

    @Query("SELECT * FROM time_spent WHERE id = (SELECT max(id) FROM time_spent)")
    suspend fun getLastTimeSpent(): TimeSpent?

}
