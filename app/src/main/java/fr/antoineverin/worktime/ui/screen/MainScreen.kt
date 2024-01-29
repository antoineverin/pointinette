package fr.antoineverin.worktime.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Surfing
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.antoineverin.worktime.LIST_ENTRIES
import fr.antoineverin.worktime.LIST_VACATION
import fr.antoineverin.worktime.R
import fr.antoineverin.worktime.ui.viewmodel.MainScreenViewModel
import java.time.Duration
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.absoluteValue

@Composable
fun MainScreen(
    navigate: (String) -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel()
)
{
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(
            text = "42 Pointinette",
            fontSize = 50.sp,
            fontFamily = FontFamily(Font(R.font.roboto_black)),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .padding(top = 10.dp),
            style = androidx.compose.ui.text.TextStyle(
                textAlign = TextAlign.Center,
            )
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            CurrentPeriod(period = YearMonth.now())
            Spacer(modifier = Modifier.height(34.dp))
            TimeSpentSummary(
                hoursSpent = viewModel.getTimeDone(),
                hoursObjective = viewModel.getHoursObjective()
            )
            if (viewModel.getCurrentDayTimeSpent() != null) {
                Spacer(modifier = Modifier.height(34.dp))
                Text(
                    text = "Today you've worked :",
                    fontWeight = FontWeight.Bold,
                )
                Text(text = viewModel.getCurrentDayTimeSpent()!!)
            }
            Spacer(modifier = Modifier.height(34.dp))
            Text(
                text = "You have to do :",
                fontWeight = FontWeight.Bold,
            )
            val remainingHours = viewModel.getRemainingHoursPerDay()
            if (remainingHours != null)
                Text(
                    text = "${remainingHours.toHours()}h " +
                            "${remainingHours.toMinutes() % 60}m per days"
                )
            val remainingDifference = viewModel.getRemainingHoursDifference()
            if (remainingDifference != null)
                Text(
                    text = "${remainingDifference.toHours()}h " +
                            "${remainingDifference.toMinutes().absoluteValue % 60}m",
                    color = if (remainingDifference.isNegative) Color.Red else Color.Green,
                    fontSize = 13.sp
                )
        }
        Column(
            Modifier.fillMaxWidth().wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { viewModel.addEntry(navigate) },
                modifier = Modifier
                    .size(width = 350.dp, height = 100.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        modifier = Modifier.size(55.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Work!",
                        fontSize = 50.sp,
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Row {
                Button(onClick = { navigate(LIST_VACATION) }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Surfing, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "List Vacations",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { navigate(LIST_ENTRIES) }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.List, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "List entries",
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.fetchAll()
    }
}

@Composable
private fun CurrentPeriod(period: YearMonth) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Current Period :",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
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
        Text(
            text = "↓",
            fontSize = 25.sp,
        )
        Text(text = "" + hoursObjective + "h")
    }
}
