package rfm.hillsongpt.data.remote

import retrofit2.http.GET
import rfm.hillsongpt.domain.model.Question

interface QuizzApi {

    @GET("questions")
    suspend fun getQuestions() : List<Question>

}