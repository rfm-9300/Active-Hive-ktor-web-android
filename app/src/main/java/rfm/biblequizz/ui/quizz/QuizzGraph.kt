package rfm.biblequizz.ui.quizz

import androidx.compose.runtime.getValue
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
            val quizzViewModel = hiltViewModel<QuizzViewModel>()
            val uiState by quizzViewModel.uiState.collectAsStateWithLifecycle()

            QuizzQuestionScreen(
                uiState = uiState,
                onEvent = quizzViewModel::onEvent,
                onNextQuestion = quizzViewModel::onNextQuestion,
            )
        }
        composable<QuizzNav.QuizzResult> {

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