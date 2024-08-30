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

data class QuizzUiEvent(
    val onAnswerSelected: (String) -> Unit
)

data class QuizzUiState (
    val isLoading: Boolean = false,
    val isFinished: Boolean = false,
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: String? = null,
    val showCorrectAnswer: Boolean = false,
    val questionTitle : String = "",
    val correctAnswer: String = "",
    val answers: List<String> = emptyList(),
    val totalQuestions: Int = 0
)

private data class QuizzViewModelState(
    val isLoading : Boolean = true,
    val isFinished : Boolean = false,
    val currentQuestionIndex: Int = 0,
    val questions: List<Question> = emptyList(),
    val selectedAnswer: String? = null,
    val showCorrectAnswer: Boolean = false
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
                correctAnswer = "",
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
                correctAnswer = questions[currentQuestionIndex].correctAnswer,
                answers = questions[currentQuestionIndex].wrongAnswers + questions[currentQuestionIndex].correctAnswer,
                totalQuestions = questions.size
            )
        }
    }
}


@HiltViewModel
class QuizzViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
): ViewModel() {

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
            else -> {
                event.onAnswerSelected("answer")
            }
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

    fun onNextQuestion() {
        if (viewModelState.value.currentQuestionIndex == viewModelState.value.questions.size - 1) {
            viewModelState.update { it.copy(isFinished = true) }
            return
        }
        viewModelState.update { it.copy(currentQuestionIndex = it.currentQuestionIndex + 1) }
    }
}