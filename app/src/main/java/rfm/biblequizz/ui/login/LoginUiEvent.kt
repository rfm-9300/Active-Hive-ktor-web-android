package rfm.biblequizz.ui.login

sealed class LoginUiEvent {
    data object Login: LoginUiEvent()

    data class SignUpUsernameChanged(val value: String): LoginUiEvent()
    data class SignUpPasswordChanged(val value: String): LoginUiEvent()
    data class SignInUsernameChanged(val value: String): LoginUiEvent()
    data class SignInPasswordChanged(val value: String): LoginUiEvent()

}