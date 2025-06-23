package com.campusbites.app.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.campusbites.app.utils.LocaleUtils
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import com.campusbites.app.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("language_pref", Context.MODE_PRIVATE)
    val currentLang = LocaleUtils.getSavedLanguage(context)
    var selectedLang by remember { mutableStateOf(currentLang) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.language_settings)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = context.getString(R.string.select_language),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            val langs = listOf("en" to "English", "hi" to "हिन्दी", "pa" to "ਪੰਜਾਬੀ")
            langs.forEach { (code, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedLang == code,
                        onClick = { selectedLang = code },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = label,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (selectedLang != currentLang) {
                        prefs.edit().putString("lang_code", selectedLang).apply()
                        LocaleUtils.setLocale(context, selectedLang)

                        scope.launch {
                            snackbarHostState.showSnackbar(context.getString(R.string.language_changed))
                            delay(300)

                            // Restart current activity without navigating elsewhere
                            val activity = context as? Activity
                            activity?.let {
                                it.finish()
                                context.startActivity(Intent(context, it.javaClass))
                            }
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(context.getString(R.string.no_language_change))
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(context.getString(R.string.apply_and_restart), color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
