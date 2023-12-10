package fr.antoineverin.worktime.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.antoineverin.worktime.database.dao.TimeSpentDao
import fr.antoineverin.worktime.database.entities.TimeSpent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListEntriesViewModel @Inject constructor(
    private val timeSpentDao: TimeSpentDao
): ViewModel() {

    val entries = mutableStateListOf<TimeSpent>()

    fun fetchEntries() {
        viewModelScope.launch {
            entries.addAll(timeSpentDao.getAllTimeSpent())
        }
    }

    fun deleteEntry(entry: TimeSpent) {
        viewModelScope.launch {
            timeSpentDao.delete(entry)
            entries.remove(entry)
        }
    }

}
