package fr.antoineverin.worktime.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import fr.antoineverin.worktime.ui.field.DateField
import fr.antoineverin.worktime.ui.field.TimeField
import fr.antoineverin.worktime.ui.viewmodel.EditEntryViewModel

@Composable
fun EditEntryScreen(
    entryId: Int,
    popUp: () -> Unit,
    viewModel: EditEntryViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth().padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Label(name = "Date") {
            DateField(
                value = viewModel.date.value,
                onValueChange = { viewModel.date.value = it; viewModel.checkInputValidity() },
                focusManager = focusManager,
                imeAction = ImeAction.Done
            )
        }
        Spacer(Modifier.height(13.dp))
        Label(name = "From") {
            TimeField(
                value = viewModel.from.value,
                onValueChange = { viewModel.from.value = it; viewModel.checkInputValidity() },
                focusManager = focusManager,
                imeAction = ImeAction.Done
            )
        }
        Spacer(Modifier.height(13.dp))
        Label(name = "To") {
            TimeField(
                value = viewModel.to.value,
                onValueChange = { viewModel.to.value = it; viewModel.checkInputValidity() },
                focusManager = focusManager,
                imeAction = ImeAction.Done,
                action = { viewModel.pushEntry(popUp) }
            )
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = { viewModel.pushEntry(popUp) }, enabled = viewModel.isValid.value) {
            Text(text = "Valid")
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.fetchEntry(entryId)
    }
}

@Composable
private fun Label(name: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(modifier = modifier) {
        Text(text = name)
        Spacer(Modifier.height(5.dp))
        content()
    }
}

fun checkDigitAndRange(value: String, range: IntRange): Boolean {
    return value.isDigitsOnly() && (value == "" || value.toInt() in range)
}
