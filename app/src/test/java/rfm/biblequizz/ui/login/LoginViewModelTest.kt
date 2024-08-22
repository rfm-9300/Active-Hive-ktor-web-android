package rfm.biblequizz.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import rfm.biblequizz.data.model.AuthResult
import rfm.biblequizz.domain.model.UseCaseResult
import rfm.biblequizz.domain.usecase.LoginUseCase

class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var loginUseCase: LoginUseCase

    private lateinit var viewModel: LoginViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = LoginViewModel(loginUseCase)
    }

    @Test
    fun `login success updates authResults with Authorized`(): Unit = runTest(testDispatcher) {
        // Arrange
        `when`(loginUseCase.invoke("testUser", "testPass")).thenReturn(UseCaseResult.Success(Unit))

        // Act
        viewModel.onEvent(LoginUiEvent.SignInUsernameChanged("testUser"))
        viewModel.onEvent(LoginUiEvent.SignInPasswordChanged("testPass"))
        viewModel.onEvent(LoginUiEvent.Login)

        // Assert
        val result = viewModel.authResults.first()
        assert(result is AuthResult.Authorized)
    }
}