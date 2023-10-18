package fr.antoineverin.worktime.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.antoineverin.worktime.database.dao.TimeSpentDao
import fr.antoineverin.worktime.database.entities.TimeSpent
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class AddEntryViewModel @Inject constructor(
    private val timeSpentDao: TimeSpentDao
): ViewModel() {

    private lateinit var timeSpent: TimeSpent
    private val dateField = mutableStateOf(LocalDate.now())
    private val fromField = mutableStateOf(TimeValue(false, "", ""))
    private val toField = mutableStateOf(TimeValue(false, "", ""))

    fun getFrom(): TimeValue {
        return fromField.value
    }

    fun onFromChange(time: TimeValue) {
        fromField.value = time
    }

    fun getTo(): TimeValue {
        return toField.value
    }

    fun onToChange(time: TimeValue) {
        try {
            time.minutes.toInt()
            time.hours.toInt()
            toField.value = time.copy(filled = true)
        }catch(error: NumberFormatException) {
            toField.value = time.copy(filled = false)
        }
    }

    fun fetchLastEntry() {
        val now = LocalTime.now()
        viewModelScope.launch {
            val lastTimeSpent = timeSpentDao.getLastTimeSpent()
            if((lastTimeSpent == null) || (lastTimeSpent.to != null)) {
                timeSpent = TimeSpent(0, YearMonth.now(), LocalDate.now(), LocalTime.now(), null)
                dateField.value = LocalDate.now()
                fromField.value = TimeValue(true, now.hour.toString(), now.minute.toString())
                toField.value = TimeValue(false, "", "")
            }else{
                timeSpent = lastTimeSpent
                dateField.value = lastTimeSpent.date
                fromField.value = TimeValue(true, lastTimeSpent.from.hour.toString(), lastTimeSpent.from.minute.toString())
                toField.value = TimeValue(true, now.hour.toString(), now.minute.toString())
            }
        }
    }

    fun updateEntry(popUp: () -> Unit) {
        viewModelScope.launch {
            timeSpent.date = dateField.value
            timeSpent.from = timeValueToLocalTime(fromField.value)!!
            timeSpent.to = timeValueToLocalTime(toField.value)
            timeSpent.period = YearMonth.of(timeSpent.date.year, timeSpent.date.monthValue)
            if(timeSpent.id == 0)
                timeSpentDao.insert(timeSpent)
            else
                timeSpentDao.update(timeSpent)
            popUp()
        }
    }

    private fun timeValueToLocalTime(value: TimeValue): LocalTime? {
        if(!value.filled) return null
        return LocalTime.of(value.hours.toInt(), value.minutes.toInt())
    }

}

data class TimeValue(
    var filled: Boolean,
    var hours: String,
    var minutes: String,
)
