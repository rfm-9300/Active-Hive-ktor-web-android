package rfm.hillsongpt.ui.home


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import rfm.hillsongpt.ui.RootNavigation
import rfm.hillsongpt.ui.components.AppBottomBar
import rfm.hillsongpt.ui.components.AppTopBar

@Composable
fun HomeScreen(
    modifier: Modifier,
    name: String,
    email: String,
    navigateToQuizz: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navigateToQuizz() },
            modifier = Modifier.padding(16.dp)

        ) {
            Text("Start Quizz")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    AppTheme (dynamicColor = false) {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold (
                bottomBar = { AppBottomBar() },
                topBar = { AppTopBar() }
            ){ values ->
                HomeScreen(
                    modifier = Modifier.padding(values),
                    name = "test",
                    email = "test",
                    navigateToQuizz = {  }
                )
            }


        }

    }

}