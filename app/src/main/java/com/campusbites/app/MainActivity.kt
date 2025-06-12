package com.campusbites.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.campusbites.app.ui.theme.CampusbitesTheme
import com.google.firebase.FirebaseApp
import androidx.navigation.compose.rememberNavController      // ✅ for rememberNavController
import com.campusbites.app.navigation.AppNavHost              // ✅ for your custom NavHost
import android.util.Log


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            val navController = rememberNavController()
            CampusbitesTheme {
                AppNavHost(navController)
            }
        }
    }
}