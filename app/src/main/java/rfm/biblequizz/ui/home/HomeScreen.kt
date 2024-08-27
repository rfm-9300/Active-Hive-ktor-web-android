package rfm.biblequizz.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import rfm.biblequizz.domain.model.Question

@Composable
fun HomeScreen(
    name: String,
    email: String,
    getQuestion: () -> Unit,
) {
    getQuestion()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(text = "$name and $email")
        }
    }
}