package fr.antoineverin.worktime.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.antoineverin.worktime.database.entities.TimeSpent
import fr.antoineverin.worktime.ui.theme.WorktimeTheme
import fr.antoineverin.worktime.ui.viewmodel.ListEntriesViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun ListEntriesScreen(
    navigate: (String) -> Unit,
    viewModel: ListEntriesViewModel = hiltViewModel()
) {
    LazyColumn {
        items(
            items = viewModel.entries,
            key = { entry -> entry.id }
        ) { entry ->
            EntryItem(
                entry = entry,
                delete = { viewModel.deleteEntry(it) },
                edit = { navigate("entry/edit/${it.id}") }
            )
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.entries.clear()
        viewModel.fetchEntries()
    }
}

@Composable
private fun EntryItem(
    entry: TimeSpent,
    delete: (TimeSpent) -> Unit,
    edit: (TimeSpent) -> Unit
) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(
            Modifier
                .fillMaxWidth(.75f)
                .padding(5.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = entry.date.format(DateTimeFormatter.ofPattern("EEEE dd")))
                Text(text = entry.formatDuration())
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = entry.period.format(DateTimeFormatter.ofPattern("MMMM yyyy")))
                Text(text = entry.getDuration().toHours().toString() + "h " +
                        entry.getDuration().toMinutes() % 60 + "m")
            }
        }
        IconButton(onClick = { edit(entry) }) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit")
        }
        IconButton(onClick = { delete(entry) }) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EntryItemPreview() {
    WorktimeTheme {
        Column {
            EntryItem(entry = TimeSpent(
                0,
                YearMonth.now(),
                LocalDate.now(),
                LocalTime.of(8, 30),
                LocalTime.of(17, 45)
            ), { }, { })
            Spacer(modifier = Modifier.height(5.dp))
            EntryItem(entry = TimeSpent(
                0,
                YearMonth.now(),
                LocalDate.now(),
                LocalTime.of(8, 30),
                null
            ), { }, { })
        }
    }
}
