package rfm.hillsongpt.ui.login


import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rfm.hillsongpt.R
import rfm.hillsongpt.data.model.LoginResult
import rfm.hillsongpt.domain.model.Result
import rfm.hillsongpt.domain.repository.LoginRepository
import rfm.hillsongpt.utils.ErrorMessage
import java.util.UUID
import javax.inject.Inject

data class LoginUiState (
    val isLoading: Boolean = false,
    val usernameField: String = "",
    val passwordField: String = "",
    val isUsernameValid : Boolean = false,
    val isPasswordValid : Boolean = false,
    val isRememberMeChecked : Boolean = false,
    val isAuthorized: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList()
)

private data class LoginViewModelState(
    val usernameField : String = "",
    val passwordField : String = "",
    val isUsernameValid : Boolean = false,
    val isPasswordValid : Boolean = false,
    val isRememberMeChecked : Boolean = false,
    val isLoading : Boolean = false,
    val isAuthorized: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList()
){
    /**
     * Converts this [LoginViewModelState] to a [LoginUiState] for use in the UI layer.
     */
    fun toUiState(): LoginUiState {
        return LoginUiState(
            usernameField = usernameField,
            passwordField = passwordField,
            isUsernameValid = isUsernameValid,
            isPasswordValid = isPasswordValid,
            isRememberMeChecked = isRememberMeChecked,
            isLoading = isLoading,
            isAuthorized = isAuthorized,
            errorMessages = errorMessages
        )
    }
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel() {

    private val viewModelState = MutableStateFlow(LoginViewModelState())

    val uiState = viewModelState
        .map(LoginViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )


    fun onEvent(event: LoginUiEvent) {
        when(event) {
            is LoginUiEvent.LoginButtonClicked -> {
                doLogin(event.username, event.password)
            }
            is LoginUiEvent.UsernameTextFieldChanged -> {
                viewModelState.update { it.copy(usernameField = event.value) }
            }
            is LoginUiEvent.PasswordTextFieldChanged -> {
                viewModelState.update { it.copy(passwordField = event.value) }
            }
            is LoginUiEvent.RememberMeCheckboxChanged -> TODO()
            is LoginUiEvent.ErrorDismissed -> {
                errorShown(event.id)
            }
        }
    }

    /**
     * Notify that an error was displayed on the screen
     */
    private fun errorShown(id: Long) {
        viewModelState.update {
            it.copy(errorMessages = it.errorMessages.filter { it.id != id })
        }
    }

    private fun doLogin(username: String, password: String) {
        // Validate fields
        val areFieldsValid = validateFields(username, password)
        if (!areFieldsValid) return

        // Ui state is refreshing
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result  = loginRepository.login(username, password)
            viewModelState.update {
                when(result) {
                    is Result.Success -> {
                        it.copy(isLoading = false, isAuthorized = true)
                    }
                    is Result.Error -> {
                        val error = result.error
                        val errorMessage = when(error) {
                            is LoginResult.Unauthorized -> ErrorMessage(id = UUID.randomUUID().mostSignificantBits, messageId = R.string.login_error_unauthorized)
                            is LoginResult.UnknownError -> ErrorMessage(id = UUID.randomUUID().mostSignificantBits, messageId = R.string.login_error_unknown)
                            // else never happens
                            else -> ErrorMessage(id = UUID.randomUUID().mostSignificantBits, messageId = R.string.login_error_unknown)
                        }
                        it.copy(isLoading = false, errorMessages = it.errorMessages + errorMessage)
                    }

                    is Result.GenericError -> TODO()
                }
            }

        }
    }

    private fun validateFields(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }
}