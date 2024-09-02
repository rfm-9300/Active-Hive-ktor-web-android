package rfm.biblequizz.ui.quizz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import rfm.biblequizz.ui.QuizzGraph


fun NavGraphBuilder.quizzGraph(
    navController: NavHostController
){
    navigation<QuizzGraph>(startDestination = QuizzNav.QuizzQuestion) {
        composable<QuizzNav.QuizzHome> {

        }
        composable<QuizzNav.QuizzQuestion> {
            val parentEntry = remember(it){
                navController.getBackStackEntry(QuizzNav.QuizzQuestion)
            }
            val quizzViewModel = hiltViewModel<QuizzViewModel>(parentEntry)
            val uiState by quizzViewModel.uiState.collectAsStateWithLifecycle()
            // when the quizz is finished, navigate to the result screen
            val onFinished = { navController.navigate(QuizzNav.QuizzResult) }

            QuizzQuestionScreen(
                uiState = uiState,
                onEvent = quizzViewModel::onEvent,
                onFinished = onFinished
            )
        }
        composable<QuizzNav.QuizzResult> {
            val parentEntry = remember(it){
                navController.getBackStackEntry(QuizzNav.QuizzQuestion)
            }

            val quizzViewModel = hiltViewModel<QuizzViewModel>(parentEntry)
            val uiState by quizzViewModel.uiState.collectAsStateWithLifecycle()

            QuizzResultScreen(
                uiState = uiState,
                onEvent = quizzViewModel::onEvent
            )
        }
    }
}


sealed class QuizzNav {
    @Serializable
    object QuizzHome : QuizzNav()
    @Serializable
    object QuizzQuestion : QuizzNav()
    @Serializable
    object QuizzResult : QuizzNav()
}