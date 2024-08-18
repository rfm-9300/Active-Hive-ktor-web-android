package rfm.biblequizz.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rfm.biblequizz.data.model.AuthResult
import rfm.biblequizz.domain.repository.LoginRepository;

class LoginUseCase (
    private val loginRepository: LoginRepository,
    private val dispatcher:CoroutineDispatcher= Dispatchers.IO
) {
    suspend operator fun invoke(username: String, password: String): Boolean = withContext(dispatcher) {
        val response = loginRepository.login(username, password)
        return@withContext response is AuthResult.Authorized
    }
}