package rfm.hillsongpt.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import rfm.hillsongpt.ui.home.MainNavHost
import rfm.hillsongpt.ui.login.LoginScreen
import rfm.hillsongpt.ui.login.LoginViewModel
import rfm.hillsongpt.ui.login.loginGraph
import rfm.hillsongpt.ui.quizz.quizzGraph
import rfm.hillsongpt.ui.signup.SignUpScreen

@Composable
fun RootNavigation() {
    val rootNavController = rememberNavController()
    NavHost(
        navController = rootNavController,
        startDestination = LoginGraph
    ) {
        loginGraph(rootNavController)

        composable<MainNavHost> {
            MainNavHost()
        }

        composable<LoginScreenNav> {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()

            LoginScreen(
                navHostController = rootNavController,
                uiState = uiState,
                onEvent = loginViewModel::onEvent
                )
        }

        composable<SignUpScreenNav> {
            SignUpScreen(navHost = rootNavController)
        }
        quizzGraph(rootNavController)

    }
}

@Serializable
object LoginScreenNav
@Serializable
data object MainNavHost
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
