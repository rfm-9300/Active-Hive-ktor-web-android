package rfm.hillsongpt.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import rfm.hillsongpt.data.model.LoginRequest
import rfm.hillsongpt.data.model.TokenResponse

interface LoginApi {

    @POST("signup")
    suspend fun signUp(
        @Body request: LoginRequest
    )

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): TokenResponse

    @GET("authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )

    @GET("/")
    suspend fun test()
}