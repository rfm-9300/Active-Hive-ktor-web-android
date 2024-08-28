package rfm.biblequizz.domain.repository

import rfm.biblequizz.domain.model.Question
import rfm.biblequizz.domain.model.Result

interface QuestionRepository {
    suspend fun getQuestionsFromRemote(): Result<List<Question>>
    suspend fun getQuestionsFromLocal(): Result<List<Question>>
}
