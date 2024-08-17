package rfm.biblequizz.ui.signup


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import rfm.biblequizz.R
import rfm.biblequizz.ui.components.HeaderText
import rfm.biblequizz.ui.components.LoginTextField
import rfm.biblequizz.ui.theme.BiblequizzTheme

val defaultPadding = 30.dp
val itemSpacing = 8.dp
val spacingTop = 10.dp



@Composable
fun SignUpScreen(

) {

    val context = LocalContext.current

    val (firstName, setFirstName) = rememberSaveable { mutableStateOf("") }
    val (lastName, setLastName) = rememberSaveable { mutableStateOf("") }
    val (email, setEmail) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    val (confirmPassword, setConfirmPassword) = rememberSaveable { mutableStateOf("") }
    val (checked, setChecked) = rememberSaveable { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderText(text = stringResource(id = R.string.sign_up_screen_sign_up),
            modifier = Modifier
                .padding(top = 30.dp)
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(itemSpacing))
        LoginTextField(
            textModifier = Modifier.padding(start = 10.dp),
            value = firstName,
            onValueChange = setFirstName,
            labelText = stringResource(id = R.string.sign_up_screen_first_name),
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(itemSpacing))
        LoginTextField(
            textModifier = Modifier.padding(start = 10.dp),
            value = lastName,
            onValueChange = setLastName,
            labelText = stringResource(id = R.string.sign_up_screen_last_name),
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(itemSpacing))
        LoginTextField(
            textModifier = Modifier.padding(start = 10.dp),
            value = email,
            onValueChange = setEmail,
            labelText = stringResource(id = R.string.sign_up_screen_email),
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(itemSpacing))
        LoginTextField(
            textModifier = Modifier.padding(start = 10.dp),
            value = password,
            onValueChange = setPassword,
            labelText = stringResource(id = R.string.sign_up_screen_password),
            visualTransformation = PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password
        )
        Spacer(modifier = Modifier.height(itemSpacing))
        LoginTextField(
            textModifier = Modifier.padding(start = 10.dp),
            value = confirmPassword,
            onValueChange = setConfirmPassword,
            labelText = stringResource(id = R.string.sign_up_screen_confirm_password),
            visualTransformation = PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password
        )
        Spacer(modifier = Modifier.height(3 * itemSpacing))
        Row {
            val agreeText = stringResource(id = R.string.sign_up_screen_agree_with)
            val privacyText = stringResource(id = R.string.sign_up_screen_privacy)
            val policyText = stringResource(id = R.string.sign_up_screen_policy)
            val andText = stringResource(id = R.string.sign_up_screen_and)
            val annotatedString = buildAnnotatedString {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append(agreeText)
                }
                append(" ")
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    pushStringAnnotation(tag = privacyText, annotation = privacyText)
                    append(privacyText)
                }
                append(" ")
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append(andText)
                }
                append(" ")
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    pushStringAnnotation(tag = policyText, annotation = policyText)
                    append(policyText)
                }
            }

            Checkbox(checked = checked, onCheckedChange = setChecked)
            ClickableText(
                text = annotatedString,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) { offset ->
                annotatedString.getStringAnnotations(tag = privacyText, start = offset, end = offset)
                    .firstOrNull()?.let {
                        // Handle privacy click
                        Toast.makeText(context, "Privacy clicked", Toast.LENGTH_SHORT).show()
                    }
                annotatedString.getStringAnnotations(tag = policyText, start = offset, end = offset)
                    .firstOrNull()?.let {
                        // Handle policy click
                        Toast.makeText(context, "Policy clicked", Toast.LENGTH_SHORT).show()
                    }

            }
        }
        Spacer(modifier = Modifier.height(4 * itemSpacing))
        // sing in button change color if all fields are filled
        val buttonColor = if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && checked) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.tertiary
        }
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(text = stringResource(id = R.string.sign_up_screen_sign_in))
        }
        Spacer(modifier = Modifier.height(itemSpacing))
        val signInText = stringResource(id = R.string.sign_up_screen_already_have_account)
        val signIn = stringResource(id = R.string.sign_up_screen_sign_in)
        val signInAnnotatedString = buildAnnotatedString {
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                append(signInText)
            }
            append(" ")
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                pushStringAnnotation(tag = signIn, annotation = signIn)
                append(signIn)
            }
        }
        ClickableText(
            text = signInAnnotatedString,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { offset ->
            signInAnnotatedString.getStringAnnotations(tag = signIn, start = offset, end = offset)
                .firstOrNull()?.let {
                    // Handle sign in click
                    Toast.makeText(context, "Sign in clicked", Toast.LENGTH_SHORT).show()
                }
        }

    }
}




@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    BiblequizzTheme () {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SignUpScreen()
        }

    }
}