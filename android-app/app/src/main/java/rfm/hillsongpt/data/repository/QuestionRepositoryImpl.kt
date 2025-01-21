package rfm.hillsongpt.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rfm.hillsongpt.data.local.Roomdb
import rfm.hillsongpt.data.local.toDomain
import rfm.hillsongpt.data.local.toEntity
import rfm.hillsongpt.data.log.Timber
import rfm.hillsongpt.data.remote.QuizzApi
import rfm.hillsongpt.domain.model.Question
import rfm.hillsongpt.domain.model.Result
import rfm.hillsongpt.domain.repository.QuestionRepository

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