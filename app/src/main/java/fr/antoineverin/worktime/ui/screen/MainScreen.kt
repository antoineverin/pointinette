package fr.antoineverin.worktime.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.antoineverin.worktime.LIST_ENTRIES
import fr.antoineverin.worktime.LIST_VACATION
import fr.antoineverin.worktime.ui.viewmodel.MainScreenViewModel
import java.time.Duration
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MainScreen(
    navigate: (String) -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CurrentPeriod(period = YearMonth.now())
        Spacer(modifier = Modifier.height(24.dp))
        TimeSpentSummary(
            hoursSpent = viewModel.getTimeSpentSummary(),
            hoursObjective = viewModel.getHoursObjective()
        )
        if (viewModel.getCurrentDayTimeSpent() != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Today you've worked:")
            Text(text = viewModel.getCurrentDayTimeSpent()!!)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { viewModel.addEntry(navigate) }) {
            Text(text = "Work!")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navigate(LIST_ENTRIES) }) {
            Text(text = "List entries")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navigate(LIST_VACATION) }) {
            Text(text = "List vacations")
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.fetchTimeSpentSummary()
        viewModel.fetchCurrentDayEntries()
    }
}

@Composable
private fun CurrentPeriod(period: YearMonth) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Current Period")
        Text(text = "" + period.month.getDisplayName(TextStyle.FULL, Locale.FRANCE) + " " + period.year)
    }
}

@Composable
private fun TimeSpentSummary(
    hoursSpent: Duration?,
    hoursObjective: Int
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if(hoursSpent == null)
            Text(text = "...")
        else
            Text(text = "" + hoursSpent.toHours() + "h " + hoursSpent.toMinutes() % 60 + "m")
        Text(text = "/")
        Text(text = "" + hoursObjective + "h")
    }
}
