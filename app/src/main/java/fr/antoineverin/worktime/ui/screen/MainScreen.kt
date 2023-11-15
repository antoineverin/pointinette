package fr.antoineverin.worktime.ui.screen

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.antoineverin.worktime.LIST_ENTRIES
import fr.antoineverin.worktime.ui.viewmodel.MainScreenViewModel
import java.time.Duration
import java.time.LocalTime
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
        if (viewModel.isAtWork()) {
            Spacer(modifier = Modifier.height(24.dp))
            CountUp(time = viewModel.getAtWorkSince()!!) { viewModel.getWorkDuration(it) }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { viewModel.addEntry(navigate) }) {
            Text(text = "Work!")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navigate(LIST_ENTRIES) }) {
            Text(text = "List entries")
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.fetchTimeSpentSummary()
        viewModel.fetchLastEntry()
    }
}

@Composable
private fun CountUp(time: LocalTime, getDuration: (LocalTime) -> String) {
    val text = minutesFrom(time = time, getDuration)

    Text(text = "You've been working for:")
    Text(text = text)
}

@Composable
private fun minutesFrom(time: LocalTime, getDuration: (LocalTime) -> String): String {
    var value by remember { mutableStateOf(getDuration(time)) }

    DisposableEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())

        val runnable = {
            value = getDuration(time)
        }

        handler.postDelayed(runnable, 60_000)

        onDispose {
            handler.removeCallbacks(runnable)
        }
    }
    
    return value
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
