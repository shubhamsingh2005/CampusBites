package com.campusbites.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.campusbites.app.screens.*

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("signup") {
            SignupScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("phone") {
            PhoneAuthScreen(navController)
        }
        composable("language_settings") {
            LanguageSettingsScreen(navController)
        }
        composable("forgot") {
            ForgotPasswordScreen(navController)
        }
        composable("profile") {
            ProfileScreen(navController) // ✅ NEW route added
        }
        composable("settings") {
            SettingsScreen(navController)
        }

    }
}
