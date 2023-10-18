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
import fr.antoineverin.worktime.ADD_ENTRY
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
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navigate(ADD_ENTRY) }) {
            Text(text = "Work!")
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.fetchTimeSpentSummary()
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
            Text(text = "" + hoursSpent.toHours() + "h")
        Text(text = "/")
        Text(text = "" + hoursObjective + "h")
    }
}
