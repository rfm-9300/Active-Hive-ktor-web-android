package rfm.hillsongpt.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import rfm.hillsongpt.ui.home.MainNav
import rfm.hillsongpt.utils.navigateWithOptions

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
    val route: String
)

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false,
        route = MainNav.HomeScreen().toString()
    ),
    BottomNavigationItem(
        title = "Connect",
        selectedIcon = Icons.Filled.CheckCircle,  // Use appropriate icons
        unselectedIcon = Icons.Outlined.CheckCircle,
        hasNews = false,
        route = MainNav.ConnectGroupScreen.toString()
    ),
    BottomNavigationItem(
        title = "Service",
        selectedIcon = Icons.Filled.ShoppingCart,  // Use appropriate icons
        unselectedIcon = Icons.Outlined.ShoppingCart,
        hasNews = true,
        route = MainNav.ServiceScreen.toString()
    )
)

@Composable
fun AppBottomBar (
    navController: NavHostController,
    currentRoute: String? = null
){
    NavigationBar (
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 0.dp
    ) {
        var selectedItemIndex by rememberSaveable {
            mutableIntStateOf(0)
        }
        bottomNavigationItems.forEachIndexed  { index, item ->
            NavigationBarItem(
                selected = when (currentRoute) {
                    MainNav.HomeScreen().toString() -> index == 0
                    MainNav.ConnectGroupScreen.toString() -> index == 1
                    MainNav.ServiceScreen.toString() -> index == 2
                    else -> false
                },
                onClick = {
                    when (index) {
                        0 -> navController.navigateWithOptions(MainNav.HomeScreen())
                        1 -> navController.navigateWithOptions(MainNav.ConnectGroupScreen)
                        2 -> navController.navigateWithOptions(MainNav.ServiceScreen)
                    }
                },
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedIndicatorColor = Color.Transparent,
                ),
                label = { Text(text = item.title) },
                alwaysShowLabel = false,
                icon = {
                    BadgedBox(
                        badge = {
                            if(item.badgeCount != null){
                                Badge {
                                    Text(text = item.badgeCount.toString())
                                }
                            } else if (item.hasNews) {
                                Badge {
                                    Text(text = "•")
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (when (currentRoute) {
                                    MainNav.HomeScreen().toString() -> index == 0
                                    MainNav.ConnectGroupScreen.toString() -> index == 1
                                    MainNav.ServiceScreen.toString() -> index == 2
                                    else -> false
                                }) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }
                },
            )
        }
    }
}

