package fr.antoineverin.worktime.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Entity(tableName = "time_spent")
data class TimeSpent(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var period: YearMonth,
    var date: LocalDate,
    var from: LocalTime,
    var to: LocalTime?,
) {

    fun formatDuration(): String {
        return (
                from.format(DateTimeFormatter.ofPattern("HH:mm"))
                + " - "
                + (to?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "??:??")
                )
    }

    fun getDuration(): Duration? {
        if(to == null) return null
        return Duration.ofSeconds(to!!.toSecondOfDay().toLong())
            .minusSeconds(from.toSecondOfDay().toLong())
    }

}
