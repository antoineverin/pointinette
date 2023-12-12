package fr.antoineverin.worktime.ui.screen.vacation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.antoineverin.worktime.EDIT_VACATION
import fr.antoineverin.worktime.database.entities.Vacation
import fr.antoineverin.worktime.ui.theme.WorktimeTheme
import fr.antoineverin.worktime.ui.viewmodel.vacation.ListVacationViewModel
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListVacationScreen(
    navigate: (String) -> Unit,
    viewModel: ListVacationViewModel = hiltViewModel()
) {
    Scaffold(
        floatingActionButton = {
            IconButton(onClick = { navigate("$EDIT_VACATION/0") }) {
                Icons.Filled.Add
            }
        }
    ) { paddingValues ->
        LazyColumn(Modifier.height(paddingValues.calculateTopPadding())) {
            items(
                items = viewModel.vacations,
                key = { vacation -> vacation.id }
            ) { it ->
                VacationItem(
                    vacation = it,
                    delete = { viewModel.deleteVacation(it) },
                    edit = { navigate("${EDIT_VACATION}/${it.id}") }
                )
            }
        }
    }


    LaunchedEffect(viewModel) {
        viewModel.vacations.clear()
        viewModel.fetchEntries()
    }
}

@Composable
fun VacationItem(
    vacation: Vacation,
    delete: (Vacation) -> Unit,
    edit: (Vacation) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = vacation.period.format(DateTimeFormatter.ofPattern("MMMM yyyy")))
            Text(text = "" + vacation.days + " days")
        }
        Row {
            IconButton(onClick = { edit(vacation) }) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = { delete(vacation) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VacationItemPreview() {
    WorktimeTheme {
        VacationItem(Vacation(0, YearMonth.now(), 5), { }, { })
    }
}
