package com.campusbites.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.campusbites.app.screens.LoginScreen
import com.campusbites.app.screens.SignupScreen
import com.campusbites.app.screens.HomeScreen
import com.campusbites.app.screens.PhoneAuthScreen  // ✅ Import for Phone Login screen

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
            HomeScreen()
        }
        composable("phone") {
            PhoneAuthScreen(navController) // ✅ Phone OTP login screen
        }
    }
}
