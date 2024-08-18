package rfm.biblequizz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import rfm.biblequizz.ui.home.HomeScreen
import rfm.biblequizz.ui.login.LoginScreen

@Composable
fun Navigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = LoginScreen
    ) {
        composable<LoginScreen> {
            LoginScreen(
                navigateToHome = {
                    navHostController.navigate(HomeScreenNav(
                        name = "test",
                        email = "working",
                    ))
                }
            )
        }
        composable<HomeScreenNav> {
            val args = it.toRoute<HomeScreenNav>()
            args.email?.let { it1 ->
                args.name?.let { it2 ->
                    HomeScreen(
                        name = it2,
                        email = it1
                    )
                }
            }


        }

    }
}

@Serializable
object LoginScreen
@Serializable
data class HomeScreenNav(
    val name: String? = "test",
    val email: String? = "",
    val password: String? = ""
)
