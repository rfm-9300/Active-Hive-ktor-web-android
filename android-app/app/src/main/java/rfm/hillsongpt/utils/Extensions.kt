package rfm.hillsongpt.utils

import androidx.navigation.NavHostController


fun NavHostController.navigateWithOptions(route: Any) {
    this.navigate(route) {
        popUpTo(this@navigateWithOptions.graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}