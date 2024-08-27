package rfm.biblequizz.domain.model

/**
 * A generic class that holds a value or an error
 * @param <T>
 */
sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<U : Any>(val error: U, val exception: Exception? = null ) : Result<U>()
    data class GenericError(val exception: Exception) : Result<Nothing>()
}

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}
