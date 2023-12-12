package fr.antoineverin.worktime.database.dao

import androidx.room.Dao
import androidx.room.Query
import fr.antoineverin.worktime.database.entities.Vacation

@Dao
interface VacationDao: WtDao<Vacation> {

    @Query("SELECT * FROM vacation")
    suspend fun getAllVacation(): List<Vacation>

    @Query("SELECT * FROM vacation WHERE period = :period")
    suspend fun getAllFromPeriod(period: String): List<Vacation>

    @Query("SELECT * FROM vacation WHERE id = :id")
    suspend fun getVacation(id: Int): Vacation

}