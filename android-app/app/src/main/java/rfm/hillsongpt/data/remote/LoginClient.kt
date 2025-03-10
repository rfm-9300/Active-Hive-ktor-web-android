package rfm.hillsongpt.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import rfm.hillsongpt.data.log.Timber
import java.util.concurrent.TimeUnit

object LoginClient {

    private const val BASE_URL = "http://172.233.111.163:8080/"
    private const val BASE_TIMEOUT = 45L

    fun build(): LoginApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideHttpClient())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(LoginApi::class.java)
    }

    private fun provideHttpClient(): OkHttpClient {
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
            .readTimeout(BASE_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(BASE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
}