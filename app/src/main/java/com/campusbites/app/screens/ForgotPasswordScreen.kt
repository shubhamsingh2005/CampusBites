@file:OptIn(ExperimentalMaterial3Api::class)

package com.campusbites.app.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.campusbites.app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val firebaseAuth = FirebaseAuth.getInstance()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.forgot_password)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.reset_password_instruction),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email_label)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (email.isBlank() || !email.contains("@")) {
                        Toast.makeText(context, context.getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    firebaseAuth.fetchSignInMethodsForEmail(email).addOnSuccessListener { result ->
                        val signInMethods = result.signInMethods
                        if (signInMethods.isNullOrEmpty()) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(context.getString(R.string.user_not_found))
                            }
                        } else {
                            firebaseAuth.sendPasswordResetEmail(email)
                                .addOnSuccessListener {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(context.getString(R.string.reset_link_sent))
                                        navController.popBackStack()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    val errorMessage = when (e) {
                                        is FirebaseAuthInvalidUserException -> context.getString(R.string.user_not_found)
                                        is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.invalid_email_format)
                                        else -> context.getString(R.string.reset_failed)
                                    }
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(errorMessage)
                                    }
                                }
                        }
                    }.addOnFailureListener {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(context.getString(R.string.reset_failed))
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.send_reset_link))
            }
        }
    }
}
