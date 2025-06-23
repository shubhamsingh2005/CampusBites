package com.campusbites.app.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.campusbites.app.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

@Composable
fun PhoneAuthScreen(navController: NavHostController) {
    val context = LocalContext.current
    val firebaseAuth = FirebaseAuth.getInstance()
    val db = Firebase.firestore

    var phoneNumber by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var isVerifying by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        // Handle intent result if needed
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = context.getString(R.string.login_via_otp), style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            placeholder = { Text("+91xxxxxxxxxx") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(context as Activity)
                    .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                            firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    navController.navigate("home")
                                }
                            }
                        }

                        override fun onVerificationFailed(e: FirebaseException) {
                            Toast.makeText(context, "Verification Failed: ${e.message}", Toast.LENGTH_LONG).show()
                        }

                        override fun onCodeSent(
                            verifId: String,
                            token: PhoneAuthProvider.ForceResendingToken
                        ) {
                            verificationId = verifId
                            isVerifying = true
                        }
                    }).build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = phoneNumber.length >= 10
        ) {
            Text("Send OTP")
        }

        if (isVerifying) {
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = verificationCode,
                onValueChange = { verificationCode = it },
                label = { Text("Enter OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val credential = PhoneAuthProvider.getCredential(verificationId!!, verificationCode)
                    firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = task.result?.user
                                val userData = hashMapOf(
                                    "uid" to user?.uid,
                                    "phone" to user?.phoneNumber,
                                    "name" to "",
                                    "email" to ""
                                )
                                db.collection("users").document(user!!.uid).set(userData)
                                navController.navigate("home")
                            } else {
                                Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Verify")
            }
        }
    }
}
