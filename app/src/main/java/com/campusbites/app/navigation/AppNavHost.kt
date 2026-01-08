package com.campusbites.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.campusbites.app.screens.*
import com.campusbites.app.viewmodel.CartViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    // ✅ Create shared CartViewModel manually
    val cartViewModel = remember { CartViewModel() }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // 🔐 Auth Screens
        composable("login") {
            LoginScreen(navController)
        }
        composable("signup") {
            SignupScreen(navController)
        }

        // ⚙️ Settings and Profile
        composable("language_settings") {
            LanguageSettingsScreen(navController)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController)
        }

        // 🏠 Main Screens
        composable("home") {
            HomeScreen(navController)
        }
        composable("categories") {
            CategoriesScreen(navController)
        }

        // 🛒 Shop Page
        composable("shop/{shopId}") { backStackEntry ->
            val shopId = backStackEntry.arguments?.getString("shopId")?.toIntOrNull() ?: 0
            ShopPageScreen(
                navController = navController,
                shopId = shopId,
                cartViewModel = cartViewModel // ✅ Pass manually
            )
        }

        // 🧺 Cart Screen (also receives viewModel)
        composable("cart") {
            CartScreen(navController, cartViewModel = cartViewModel)
        }
    }
}
