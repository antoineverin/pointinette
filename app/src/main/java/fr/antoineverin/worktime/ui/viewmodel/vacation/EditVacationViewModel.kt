package fr.antoineverin.worktime.ui.viewmodel.vacation

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
    val days = mutableStateOf("0")
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

    fun checkFieldsValidity(): Boolean {
        if (period.value.isEmpty() || days.value.isEmpty())
            isValid.value = false
        else
            isValid.value = period.value.isValid()
                    && days.value.toIntOrNull() != null
                    && days.value.toInt() > 0
        return isValid.value
    }

    fun pushEntry(popUp: () -> Unit) {
        if (!checkFieldsValidity())
            return

        vacation.period = period.value.toYearMonth()
        vacation.days = days.value.toInt()

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
            vacation.period.monthValue.toString(),
            vacation.period.year.toString())
        days.value = vacation.days.toString()
        checkFieldsValidity()
    }

}