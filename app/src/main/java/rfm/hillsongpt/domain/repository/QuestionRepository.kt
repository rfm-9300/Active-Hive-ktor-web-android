package rfm.hillsongpt.domain.repository

import rfm.hillsongpt.domain.model.Question
import rfm.hillsongpt.domain.model.Result

interface QuestionRepository {
    suspend fun getQuestionsFromRemote(): Result<List<Question>>
    suspend fun getQuestionsFromLocal(): Result<List<Question>>
}
