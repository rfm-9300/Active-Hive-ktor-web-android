package rfm.biblequizz.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rfm.biblequizz.data.local.Roomdb
import rfm.biblequizz.data.local.toEntity
import rfm.biblequizz.data.log.Timber
import rfm.biblequizz.data.remote.QuizzApi
import rfm.biblequizz.domain.model.Question
import rfm.biblequizz.domain.model.Result
import rfm.biblequizz.domain.repository.QuestionRepository

class QuestionRepositoryImpl(
    private val db : Roomdb,
    private val quizzApi: QuizzApi
) : QuestionRepository {



    override suspend fun getQuestions(): Result<List<Question>> = withContext(Dispatchers.IO) {
        return@withContext try {
            Timber.i("Fetching questions")
            val questions = quizzApi.getQuestions()
            db.questionDao.save(questions.map { it.toEntity() })
            Result.Success(questions)
        } catch (e: Exception) {
            Timber.e(e)
            Result.GenericError(e)
        }
    }
}