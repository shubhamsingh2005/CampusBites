package com.campusbites.app.screens


import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

@Composable
fun PhoneAuthScreen(navController: NavHostController) {
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var storedVerificationId by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(24.dp)) {
        Text("Phone Login", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number (+91XXXXXXXXXX)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(context as ComponentActivity)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(cred: PhoneAuthCredential) {
                        FirebaseAuth.getInstance().signInWithCredential(cred)
                            .addOnSuccessListener { navController.navigate("home") }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        Toast.makeText(context, "Verification Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                        storedVerificationId = verificationId
                        Toast.makeText(context, "OTP Sent", Toast.LENGTH_SHORT).show()
                    }
                }).build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }) {
            Text("Send OTP")
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = { otp = it },
            label = { Text("Enter OTP") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, otp)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnSuccessListener {
                    navController.navigate("home")
                }
                .addOnFailureListener {
                    Toast.makeText(context, "OTP Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }) {
            Text("Verify OTP")
        }
    }
}
