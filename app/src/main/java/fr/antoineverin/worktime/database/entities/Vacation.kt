package fr.antoineverin.worktime.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.YearMonth

@Entity(tableName = "vacation")
data class Vacation(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var period: YearMonth,
    var days: Int,
    var comment: String?
)
