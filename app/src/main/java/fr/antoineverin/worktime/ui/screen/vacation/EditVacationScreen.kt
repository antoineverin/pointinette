package fr.antoineverin.worktime.ui.screen.vacation

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
import androidx.hilt.navigation.compose.hiltViewModel
import fr.antoineverin.worktime.ui.field.Label
import fr.antoineverin.worktime.ui.field.NumberField
import fr.antoineverin.worktime.ui.field.YearMonthField
import fr.antoineverin.worktime.ui.viewmodel.vacation.EditVacationViewModel

@Composable
fun EditVacationScreen(
    vacationId: Int,
    popUp: () -> Unit,
    viewModel: EditVacationViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Label(name = "Period") {
            YearMonthField(
                value = viewModel.period.value,
                onValueChange = { viewModel.period.value = it; viewModel.checkFieldsValidity() },
                focusManager = focusManager
            )
        }
        Spacer(Modifier.height(13.dp))
        Label(name = "Jours") {
            NumberField(
                label = "Jours",
                value = viewModel.days.value,
                onValueChange = {
                    viewModel.days.value = it; viewModel.checkFieldsValidity()
                },
                checkValue = { true },
                focusManager = focusManager,
                imeAction = ImeAction.Next,
                action = { viewModel.pushEntry(popUp) }
            )
        }
        Spacer(Modifier.height(24.dp))
        Button(onClick = { viewModel.pushEntry(popUp) }, enabled = viewModel.isValid.value) {
            Text(text = "Valid")
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.fetchEntry(vacationId)
    }
}
