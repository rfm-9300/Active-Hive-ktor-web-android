package rfm.hillsongpt.ui.login


import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import rfm.hillsongpt.ui.LoginGraph

fun NavGraphBuilder.loginGraph(
    rootNavController: NavHostController
){
    navigation<LoginGraph>(startDestination = LoginNav.LandingScreen) {
        composable<LoginNav.LandingScreen> {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()

            LandingScreen(
                navHostController = rootNavController,
                uiState = uiState,
                onEvent = loginViewModel::onEvent
            )
        }
        composable<LoginNav.LoginScreen> {
            val loginViewModel = hiltViewModel<LoginViewModel>()
            val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()

            LoginScreen(
                navHostController = rootNavController,
                uiState = uiState,
                onEvent = loginViewModel::onEvent
            )
        }
    }
}

sealed class LoginNav {
    @Serializable
    object LoginScreen : LoginNav()
    @Serializable
    object LandingScreen : LoginNav()
}