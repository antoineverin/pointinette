package fr.antoineverin.worktime.ui.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import fr.antoineverin.worktime.ui.screen.checkDigitAndRange
import java.time.DateTimeException
import java.time.LocalTime

@Composable
fun TimeField(
    value: TimeFieldValue,
    onValueChange: (TimeFieldValue) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    action: () -> Unit = { focusManager.moveFocus(FocusDirection.Next) }
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NumberField(
            label = "Hour",
            value = value.hours,
            onValueChange = { onValueChange(value.copy(hours = it)) },
            checkValue = { checkDigitAndRange(it, 0..24) },
            focusManager = focusManager,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(5.dp))
        NumberField(
            label = "Minutes",
            value = value.minutes,
            onValueChange = { onValueChange(value.copy(minutes = it)) },
            checkValue = { checkDigitAndRange(it, 0..60) },
            focusManager = focusManager,
            modifier = Modifier.weight(1f),
            imeAction = imeAction,
            action = action
        )
    }
}

data class TimeFieldValue(
    val minutes: String,
    val hours: String
) {

    fun isValid(): Boolean {
        if (!(minutes.isDigitsOnly() && hours.isDigitsOnly()))
            return false
        if (isEmpty())
            return false
        return try {
            LocalTime.of(hours.toInt(), minutes.toInt())
            true
        }catch (e: DateTimeException) {
            false
        }
    }

    fun isEmpty(): Boolean {
        return minutes == "" || hours == ""
    }

    fun toLocalTime(): LocalTime {
        return LocalTime.of(hours.toInt(), minutes.toInt())
    }

}
