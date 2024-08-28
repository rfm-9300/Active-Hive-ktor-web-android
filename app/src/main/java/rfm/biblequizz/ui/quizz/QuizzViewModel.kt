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
    val questions: List<Question> = emptyList(),
    val isFinished: Boolean = false
)

private data class QuizzViewModelState(
    val isLoading : Boolean = true,
    val questions: List<Question> = emptyList(),
    val isFinished : Boolean = false
){
    /**
     * Converts this [QuizzViewModelState] to a [QuizzUiState] for use in the UI layer.
     */
    fun toUiState(): QuizzUiState {
        return QuizzUiState(
            isLoading = isLoading,
            questions = questions,
            isFinished = isFinished
        )
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
}