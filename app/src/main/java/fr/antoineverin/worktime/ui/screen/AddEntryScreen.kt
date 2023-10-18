package fr.antoineverin.worktime.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.antoineverin.worktime.ui.viewmodel.AddEntryViewModel
import fr.antoineverin.worktime.ui.viewmodel.TimeValue

@Composable
fun AddEntryScreen(
    popUp: () -> Unit,
    viewModel: AddEntryViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.padding(5.dp)
    ) {
        TimeField(viewModel.getFrom(), { viewModel.onFromChange(it) })
        Spacer(modifier = Modifier.height(10.dp))
        TimeField(viewModel.getTo(), { viewModel.onToChange(it) })
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { viewModel.updateEntry(popUp) }) {
            Text(text = "Valid")
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.fetchLastEntry()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeField(
    time: TimeValue,
    onValueChange: (TimeValue) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier
    ) {
        TextField(
            label = { Text(text = "Hours") },
            value = time.hours,
            onValueChange = { onValueChange(time.copy(hours = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Next)
            }),
            modifier = Modifier.fillMaxWidth(.49f)
        )
        Spacer(modifier = Modifier.width(10.dp))
        TextField(
            label = { Text(text = "Min") },
            value = time.minutes,
            onValueChange = { onValueChange(time.copy(minutes = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
        )
    }
}

@Preview
@Composable
fun TimeFieldPreview() {
    TimeField(TimeValue(true, "13", "45"), { })
}
