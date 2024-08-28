package rfm.biblequizz.ui.quizz


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rfm.biblequizz.data.log.Timber
import rfm.biblequizz.domain.model.Question
import rfm.biblequizz.ui.login.FullScreenLoading
import rfm.biblequizz.ui.login.LoginUiEvent
import rfm.biblequizz.ui.theme.*

val defaultPadding = 16.dp
val itemSpacing = 8.dp
val spacingTop = 40.dp

@Composable
fun QuizzScreen(
    uiState: QuizzUiState,
    onEvent: (QuizzUiEvent) -> Unit,
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    //var answers by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var showCorrectAnswer by remember { mutableStateOf(false) }

    val questions = uiState.questions

    // Remember the shuffled answers for the current question only if there are questions
    val answers = remember(currentQuestionIndex, questions) {
        if (questions.isNotEmpty()) {
            (questions[currentQuestionIndex].wrongAnswers + questions[currentQuestionIndex].correctAnswer).shuffled()
        } else {
            emptyList()
        }
    }


    if (uiState.isLoading){
        Timber.i("QuizzScreen: Loading")
        FullScreenLoading()
    }else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(defaultPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // center text
            Text(
                text = questions.getOrNull(currentQuestionIndex)?.question ?: "No question",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = spacingTop,
                        start = defaultPadding * 2,
                        end = defaultPadding * 2
                    )
            ){
                answers.forEach { answer ->
                    AnswerButton(
                        answerText = answer,
                        isSelected = selectedAnswer == answer,
                        isCorrect = answer == questions[currentQuestionIndex].correctAnswer && showCorrectAnswer,
                        onClick = {
                            selectedAnswer = answer

                        },

                        )
                }
            }

            Button(onClick = {
                if (selectedAnswer != null) {
                    showCorrectAnswer = true
                }
                currentQuestionIndex++
            }) {
                Text(text = "Next")

            }


        }
    }
}

@Composable
fun AnswerButton(
    answerText: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isCorrect -> MaterialTheme.colorScheme.primary
        isSelected -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.primary
    }

    val textColor = when {
        isCorrect -> Green90
        isSelected -> PurpleGrey40
        else -> Green20
    }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = answerText,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizzScreenPreview() {
    QuizzScreen(
        uiState = QuizzUiState(
            questions = listOf(
                Question(
                    question = "What is the capital of France?",
                    correctAnswer = "Paris",
                    wrongAnswers = listOf("London", "Berlin", "Madrid",),
                    uuid = "1"
                )
            )
        ),
        onEvent = {}
    )
}