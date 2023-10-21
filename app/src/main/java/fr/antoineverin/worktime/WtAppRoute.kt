package fr.antoineverin.worktime

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import fr.antoineverin.worktime.ui.screen.EditEntryScreen
import fr.antoineverin.worktime.ui.screen.ListEntriesScreen
import fr.antoineverin.worktime.ui.screen.MainScreen

fun NavGraphBuilder.wtAppRoute(appState: WtAppState) {
    composable(HOME_ROUTE) { MainScreen({ appState.navigate(it) }) }
    composable(LIST_ENTRIES) { ListEntriesScreen({ appState.navigate(it) }) }
    composable(
        route = "$EDIT_ENTRY/{entryId}",
        arguments = listOf(navArgument("entryId") { type = NavType.IntType })
    ) { backEntryStack ->
        backEntryStack.arguments?.getInt("entryId")?.let { EditEntryScreen(it, { appState.popUp() }) }
    }
}

const val HOME_ROUTE = "home"
const val LIST_ENTRIES = "entry/list"
const val EDIT_ENTRY = "entry/edit"
