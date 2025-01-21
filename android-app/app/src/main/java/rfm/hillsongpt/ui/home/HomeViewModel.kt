package rfm.hillsongpt.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import rfm.hillsongpt.data.log.Timber
import rfm.hillsongpt.domain.repository.QuestionRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val questionRepository: QuestionRepository
): ViewModel() {

    init {
        getQuestions()
    }

    private fun getQuestions() {
        viewModelScope.launch {
            Timber.i("Fetching questions from HomeViewModel")
            questionRepository.getQuestionsFromRemote()
        }

    }


}