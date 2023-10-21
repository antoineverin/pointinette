package fr.antoineverin.worktime.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.antoineverin.worktime.database.dao.TimeSpentDao
import fr.antoineverin.worktime.database.entities.TimeSpent
import fr.antoineverin.worktime.ui.field.DateFieldValue
import fr.antoineverin.worktime.ui.field.TimeFieldValue
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class EditEntryViewModel @Inject constructor(
    val timeSpentDao: TimeSpentDao
): ViewModel() {

    private lateinit var entry: TimeSpent
    val date = mutableStateOf(DateFieldValue("", "", ""))
    val from = mutableStateOf(TimeFieldValue("", ""))
    val to = mutableStateOf(TimeFieldValue("", ""))
    val isValid = mutableStateOf(false)

    fun fetchEntry(id: Int) {
        if (id == 0) {
            setupWithEntry(TimeSpent(
                0,
                YearMonth.now(),
                LocalDate.now(),
                LocalTime.now(),
                null
            ))
            return
        }

        viewModelScope.launch {
            setupWithEntry(timeSpentDao.getTimeSpent(id))
        }
    }

    fun pushEntry(popUp: () -> Unit) {
        viewModelScope.launch {
            entry.date = date.value.toLocalDate()
            entry.period = YearMonth.of(entry.date.year, entry.date.monthValue)
            entry.from = from.value.toLocalTime()
            if (to.value.isEmpty()) entry.to = null
            else                    entry.to = to.value.toLocalTime()

            if (entry.id == 0)
                timeSpentDao.insert(entry)
            else
                timeSpentDao.update(entry)
            popUp()
        }
    }

    fun checkInputValidity() {
        if (date.value.isEmpty() || from.value.isEmpty())
            isValid.value = false
        isValid.value = date.value.isValid() && from.value.isValid() && (to.value.isEmpty() || to.value.isValid())
    }

    private fun setupWithEntry(entry: TimeSpent) {
        date.value = localDateToFieldValue(entry.date)
        from.value = localTimeToFieldValue(entry.from)

        if (entry.to == null) {
            if (entry.id == 0)  to.value = TimeFieldValue("", "")
            else                to.value = localTimeToFieldValue(LocalTime.now())
        } else
            to.value = localTimeToFieldValue(entry.to!!)
        this.entry = entry
        checkInputValidity()
    }

    private fun localDateToFieldValue(value: LocalDate): DateFieldValue {
        return DateFieldValue(
            value.dayOfMonth.toString(),
            value.monthValue.toString(),
            value.year.toString()
        )
    }

    private fun localTimeToFieldValue(value: LocalTime): TimeFieldValue {
        return TimeFieldValue(
            value.minute.toString(),
            value.hour.toString()
        )
    }
}
