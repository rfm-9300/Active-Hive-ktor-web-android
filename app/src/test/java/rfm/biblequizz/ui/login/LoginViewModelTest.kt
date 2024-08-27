package rfm.biblequizz.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import rfm.biblequizz.data.model.LoginResult
import rfm.biblequizz.domain.model.Result


class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()



    private lateinit var viewModel: LoginViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)

    }

    @Test
    fun `login success updates authResults with Authorized`(): Unit = runTest(testDispatcher) {
        // Arrange


    }

    @Test
    fun `login failure updates authResults with Unauthorized`(): Unit = runTest(testDispatcher) {



    }

    @Test
    fun `test onEvent singIn`() = runTest(testDispatcher) {
    }

    @Test
    fun `test onEvent singUp`() = runTest(testDispatcher) {
        // Arrange

    }

    @Test
    fun `test onEvent singInPasswordChanged`() = runTest(testDispatcher) {
        // Arrange

    }

    @Test
    fun `test onEvent singUpPasswordChanged`() = runTest(testDispatcher) {
        // Arrange

    }
}