package rfm.biblequizz.ui.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import rfm.biblequizz.ui.HomeGraph

fun NavGraphBuilder.homeGraph(
    navController: NavHostController
){
    navigation<HomeGraph>(
        startDestination = HomeNav.HomeSreen
    ) {
        composable<HomeNav.HomeSreen> {

    }

}
}

sealed class HomeNav {
    @Serializable
    object HomeSreen : HomeNav()
}