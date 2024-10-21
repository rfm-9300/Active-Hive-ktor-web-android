package rfm.biblequizz.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import rfm.biblequizz.ui.home.HomeScreen
import rfm.biblequizz.ui.home.HomeViewModel
import rfm.biblequizz.ui.login.LoginScreen
import rfm.biblequizz.ui.login.LoginViewModel
import rfm.biblequizz.ui.login.loginGraph
import rfm.biblequizz.ui.quizz.quizzGraph
import rfm.biblequizz.ui.signup.SignUpScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = LoginGraph
    ) {
        loginGraph(navController)
        composable<LoginScreenNav> {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()

            LoginScreen(
                navHostController = navController,
                uiState = uiState,
                onEvent = loginViewModel::onEvent
                )
        }
        composable<HomeScreenNav> {
            val homeViewModel = hiltViewModel<HomeViewModel>()

            val args = it.toRoute<HomeScreenNav>()
            args.email?.let { it1 ->
                args.name?.let { it2 ->
                    HomeScreen(
                        name = it2,
                        email = it1,
                        navigateToQuizz = { navController.navigate(QuizzGraph) }
                    )
                }
            }
        }
        composable<SignUpScreenNav> {
            SignUpScreen(navHost = navController)
        }
        quizzGraph(navController)

    }
}

@Serializable
object LoginScreenNav
@Serializable
data class HomeScreenNav(
    val name: String? = "test",
    val email: String? = "",
    val password: String? = ""
)
@Serializable
object SignUpScreenNav
@Serializable
object QuizzGraph
@Serializable
object LoginGraph
@Serializable
object HomeGraph

sealed class QuizzNav {
    @Serializable
    object QuizzHome : QuizzNav()
    @Serializable
    object QuizzQuestion : QuizzNav()
    @Serializable
    object QuizzResult : QuizzNav()
}
