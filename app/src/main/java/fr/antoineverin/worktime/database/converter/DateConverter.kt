package fr.antoineverin.worktime.database.converter

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth

class DateConverter {

    @TypeConverter
    fun fromLocalDate(value: LocalDate): Long {
        return value.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(value: Long): LocalDate {
        return LocalDate.ofEpochDay(value)
    }

    @TypeConverter
    fun fromLocalTime(value: LocalTime): Long {
        return value.toSecondOfDay().toLong()
    }

    @TypeConverter
    fun toLocalTime(value: Long): LocalTime {
        return LocalTime.ofSecondOfDay(value)
    }

    @TypeConverter
    fun fromYearMonth(value: YearMonth): String {
        return value.toString()
    }

    @TypeConverter
    fun toYearMonth(value: String): YearMonth {
        return YearMonth.parse(value)
    }

}
