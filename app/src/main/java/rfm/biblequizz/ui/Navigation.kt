package rfm.biblequizz.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import rfm.biblequizz.ui.home.HomeScreen
import rfm.biblequizz.ui.login.LoginScreen
import rfm.biblequizz.ui.signup.SignUpScreen

@Composable
fun Navigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = LoginScreen
    ) {
        composable<LoginScreen> {
            LoginScreen(navHostController = navHostController)
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
        composable<SignUpScreenNav> {
            SignUpScreen(navHost = navHostController)
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
@Serializable
object SignUpScreenNav
