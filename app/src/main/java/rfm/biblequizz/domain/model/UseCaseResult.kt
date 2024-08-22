package rfm.biblequizz.domain.model

sealed class UseCaseResult<out T : Any, out U : Any> {
    data class Success<T : Any>(val result: T) : UseCaseResult<T, Nothing>()
    data class Error<U : Any>(val error: U) : UseCaseResult<Nothing, U>()
}