package rfm.biblequizz.ui.quizz



import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import rfm.biblequizz.data.log.Timber
import rfm.biblequizz.ui.login.FullScreenLoading

val defaultPadding = 16.dp
val itemSpacing = 8.dp
val spacingTop = 40.dp

@Composable
fun QuizzQuestionScreen(
    uiState: QuizzUiState,
    onEvent: (QuizzUiEvent) -> Unit,
    onFinished: () -> Unit
) {
    var selectedAnswer by remember { mutableStateOf<String?>(null) }

    // navigate to result screen when the quizz is finished
    LaunchedEffect(uiState) {
        if(uiState.isFinished){
            Timber.i("QuizzScreen: Finished")
            onFinished()
        }
    }


    // Display loading screen
    if (uiState.isLoading){
        Timber.i("QuizzScreen: Loading")
        FullScreenLoading()
    }else {
        // Display the quizz content
        QuizzContent(
            uiState = uiState,
            onAnswerSelected = { answer ->
                selectedAnswer = answer
            },
            onNextQuestion = {
                if (selectedAnswer != null) {
                    onEvent(QuizzUiEvent.NextButtonClick)
                }
            },
            selectedAnswer = selectedAnswer,
            showCorrectAnswer = uiState.showCorrectAnswer
        )
    }
}

@Composable
fun QuizzContent(
    uiState: QuizzUiState,
    onAnswerSelected: (String) -> Unit,
    onNextQuestion: () -> Unit,
    selectedAnswer: String?,
    showCorrectAnswer: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Spacer
        Spacer(modifier = Modifier.height(spacingTop))

        // Question title
        Text(
            text = uiState.questionTitle,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        // Spacer
        Spacer(modifier = Modifier.height(itemSpacing * 5))

        // Progress bar and question number
        QuizProgressBar(
            currentQuestionIndex = uiState.currentQuestionIndex,
            totalQuestions = uiState.totalQuestions
        )

        // Answers options buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = spacingTop,
                    start = defaultPadding * 2,
                    end = defaultPadding * 2
                )
        ) {
            uiState.answers.forEach { answer ->
                AnswerButton(
                    answerText = answer,
                    isSelected = selectedAnswer == answer,
                    isCorrect = answer == uiState.currentCorrectAnswer && showCorrectAnswer,
                    onClick = { onAnswerSelected(answer) }
                )
            }
        }

        // Spacer
        Spacer(modifier = Modifier.height(itemSpacing * 5))

        // Next button
        Button(onClick = onNextQuestion) {
            Text(
                text = "Next",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
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
        isCorrect -> MaterialTheme.colorScheme.onPrimary
        isSelected -> MaterialTheme.colorScheme.onSecondary
        else -> MaterialTheme.colorScheme.onPrimary
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

@Composable
fun QuizProgressBar(
    currentQuestionIndex: Int,
    totalQuestions: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Display the progress as text
        Text(
            text = "Question ${currentQuestionIndex + 1} of $totalQuestions",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Display the progress bar
        LinearProgressIndicator(
            progress = { (currentQuestionIndex + 1).toFloat() / totalQuestions },
            modifier = Modifier
                .fillMaxWidth()
                .height(9.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
        )
    }
}


@Preview(showBackground = true)
@Composable
fun QuizzScreenPreview() {
    AppTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        Surface {
            QuizzQuestionScreen(
                uiState = QuizzUiState(
                    isLoading = false,
                    isFinished = false,
                    currentQuestionIndex = 0,
                    selectedAnswer = null,
                    showCorrectAnswer = false,
                    questionTitle = "What is the capital of France?",
                    currentCorrectAnswer = "Paris",
                    answers = listOf("Paris", "London", "Berlin", "Madrid")
                ),
                onEvent = {},
                onFinished = {}
            )
        }
    }
}