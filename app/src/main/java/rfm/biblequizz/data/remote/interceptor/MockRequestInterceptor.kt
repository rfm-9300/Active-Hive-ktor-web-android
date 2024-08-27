package rfm.biblequizz.data.remote.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody

class MockRequestInterceptor(val context: Context) : Interceptor {

    companion object {
        private val JSON_MEDIA_TYPE = "application/json".toMediaTypeOrNull()
        private const val MOCK = "mock"
        private const val RESULT_CODE = "resultCode"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = Response.Builder()
            .code(200)
            .message("OK")
            .request(chain.request())
            .protocol(Protocol.HTTP_1_0)
            .body(
                context.readFileFromAssets("questions.json")
                    .toResponseBody(JSON_MEDIA_TYPE)
            )
            .addHeader("content-type", "application/json")
            .build()
        return response
    }

    fun Context.readFileFromAssets(filePath: String): String {
        return resources.assets.open(filePath).bufferedReader().use {
            it.readText()
        }
    }
}