package fr.antoineverin.worktime.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.antoineverin.worktime.EDIT_ENTRY
import fr.antoineverin.worktime.database.dao.TimeSpentDao
import fr.antoineverin.worktime.database.dao.VacationDao
import fr.antoineverin.worktime.database.entities.TimeSpent
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
    private var lastEntry = mutableStateOf<TimeSpent?>(null)

    fun getTimeSpentSummary(): Duration? {
        return timeSpentSummary.value
    }

    fun getHoursObjective(): Int {
        return hoursObjective.intValue
    }

    fun getCurrentDayTimeSpent(): String? {
        if (currentDayTimeSpent.value == null || currentDayTimeSpent.value == Duration.ZERO)
            return null
        return LocalTime.ofSecondOfDay(currentDayTimeSpent.value!!.seconds)
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

    fun fetchTimeSpentSummary() {
        viewModelScope.launch {
            var time = Duration.ZERO
            timeSpentDao.getTimeSpentFromPeriod(YearMonth.now().toString()).forEach {
                if(it.to != null) {
                    var ld = Duration.ofSeconds(it.to!!.toSecondOfDay().toLong())
                    ld = ld.minusSeconds(it.from.toSecondOfDay().toLong())
                    time = time.plus(ld)
                }
            }
            
            // Calculating month's hours objectives
            timeSpentSummary.value = time
            var hours = 140
            vacationDao.getAllFromPeriod(YearMonth.now().toString()).forEach {
                hours -= 7 * it.days
            }
            hoursObjective.intValue = hours
        }
    }

    fun fetchCurrentDayEntries() {
        viewModelScope.launch {
            var duration = Duration.ZERO
            Log.d("time", LocalDate.now().toEpochDay().toString())
            timeSpentDao.getTimeSpentFromDay(LocalDate.now().toEpochDay()).forEach { entry ->
                Log.d("time", "founded")
                duration = duration.plus(entry.getDuration())
            }
            currentDayTimeSpent.value = duration
        }
    }

}
