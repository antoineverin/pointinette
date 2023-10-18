package fr.antoineverin.worktime

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class WtAppState(
    val navHostController: NavHostController
) {

    fun navigate(route: String) {
        navHostController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navHostController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) {
                inclusive = true
            }
        }
    }

    fun popUp() {
        navHostController.popBackStack()
    }

}

@Composable
fun rememberAppState(
    navHostController: NavHostController = rememberNavController()
): WtAppState {
    return WtAppState(navHostController)
}
