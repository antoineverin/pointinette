package fr.antoineverin.worktime.utils

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.round

fun calculateHoursDifference(at: LocalDate, hoursDone: Duration, hoursObjective: Int, daysOff: Int): Duration
{
    val hoursPerDays = round(100f * hoursObjective / (getWorkDays(YearMonth.from(at)) - daysOff)) / 100
    val hoursShouldHave = hoursPerDays * getSpentWorkDays(at)
    return hoursDone.minusMinutes((hoursShouldHave * 60).toLong())
}

fun calculateHoursPerDays(at: LocalDate, hoursDone: Duration, hoursObjective: Int): Duration
{
    val remainingMinutes = Duration.ofHours(hoursObjective.toLong()).minus(hoursDone).toMinutes()
    val minutesPerDay = if (at.dayOfWeek == DayOfWeek.SATURDAY || at.dayOfWeek == DayOfWeek.SUNDAY)
        remainingMinutes.toFloat() / (getRemainingWorkDays(at))
    else
        remainingMinutes.toFloat() / (getRemainingWorkDays(at) + 1)
    return Duration.ofMinutes(minutesPerDay.toLong())
}

private fun getWorkDays(month: YearMonth): Int {
    var workdays = 0
    var i = 0

    while (month.isValidDay(++i))
    {
        val dayOfWeek = month.atDay(i).dayOfWeek
        if (dayOfWeek != DayOfWeek.SATURDAY
            && dayOfWeek != DayOfWeek.SUNDAY)
            workdays++
    }

    return workdays
}

private fun getSpentWorkDays(at: LocalDate): Int {
    val month = YearMonth.from(at)
    var workdays = 0
    var i = 0

    while (++i <= at.dayOfMonth)
    {
        val dayOfWeek = month.atDay(i).dayOfWeek
        if (dayOfWeek != DayOfWeek.SATURDAY
            && dayOfWeek != DayOfWeek.SUNDAY)
            workdays++
    }

    return workdays
}

private fun getRemainingWorkDays(at: LocalDate): Int {
    val month = YearMonth.from(at)
    var workdays = 0
    var i = at.dayOfMonth

    while (month.isValidDay(++i))
    {
        val dayOfWeek = month.atDay(i).dayOfWeek
        if (dayOfWeek != DayOfWeek.SATURDAY
            && dayOfWeek != DayOfWeek.SUNDAY)
            workdays++
    }

    return workdays
}
