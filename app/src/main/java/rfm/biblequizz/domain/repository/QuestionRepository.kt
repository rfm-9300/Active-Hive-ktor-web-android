package rfm.biblequizz.domain.repository

import rfm.biblequizz.domain.model.Question
import rfm.biblequizz.domain.model.Result

interface QuestionRepository {
    suspend fun getQuestions(): Result<List<Question>>
}