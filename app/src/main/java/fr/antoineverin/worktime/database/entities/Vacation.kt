package fr.antoineverin.worktime.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.YearMonth

@Entity(tableName = "vacation")
data class Vacation(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var period: YearMonth,
    var days: Int,
    @ColumnInfo(defaultValue = "0") var type: VacationType = VacationType.OTHER,
    var comment: String?,
) {

    enum class VacationType {
        OTHER,
        VACATION,
        SICKNESS,
    }

}
