package fr.antoineverin.worktime.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

@Entity(tableName = "time_spent")
data class TimeSpent(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var period: YearMonth,
    var date: LocalDate,
    var from: LocalTime,
    var to: LocalTime?,
)
