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
import java.time.YearMonth

@Composable
fun YearMonthField(
    value: YearMonthValue,
    onValueChange: (YearMonthValue) -> Unit,
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
            label = "Month",
            value = value.month,
            onValueChange = { onValueChange(value.copy(month = it)) },
            checkValue = { checkDigitAndRange(it, 0..12) },
            focusManager = focusManager,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(5.dp))
        NumberField(
            label = "Year",
            value = value.year,
            onValueChange = { onValueChange(value.copy(year = it)) },
            checkValue = { it.isDigitsOnly() },
            focusManager = focusManager,
            modifier = Modifier.weight(1f),
            imeAction = imeAction,
            action = action
        )
    }
}

data class YearMonthValue(
    val month: String,
    val year: String
) {

    fun isValid(): Boolean {
        if (!(month.isDigitsOnly() && year.isDigitsOnly()))
            return false
        if (isEmpty())
            return false
        return try {
            YearMonth.of(year.toInt(), month.toInt())
            true
        }catch (e: Exception) {
            false
        }
    }

    fun isEmpty(): Boolean {
        return month == "" || year == ""
    }

    fun toYearMonth(): YearMonth {
        return YearMonth.of(year.toInt(), month.toInt())
    }

}
