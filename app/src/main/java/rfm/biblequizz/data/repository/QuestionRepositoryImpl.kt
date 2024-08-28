package rfm.biblequizz.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rfm.biblequizz.data.local.Roomdb
import rfm.biblequizz.data.local.toDomain
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



    override suspend fun getQuestionsFromRemote(): Result<List<Question>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val questions = quizzApi.getQuestions()
            db.questionDao.save(questions.map { it.toEntity() })
            Result.Success(questions)
        } catch (e: Exception) {
            Timber.e(e)
            Result.GenericError(e)
        }
    }

    override suspend fun getQuestionsFromLocal(): Result<List<Question>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val questions = db.questionDao.getAll().map { it.toDomain() }
            Timber.i("Questions from local: $questions")
            Result.Success(questions)
        } catch (e: Exception) {
            Timber.i("Error fetching questions from local, ${e.message}")
            Result.GenericError(e)
        }
    }
}