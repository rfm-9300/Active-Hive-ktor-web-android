package rfm.biblequizz.ui.quizz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rfm.biblequizz.data.log.Timber
import rfm.biblequizz.domain.model.Question
import rfm.biblequizz.domain.repository.QuestionRepository
import rfm.biblequizz.domain.model.Result

sealed class QuizzUiEvent{
    data object NextButtonClick : QuizzUiEvent()
}

data class QuizzUiState (
    val isLoading: Boolean = false,
    val isFinished: Boolean = false,
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: String? = null,
    val showCorrectAnswer: Boolean = false,
    val questionTitle : String = "",
    val currentCorrectAnswer: String = "",
    val answers: List<String> = emptyList(),
    val totalQuestions: Int = 0,
    val totalCorrectAnswers: Int = 0,
    val totalWrongAnswers: Int = 0
)

private data class QuizzViewModelState(
    val isLoading : Boolean = true,
    val isFinished : Boolean = false,
    val currentQuestionIndex: Int = 0,
    val questions: List<Question> = emptyList(),
    val selectedAnswer: String? = null,
    val showCorrectAnswer: Boolean = false,
    val totalCorrectAnswers: Int = 0,
    val totalWrongAnswers: Int = 0
){
    /**
     * Converts this [QuizzViewModelState] to a [QuizzUiState] for use in the UI layer.
     */
    fun toUiState(): QuizzUiState {
        // in case there are no questions, because we are still loading or there was an error
        return if (questions.isEmpty()) {
            QuizzUiState(
                isLoading = isLoading,
                isFinished = isFinished,
                currentQuestionIndex = currentQuestionIndex,
                selectedAnswer = selectedAnswer,
                showCorrectAnswer = showCorrectAnswer,
                questionTitle = "",
                currentCorrectAnswer = "",
                answers = emptyList()
            )
        } else {
            QuizzUiState(
                isLoading = isLoading,
                questionTitle = questions[currentQuestionIndex].title,
                isFinished = isFinished,
                currentQuestionIndex = currentQuestionIndex,
                selectedAnswer = selectedAnswer,
                showCorrectAnswer = showCorrectAnswer,
                currentCorrectAnswer = questions[currentQuestionIndex].correctAnswer,
                answers = questions[currentQuestionIndex].wrongAnswers + questions[currentQuestionIndex].correctAnswer,
                totalQuestions = questions.size,
                totalCorrectAnswers = totalCorrectAnswers,
                totalWrongAnswers = totalWrongAnswers
            )
        }
    }
}


@HiltViewModel
class QuizzViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
): ViewModel() {

    init {
        Timber.i("QuizzViewModel created")
    }

    private val viewModelState = MutableStateFlow(QuizzViewModelState())

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toUiState()
        )
    init {
        getQuestions()
    }

    fun onEvent(event: QuizzUiEvent) {
        when(event) {
            QuizzUiEvent.NextButtonClick -> onNextQuestion()
        }
    }

    private fun getQuestions() {
        Timber.i("Fetching questions from QuizzViewModel")
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when(val result = questionRepository.getQuestionsFromLocal()) {
                is Result.Success -> {
                    Timber.i("Questions fetched from QuizzViewModel")
                    viewModelState.update { it.copy(isLoading = false, questions = result.data) }
                }
                is Result.Error -> {
                    Timber.i("Error fetching questions from QuizzViewModel")
                    viewModelState.value = viewModelState.value.copy(isLoading = false)
                }
                is Result.GenericError -> {
                    Timber.i("Error fetching questions from QuizzViewModel")
                    viewModelState.value = viewModelState.value.copy(isLoading = false)
                }
            }
        }
    }

    private fun onNextQuestion() {
        // check if we are at the last question
        if (viewModelState.value.isFinished) return // Avoid any further updates if finished
        if (viewModelState.value.currentQuestionIndex == viewModelState.value.questions.size - 1) {
            Timber.i("Quizz finished")
            updateAnswerCount()
            viewModelState.update { it.copy(isFinished = true) }
            return
        }
        updateAnswerCount()
        viewModelState.update { it.copy(currentQuestionIndex = it.currentQuestionIndex + 1) }
    }

    private fun updateAnswerCount() {
        // check if the selected answer is correct
        val currentQuestion = viewModelState.value.questions[viewModelState.value.currentQuestionIndex]
        val isCorrect = viewModelState.value.selectedAnswer == currentQuestion.correctAnswer
        if (isCorrect) {
            viewModelState.update { it.copy(totalCorrectAnswers = it.totalCorrectAnswers + 1) }
            Timber.i("Correct answer, total correct answers: ${viewModelState.value.totalCorrectAnswers}")
        } else {
            viewModelState.update { it.copy(totalWrongAnswers = it.totalWrongAnswers + 1) }
            Timber.i("Wrong answer, total wrong answers: ${viewModelState.value.totalWrongAnswers}")
        }
    }
}