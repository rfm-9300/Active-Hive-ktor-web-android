package rfm.biblequizz.data.model

sealed class LoginResult<T>(val data: T? = null) {
    class Authorized<T>(data: T? = null): LoginResult<T>(data)
    class Unauthorized<T>: LoginResult<T>()
    class UnknownError<T>: LoginResult<T>()
}