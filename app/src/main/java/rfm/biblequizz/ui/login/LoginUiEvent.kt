package rfm.biblequizz.ui.login

sealed class LoginUiEvent {
    data class UsernameTextFieldChanged(val value: String): LoginUiEvent()
    data class PasswordTextFieldChanged(val value: String): LoginUiEvent()
    data object RememberMeCheckboxChanged: LoginUiEvent()
    data class LoginButtonClicked(val username:String, val password: String): LoginUiEvent()

}