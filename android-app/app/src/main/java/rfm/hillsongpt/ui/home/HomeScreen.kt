package rfm.hillsongpt.ui.home


import android.graphics.drawable.Icon
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import rfm.hillsongpt.R
import rfm.hillsongpt.ui.components.AppBottomBar
import rfm.hillsongpt.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    name: String,
    email: String,
    navigateToQuizz: () -> Unit
) {
    // Create a scroll state to control the scrolling
    val scrollState = rememberScrollState()

    val buttonsList = listOf(
        RowButtons(
            title = "Assistir Online",
            onClick = {  },
            icon = Icons.Filled.LocationOn
        ),
        RowButtons(
            title = "Convidar",
            onClick = {  },
            icon = Icons.Filled.LocationOn
        ),
        RowButtons(
            title = "Check In",
            onClick = {  },
            icon = Icons.Filled.LocationOn
        ),
        RowButtons(
            title = "Mais Informações",
            onClick = {  },
            icon = Icons.Filled.LocationOn
        )
    )

    Column(
        modifier = modifier.fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            modifier = Modifier.fillMaxWidth(),
            text= stringResource(R.string.home_screen_title),
            style = MaterialTheme.typography.headlineMedium.copy(
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
            ))
        // Buttons Row
        Row (
            Modifier.horizontalScroll(rememberScrollState())
        ){
            buttonsList.forEach { button ->
                ButtonWithIconAndText(
                    text = button.title,
                    icon = button.icon,
                    onClick = button.onClick,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }





        Button(
            onClick = { navigateToQuizz() },
            modifier = Modifier.padding(16.dp)

        ) {
            Text(text = "Start Quizz")
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
                bottomBar = { AppBottomBar(
                    navController = rememberNavController(),
                    currentRoute = ""
                ) },
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

@Composable
fun ButtonWithIconAndText(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Row {
            Icon(
                imageVector = icon,
                contentDescription = null, // Content description for accessibility
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

data class RowButtons(
    val title: String,
    val onClick: () -> Unit,
    val icon: ImageVector
)