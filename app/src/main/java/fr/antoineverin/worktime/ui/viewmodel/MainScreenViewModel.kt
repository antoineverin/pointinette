package fr.antoineverin.worktime.ui.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.antoineverin.worktime.EDIT_ENTRY
import fr.antoineverin.worktime.database.dao.TimeSpentDao
import fr.antoineverin.worktime.database.dao.VacationDao
import fr.antoineverin.worktime.database.entities.TimeSpent
import fr.antoineverin.worktime.utils.calculateHoursDifference
import fr.antoineverin.worktime.utils.calculateHoursPerDays
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val timeSpentDao: TimeSpentDao,
    private val vacationDao: VacationDao
): ViewModel() {

    private var timeSpentSummary = mutableStateOf<Duration?>(null)
    private var currentDayTimeSpent = mutableStateOf<Duration?>(null)
    private var hoursObjective = mutableIntStateOf(140)
    private var daysOff = mutableIntStateOf(0)
    private var lastEntry = mutableStateOf<TimeSpent?>(null)

    fun getTimeDone(): Duration? {
        if (timeSpentSummary.value != null && currentDayTimeSpent.value != null)
            return (currentDayTimeSpent.value!!.plus(currentDayTimeSpent.value))
        return currentDayTimeSpent.value
    }

    fun getHoursObjective(): Int {
        return hoursObjective.intValue - daysOff.intValue * 7
    }

    fun getRemainingHoursPerDay(): Duration? {
        if (getTimeDone() == null) return null
        return calculateHoursPerDays(LocalDate.now(), timeSpentSummary.value!!, getHoursObjective())
    }

    fun getRemainingHoursDifference(): Duration? {
        if (getTimeDone() == null)  return null
        return calculateHoursDifference(LocalDate.now(), timeSpentSummary.value!!, getHoursObjective(), daysOff.intValue)
    }

    fun getCurrentDayTimeSpent(): String? {
        if (currentDayTimeSpent.value == null || currentDayTimeSpent.value == Duration.ZERO)
            return null
        return LocalTime.ofSecondOfDay(currentDayTimeSpent.value!!.seconds % 86399)
            .format(DateTimeFormatter.ofPattern("HH'h' mm'm'"))
    }

    fun addEntry(navigate: (String) -> Unit) {
        viewModelScope.launch {
            if (lastEntry.value == null || lastEntry.value!!.to != null)
                navigate("$EDIT_ENTRY/0")
            else
                navigate("$EDIT_ENTRY/${lastEntry.value!!.id}")
        }
    }

    fun fetchAll()
    {
        fetchTimeSpentSummary()
        fetchCurrentDayEntries()
        fetchCurrentMonthDaysOff()
    }

    private fun fetchTimeSpentSummary() {
        viewModelScope.launch {
            var time = Duration.ZERO
            timeSpentDao.getTimeSpentFromPeriod(YearMonth.now().toString()).forEach {
                if(it.to != null && it.date != LocalDate.now()) {
                    var ld = Duration.ofSeconds(it.to!!.toSecondOfDay().toLong())
                    ld = ld.minusSeconds(it.from.toSecondOfDay().toLong())
                    time = time.plus(ld)
                }
            }
            timeSpentSummary.value = time
        }
    }

    private fun fetchCurrentMonthDaysOff() {
        viewModelScope.launch {
            var days = 0
            vacationDao.getAllFromPeriod(YearMonth.now().toString()).forEach {
                days += it.days
            }
            daysOff.intValue = days
        }
    }

    private fun fetchCurrentDayEntries() {
        viewModelScope.launch {
            var duration = Duration.ZERO
            val entries = timeSpentDao.getTimeSpentFromDay(LocalDate.now().toEpochDay())
            entries.forEach { entry ->
                duration = duration.plus(entry.getDuration())
            }
            currentDayTimeSpent.value = duration
            lastEntry.value = if (entries.isEmpty()) null else entries.last()
        }
    }

}
