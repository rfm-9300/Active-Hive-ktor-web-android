package rfm.biblequizz.ui.quizz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import rfm.biblequizz.data.log.Timber
import rfm.biblequizz.ui.login.FullScreenLoading

@Composable
fun QuizzResultScreen(
    uiState: QuizzUiState,
    onEvent: (QuizzUiEvent) -> Unit,
) {
    
    if (uiState.isLoading){
        Timber.i("QuizzResultScreen: Loading")
        FullScreenLoading()
    }else {
        QuizzResultContent(
            uiState = uiState,
            onEvent = onEvent
        )
    }
    
}

@Composable
fun QuizzResultContent(uiState: QuizzUiState, onEvent: (QuizzUiEvent) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding)
    ) {
        Text(
            text = "Quizz Result",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = itemSpacing)
        )

        Text(
            text = "Correct Answers: ${uiState.totalCorrectAnswers}",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = itemSpacing)
        )

        Text(
            text = "Wrong Answers: ${uiState.totalWrongAnswers}",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = itemSpacing)
        )

        Button(
            onClick = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = itemSpacing)
        ) {
            Text(text = "Restart Quizz")
        }

        Button(
            onClick = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = itemSpacing)
        ) {
            Text(text = "Go to Home")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizzResultScreenPreview() {
    QuizzResultScreen(
        uiState = QuizzUiState(
            isLoading = false,
            isFinished = true,
            totalCorrectAnswers = 5,
            totalWrongAnswers = 5,
            currentQuestionIndex = 10,
            totalQuestions = 10,
            questionTitle = "",
            showCorrectAnswer = false,
            selectedAnswer = "",
            answers = emptyList()
        ),
        onEvent = {}
    )
}
