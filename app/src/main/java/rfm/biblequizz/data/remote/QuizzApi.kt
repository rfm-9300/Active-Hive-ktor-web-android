package rfm.biblequizz.data.remote

import retrofit2.http.GET
import rfm.biblequizz.domain.model.Question

interface QuizzApi {

    @GET("questions")
    suspend fun getQuestions() : List<Question>

}