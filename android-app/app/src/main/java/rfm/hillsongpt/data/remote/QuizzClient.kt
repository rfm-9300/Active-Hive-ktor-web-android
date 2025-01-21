package rfm.hillsongpt.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import rfm.hillsongpt.data.log.Timber
import rfm.hillsongpt.data.remote.interceptor.MockRequestInterceptor
import java.util.concurrent.TimeUnit

object QuizzClient {

    private const val BASE_URL = "http://172.233.111.163:8080/"
    private const val BASE_TIMEOUT = 45L

    fun build(
        mockupInterceptor: MockRequestInterceptor
    ): QuizzApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideHttpClient(mockupInterceptor))
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(QuizzApi::class.java)
    }

    private fun provideHttpClient(
        mockupInterceptor: MockRequestInterceptor
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor { message -> Timber.i(message) }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(Interceptor.invoke { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                chain.proceed(request)
            })
            .addInterceptor(mockupInterceptor)
            .readTimeout(BASE_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(BASE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

}