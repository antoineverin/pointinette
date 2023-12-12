package fr.antoineverin.worktime.ui.viewmodel.vacation

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.antoineverin.worktime.database.dao.VacationDao
import fr.antoineverin.worktime.database.entities.Vacation
import fr.antoineverin.worktime.ui.field.YearMonthValue
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class EditVacationViewModel @Inject constructor(
    private val vacationDao: VacationDao
): ViewModel() {

    private lateinit var vacation: Vacation
    val period = mutableStateOf(YearMonthValue("", ""))
    val days = mutableIntStateOf(0)
    val isValid = mutableStateOf(false)

    fun fetchEntry(id: Int) {
        if (id == 0) {
            setupFields(Vacation(0, YearMonth.now(), 0))
            return
        }

        viewModelScope.launch {
            setupFields(vacationDao.getVacation(id))
        }
    }

    fun checkFieldsValidity() {
        if (period.value.isValid())
            isValid.value = false
        else
            isValid.value = period.value.isValid() && days.intValue >= 0
    }

    fun pushEntry(popUp: () -> Unit) {
        vacation.period = period.value.toYearMonth()
        vacation.days = days.intValue

        viewModelScope.launch {
            if (vacation.id == 0)
                vacationDao.insert(vacation)
            else
                vacationDao.update(vacation)
            popUp()
        }
    }

    private fun setupFields(vacation: Vacation) {
        this.vacation = vacation
        period.value = YearMonthValue(
            vacation.period.month.toString(),
            vacation.period.year.toString())
        days.intValue = vacation.days
    }

}