package fr.antoineverin.worktime

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import fr.antoineverin.worktime.ui.screen.AddEntryScreen
import fr.antoineverin.worktime.ui.screen.MainScreen

fun NavGraphBuilder.wtAppRoute(appState: WtAppState) {
    composable(HOME_ROUTE) { MainScreen({ appState.navigate(it) }) }
    composable(ADD_ENTRY) { AddEntryScreen(popUp = { appState.popUp() }) }
}

const val HOME_ROUTE = "home"
const val ADD_ENTRY = "add_entry"
