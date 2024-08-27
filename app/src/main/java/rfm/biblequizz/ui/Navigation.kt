package rfm.biblequizz.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import rfm.biblequizz.ui.home.HomeScreen
import rfm.biblequizz.ui.home.HomeViewModel
import rfm.biblequizz.ui.login.LoginScreen
import rfm.biblequizz.ui.login.LoginViewModel
import rfm.biblequizz.ui.signup.SignUpScreen

@Composable
fun Navigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = LoginScreen
    ) {
        composable<LoginScreen> {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()

            LoginScreen(
                navHostController = navHostController,
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
                        getQuestion = homeViewModel::getQuestions
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
