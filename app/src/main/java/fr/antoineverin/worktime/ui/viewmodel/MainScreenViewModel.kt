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
import kotlinx.coroutines.launch
import java.time.Duration
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
    private var hoursObjective = mutableIntStateOf(140)
    private var lastEntry = mutableStateOf<TimeSpent?>(null)

    fun getTimeSpentSummary(): Duration? {
        return timeSpentSummary.value
    }

    fun getHoursObjective(): Int {
        return hoursObjective.intValue
    }

    fun getAtWorkSince(): LocalTime? {
        return lastEntry.value?.from
    }

    fun isAtWork(): Boolean {
        return lastEntry.value != null && lastEntry.value!!.to == null
    }

    fun getWorkDuration(since: LocalTime): String {
        return LocalTime.ofSecondOfDay(
            Duration.ofSeconds(LocalTime.now().toSecondOfDay().toLong())
                .minusSeconds(since.toSecondOfDay().toLong())
                .seconds
        ).format(DateTimeFormatter.ofPattern("HH'h' mm'm'"))
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
            timeSpentSummary.value = time
            var hours = 140
            vacationDao.getAllFromPeriod(YearMonth.now().toString()).forEach {
                hours -= 7 * it.days
            }
            hoursObjective.intValue = hours
        }
    }

    fun fetchLastEntry() {
        viewModelScope.launch {
            lastEntry.value = timeSpentDao.getLastTimeSpent()
        }
    }

}
