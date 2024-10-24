package rfm.hillsongpt.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import rfm.hillsongpt.ui.components.AppBottomBar
import rfm.hillsongpt.ui.components.AppTopBar
import rfm.hillsongpt.ui.home.connectgroups.ConnectGroupScreen
import rfm.hillsongpt.ui.home.serving.ServingScreen

@Composable
fun MainNavHost(){
    val mainNavController = rememberNavController()
    Scaffold (
        bottomBar = { AppBottomBar(
            navController = mainNavController,
            currentRoute = mainNavController.currentBackStackEntryAsState().value?.destination?.route
        ) },
        topBar = { AppTopBar() }
    ){ paddingValues ->
        NavHost(
            navController = mainNavController,
            startDestination = MainNav.HomeScreen()
        ) {
            composable<MainNav.HomeScreen> {

                val args = it.toRoute<MainNav.HomeScreen>()
                args.email?.let { it1 ->
                    args.name?.let { it2 ->
                        HomeScreen(
                            modifier = Modifier.padding(paddingValues),
                            name = it2,
                            email = it1,
                            navigateToQuizz = {  }
                        )
                    }
                }
            }
            composable<MainNav.ConnectGroupScreen> {
                ConnectGroupScreen(modifier = Modifier.padding(paddingValues))
            }
            composable<MainNav.ServiceScreen> {
                ServingScreen(modifier = Modifier.padding(paddingValues))
            }
        }
    }




}


sealed class MainNav {
    @Serializable
    data class HomeScreen(
        val name: String? = "test",
        val email: String? = "",
        val password: String? = ""
    ) : MainNav()
    @Serializable
    data object ConnectGroupScreen : MainNav()
    @Serializable
    data object ServiceScreen : MainNav()
}