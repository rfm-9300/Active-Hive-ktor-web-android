package rfm.hillsongpt.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import rfm.hillsongpt.ui.components.AppBottomBar
import rfm.hillsongpt.ui.components.AppTopBar

@Composable
fun MainNavHost(){
    val homeNavController = rememberNavController()
    Scaffold (
        bottomBar = { AppBottomBar() },
        topBar = { AppTopBar() }
    ){ paddingValues ->
        NavHost(
            navController = homeNavController,
            startDestination = MainNav.HomeScreen()
        ) {
            composable<MainNav.HomeScreen> {
                val homeViewModel = hiltViewModel<HomeViewModel>()

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