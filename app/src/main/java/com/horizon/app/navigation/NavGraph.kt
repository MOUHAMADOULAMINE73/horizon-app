package com.horizon.app.navigation
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.horizon.app.network.Listing
import com.horizon.app.ui.AppViewModel
import com.horizon.app.ui.screens.*

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FEED = "feed"
    const val PUBLISH = "publish"
    const val PROFILE = "profile"
    const val DETAIL = "detail"
}

@Composable
fun HorizonNavGraph(viewModel: AppViewModel) {
    val navController = rememberNavController()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    var selectedListing by remember { mutableStateOf<Listing?>(null) }

    val bottomBarRoutes = setOf(Routes.FEED, Routes.PUBLISH, Routes.PROFILE)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (isLoggedIn && currentRoute in bottomBarRoutes) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Routes.FEED,
                        onClick = { navController.navigate(Routes.FEED) { launchSingleTop = true } },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Accueil") },
                        label = { Text("Annonces") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.PUBLISH,
                        onClick = { navController.navigate(Routes.PUBLISH) { launchSingleTop = true } },
                        icon = { Icon(Icons.Default.AddCircle, contentDescription = "Publier") },
                        label = { Text("Publier") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.PROFILE,
                        onClick = { navController.navigate(Routes.PROFILE) { launchSingleTop = true } },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                        label = { Text("Profil") }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) Routes.FEED else Routes.LOGIN,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        navController.navigate(Routes.FEED) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onGoToRegister = { navController.navigate(Routes.REGISTER) }
                )
            }
            composable(Routes.REGISTER) {
                RegisterScreen(
                    viewModel = viewModel,
                    onRegisterSuccess = {
                        navController.navigate(Routes.FEED) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onGoToLogin = { navController.popBackStack() }
                )
            }
            composable(Routes.FEED) {
                FeedScreen(
                    viewModel = viewModel,
                    onListingClick = {
                        selectedListing = it
                        navController.navigate(Routes.DETAIL)
                    }
                )
            }
            composable(Routes.PUBLISH) {
                PublishScreen(
                    viewModel = viewModel,
                    onPublished = { navController.navigate(Routes.FEED) { popUpTo(Routes.FEED) { inclusive = true } } }
                )
            }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    viewModel = viewModel,
                    onLogout = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(Routes.DETAIL) {
                selectedListing?.let { listing ->
                    ListingDetailScreen(
                        listing = listing,
                        onBack = { navController.popBackStack() },
                        onContactSeller = { /* V2 : ouvrir l'écran de messagerie */ }
                    )
                }
            }
        }
    }
}
