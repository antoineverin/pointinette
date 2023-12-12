package fr.antoineverin.worktime.ui.viewmodel.vacation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.antoineverin.worktime.database.dao.VacationDao
import fr.antoineverin.worktime.database.entities.Vacation
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListVacationViewModel @Inject constructor(
    private val vacationDao: VacationDao
): ViewModel() {

    val vacations = mutableStateListOf<Vacation>()

    fun fetchEntries() {
        viewModelScope.launch {
            vacations.addAll(vacationDao.getAllVacation())
        }
    }

    fun deleteVacation(vacation: Vacation) {
        viewModelScope.launch {
            vacations.remove(vacation)
            vacationDao.delete(vacation)
        }
    }

}