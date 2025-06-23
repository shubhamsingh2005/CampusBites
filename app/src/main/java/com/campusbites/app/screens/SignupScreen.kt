package com.campusbites.app.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.campusbites.app.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit
import androidx.compose.ui.graphics.Color

@Composable
fun SignupScreen(navController: NavHostController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var usePhone by remember { mutableStateOf(false) }
    var verificationId by remember { mutableStateOf("") }
    var agreedToTerms by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val successMessage = remember { mutableStateOf("") }

    LaunchedEffect(successMessage.value) {
        if (successMessage.value.isNotBlank()) {
            snackbarHostState.showSnackbar(successMessage.value)
            successMessage.value = ""
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .widthIn(min = 300.dp, max = 400.dp),
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.create_free_account),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text(stringResource(R.string.full_name)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = { usePhone = false }) {
                        Text(stringResource(R.string.use_email))
                    }
                    TextButton(onClick = { usePhone = true }) {
                        Text(stringResource(R.string.use_phone))
                    }
                }

                if (!usePhone) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(stringResource(R.string.email_label)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text(stringResource(R.string.phone_number_hint)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.password_label)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(stringResource(R.string.confirm_password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                // ✅ Terms & Conditions Checkbox
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(checked = agreedToTerms, onCheckedChange = { agreedToTerms = it })
                    Text(
                        text = stringResource(R.string.i_agree),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                    TextButton(onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://yourdomain.com/privacy")))
                    }) {
                        Text("Privacy Policy", fontSize = MaterialTheme.typography.bodySmall.fontSize)
                    }
                    Text("|", fontSize = MaterialTheme.typography.bodySmall.fontSize)
                    TextButton(onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://yourdomain.com/terms")))
                    }) {
                        Text("Terms", fontSize = MaterialTheme.typography.bodySmall.fontSize)
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (!agreedToTerms) {
                            Toast.makeText(context, "Please agree to Privacy Policy & Terms", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        when {
                            fullName.isBlank() || password.isBlank() || confirmPassword.isBlank() ||
                                    (!usePhone && email.isBlank()) || (usePhone && phone.isBlank()) ->
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.fill_required_fields),
                                    Toast.LENGTH_SHORT
                                ).show()

                            password != confirmPassword ->
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.passwords_do_not_match),
                                    Toast.LENGTH_SHORT
                                ).show()

                            password.length < 8 ->
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.password_too_short),
                                    Toast.LENGTH_SHORT
                                ).show()

                            usePhone && verificationId.isEmpty() -> {
                                val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                                    .setPhoneNumber(phone)
                                    .setTimeout(60L, TimeUnit.SECONDS)
                                    .setActivity(context as ComponentActivity)
                                    .setCallbacks(object :
                                        PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                            FirebaseAuth.getInstance().signInWithCredential(credential)
                                                .addOnSuccessListener {
                                                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                                                    FirebaseFirestore.getInstance().collection("users")
                                                        .document(userId)
                                                        .set(mapOf("fullName" to fullName, "phone" to phone, "signupMethod" to "phone"))
                                                    successMessage.value = context.getString(R.string.successfully_registered)
                                                    navController.navigate("home")
                                                }
                                        }

                                        override fun onVerificationFailed(e: FirebaseException) {
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.otp_failed, e.message),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        override fun onCodeSent(
                                            id: String,
                                            token: PhoneAuthProvider.ForceResendingToken
                                        ) {
                                            verificationId = id
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.otp_sent),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }).build()
                                PhoneAuthProvider.verifyPhoneNumber(options)
                            }

                            usePhone && verificationId.isNotEmpty() -> {
                                val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                                FirebaseAuth.getInstance().signInWithCredential(credential)
                                    .addOnSuccessListener {
                                        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                                        FirebaseFirestore.getInstance().collection("users").document(userId)
                                            .set(mapOf("fullName" to fullName, "phone" to phone, "signupMethod" to "phone"))
                                        successMessage.value = context.getString(R.string.successfully_registered)
                                        navController.navigate("home")
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.otp_failed, it.message),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }

                            !usePhone -> {
                                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val user = FirebaseAuth.getInstance().currentUser
                                            user?.sendEmailVerification()
                                            val userId = user?.uid ?: ""
                                            FirebaseFirestore.getInstance().collection("users").document(userId)
                                                .set(mapOf("fullName" to fullName, "email" to email, "signupMethod" to "email"))
                                            successMessage.value = context.getString(R.string.successfully_registered)
                                            navController.navigate("home")
                                        } else {
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.signup_failed, task.exception?.message),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.signup), color = Color.White)
                }

                Spacer(Modifier.height(8.dp))

                TextButton(onClick = { navController.popBackStack() }) {
                    Text(stringResource(R.string.already_have_account))
                }
            }
        }

        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}
