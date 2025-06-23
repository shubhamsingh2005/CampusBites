package com.campusbites.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.key
import androidx.navigation.compose.rememberNavController
import com.campusbites.app.navigation.AppNavHost
import com.campusbites.app.ui.theme.CampusbitesTheme
import com.campusbites.app.utils.LocaleUtils
import com.campusbites.app.utils.ThemeUtils
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val langCode = LocaleUtils.getSavedLanguage(newBase)
        val contextWithLocale = LocaleUtils.setLocale(newBase, langCode)
        super.attachBaseContext(contextWithLocale)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        val langCode = LocaleUtils.getSavedLanguage(this)
        val isDarkMode = ThemeUtils.isDarkMode(this) // ⬅️ fetch dark mode setting

        setContent {
            // 🔁 UI recomposes on language key change
            key(langCode) {
                CampusbitesTheme(darkTheme = isDarkMode) {
                    val navController = rememberNavController()
                    AppNavHost(navController)
                }
            }
        }
    }
}
