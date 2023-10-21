package fr.antoineverin.worktime.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.antoineverin.worktime.EDIT_ENTRY
import fr.antoineverin.worktime.database.dao.TimeSpentDao
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val timeSpentDao: TimeSpentDao
): ViewModel() {

    private var timeSpentSummary = mutableStateOf<Duration?>(null)

    fun getTimeSpentSummary(): Duration? {
        return timeSpentSummary.value
    }

    fun getHoursObjective(): Int {
        return 150
    }

    fun addEntry(navigate: (String) -> Unit) {
        viewModelScope.launch {
            val entry = timeSpentDao.getLastTimeSpent()
            if (entry == null || entry.to != null)  navigate("$EDIT_ENTRY/0")
            else                                    navigate("$EDIT_ENTRY/${entry.id}")
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
        }
    }

}
