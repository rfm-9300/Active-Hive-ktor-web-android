package rfm.biblequizz.ui.home

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import rfm.biblequizz.ui.components.AppBottomBar

@Composable
fun MainNavHost(){
    val homeNavController = rememberNavController()
    Scaffold (
        bottomBar = { AppBottomBar() }
    ){ paddingValues ->
        val pad = paddingValues
        NavHost(
            navController = homeNavController,
            startDestination = MainNav.HomeScreen(
                name = "test",
                email = "test",
                password = "test"
            )
        ) {
            composable<MainNav.HomeScreen> {
                val homeViewModel = hiltViewModel<HomeViewModel>()

                val args = it.toRoute<MainNav.HomeScreen>()
                args.email?.let { it1 ->
                    args.name?.let { it2 ->
                        HomeScreen(
                            name = it2,
                            email = it1,
                            navigateToQuizz = {  }
                        )
                    }
                }
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
}