package rfm.biblequizz.repositories

import rfm.biblequizz.data.model.LoginResult
import rfm.biblequizz.domain.repository.LoginRepository

class FakeLoginRepository : LoginRepository {

    // Define the initial states or configurations for tests
    private var signUpResult: LoginResult<Unit> = LoginResult.Authorized()
    private var loginResult: LoginResult<Unit> = LoginResult.Authorized()
    private var authenticateResult: LoginResult<Unit> = LoginResult.Authorized()

   //Set the results of the tests
    fun setSignUpResult(result: LoginResult<Unit>) {
        signUpResult = result
    }

    fun setLoginResult(result: LoginResult<Unit>) {
        loginResult = result
    }

    fun setAuthenticateResult(result: LoginResult<Unit>) {
        authenticateResult = result
    }

    override suspend fun signUp(username: String, password: String): LoginResult<Unit> {
        return signUpResult
    }

    override suspend fun login(username: String, password: String): LoginResult<Unit> {
        return loginResult
    }

    override suspend fun authenticate(): LoginResult<Unit> {
        return authenticateResult
    }
}
