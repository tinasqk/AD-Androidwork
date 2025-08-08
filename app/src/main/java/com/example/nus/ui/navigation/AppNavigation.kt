package com.example.nus.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.nus.ui.screens.*
import com.example.nus.viewmodel.FeelViewModel
import com.example.nus.viewmodel.LifestyleViewModel
import com.example.nus.viewmodel.MoodViewModel
import com.example.nus.viewmodel.UserSessionViewModel
import com.example.nus.model.JournalEntry
import com.example.nus.viewmodel.JournalViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.nus.ui.screens.JournalDetailScreen




sealed class Screen(val route: String, val title: String) {
    object Login : Screen("login", "Login")
    object Register : Screen("register", "Register")
    object Home : Screen("home", "Home")
    object Mood : Screen("mood", "Mood")
    object Lifestyle : Screen("lifestyle", "Lifestyle")
    object Feel : Screen("feel", "Feel")
    object LifestyleLogged : Screen("lifestyle_logged", "Lifestyle Logged")
    object Journal : Screen("journal", "Journal")
    object JournalDetail : Screen("journalDetail/{entryIndex}", "Detail"){
        fun createRoute(index: Int) = "journalDetail/$index"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.Home,
        Screen.Mood,
        Screen.Lifestyle
    )

    // ViewModels
    val userSessionViewModel: UserSessionViewModel = viewModel()
    val moodViewModel: MoodViewModel = viewModel()
    val lifestyleViewModel: LifestyleViewModel = viewModel()
    val feelViewModel: FeelViewModel = viewModel()
    val journalViewModel: JournalViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val screensWithBottomBar = listOf(
        Screen.Home.route,
        Screen.Mood.route,
        Screen.Lifestyle.route,
        Screen.Feel.route,
        Screen.LifestyleLogged.route,
        Screen.Journal.route
    )

    val shouldShowBottomBar = currentDestination?.route in screensWithBottomBar

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    items.forEach { screen ->
                        val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            icon = {
                                when (screen) {
                                    Screen.Home -> if (selected) Icon(Icons.Filled.Home, null) else Icon(Icons.Outlined.Home, null)
                                    Screen.Mood -> if (selected) Icon(Icons.Filled.Face, null) else Icon(Icons.Outlined.Face, null)
                                    Screen.Lifestyle -> if (selected) Icon(Icons.Filled.DateRange, null) else Icon(Icons.Outlined.DateRange, null)
                                    else -> Icon(Icons.Outlined.Home, null)
                                }
                            },
                            label = { Text(screen.title) },
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = { userId, showEmotion ->
                        userSessionViewModel.setUserSession(userId, showEmotion)
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onSignUpClick = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onBackToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToMood = {
                        navController.navigate(Screen.Mood.route)
                    },
                    onNavigateToLifestyle = {
                        navController.navigate(Screen.Lifestyle.route)
                    },
                    onNavigateToJournal = {  // added navigation to JournalScreen
                        navController.navigate(Screen.Journal.route)
                    }
                )
            }

            composable(Screen.Mood.route) {
                MoodScreen(
                    viewModel = moodViewModel,
                    userId = userSessionViewModel.userId.value,
                    onNavigateToFeel = {
                        navController.navigate(Screen.Feel.route)
                    }
                )
            }

            composable(Screen.Lifestyle.route) {
                LifestyleScreen(
                    viewModel = lifestyleViewModel,
                    userId = userSessionViewModel.userId.value,
                    onNavigateToLifestyleLogged = {
                        navController.navigate(Screen.LifestyleLogged.route)
                    }
                )
            }

            composable(Screen.Feel.route) {
                FeelScreen(
                    viewModel = feelViewModel,
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(Screen.LifestyleLogged.route) {
                LifestyleLoggedScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(Screen.Journal.route) {
                JournalScreen(
                    journalList = journalViewModel.journalList,
                    navController = navController
                )
            }
            composable(
                Screen.JournalDetail.route,
                arguments = listOf(navArgument("entryIndex") { type = NavType.IntType })
            ) { backStackEntry ->
                val index = backStackEntry.arguments!!.getInt("entryIndex")
                journalViewModel.journalList.getOrNull(index)?.let { entry ->
                    JournalDetailScreen(entry = entry)
                }
            }
        }
    }
}
