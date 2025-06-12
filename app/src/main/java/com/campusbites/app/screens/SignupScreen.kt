package com.campusbites.app.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SignupScreen(navController: NavHostController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create Account", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password (min 8 chars)") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                when {
                    fullName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() ->
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()

                    password.length < 8 ->
                        Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()

                    password != confirmPassword ->
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()

                    else -> {
                        FirebaseAuth.getInstance()
                            .createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                                    val user = hashMapOf(
                                        "fullName" to fullName,
                                        "email" to email
                                    )
                                    FirebaseFirestore.getInstance()
                                        .collection("users")
                                        .document(userId ?: "")
                                        .set(user)
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Signup successful!", Toast.LENGTH_SHORT).show()
                                            navController.navigate("home")
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(context, "Firestore error: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }

                                } else {
                                    Toast.makeText(context, "Signup failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Already have an account? Login")
        }
    }
}
