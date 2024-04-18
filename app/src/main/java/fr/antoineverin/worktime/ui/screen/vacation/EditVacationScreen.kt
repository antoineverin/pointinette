package fr.antoineverin.worktime.ui.screen.vacation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.antoineverin.worktime.database.entities.Vacation
import fr.antoineverin.worktime.ui.field.Label
import fr.antoineverin.worktime.ui.field.NumberField
import fr.antoineverin.worktime.ui.field.YearMonthField
import fr.antoineverin.worktime.ui.viewmodel.vacation.EditVacationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVacationScreen(
    vacationId: Int,
    popUp: () -> Unit,
    viewModel: EditVacationViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    var expend by remember { mutableStateOf(false) }

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
        Label(name = "Jours ouvrés") {
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
        Spacer(Modifier.height(13.dp))
        Label(name = "Comment") {
            TextField(
                value = viewModel.comment.value,
                onValueChange = { viewModel.comment.value = it; viewModel.checkFieldsValidity() },
                label = { Text("Enter comment") }
            )
        }
        Spacer(Modifier.height(13.dp))
        Label(name = "Type") {
            TextField(
                value = Vacation.VacationType.values()[viewModel.type.intValue].name,
                onValueChange = { },
                enabled = false,
                modifier =  Modifier.clickable { expend = true },
                textStyle = TextStyle(color = Color.White)
            )
            VacationTypeDropdown(expend = expend, onDismiss = { expend = false }, onClick = { type ->
                viewModel.type.intValue = type.ordinal
                expend = false
            })
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

@Composable
fun VacationTypeDropdown(
    expend: Boolean,
    onDismiss: () -> Unit,
    onClick: (Vacation.VacationType) -> Unit,
)
{
    DropdownMenu(expanded = expend, onDismissRequest = onDismiss) {
        Vacation.VacationType.values().forEach { type ->
            DropdownMenuItem(text = { Text(type.name) }, onClick = { onClick(type) })
        }
    }
}
