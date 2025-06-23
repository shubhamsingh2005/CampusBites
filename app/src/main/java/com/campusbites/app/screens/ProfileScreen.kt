package com.campusbites.app.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.campusbites.app.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val auth = Firebase.auth
    val user = auth.currentUser
    val db = Firebase.firestore

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            uploadProfileImageToFirebase(it) { url ->
                imageUri = url
                db.collection("users").document(user?.uid ?: "").update("photoUrl", url)
            }
        }
    }

    // Load profile data from Firestore
    LaunchedEffect(Unit) {
        name = user?.displayName ?: ""
        user?.uid?.let { uid ->
            val doc = db.collection("users").document(uid).get().await()
            name = doc.getString("name") ?: name
            phone = doc.getString("phone") ?: ""
            imageUri = doc.getString("photoUrl") ?: user.photoUrl?.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(130.dp), contentAlignment = Alignment.BottomEnd) {
            Image(
                painter = imageUri?.let { rememberAsyncImagePainter(it) }
                    ?: painterResource(id = R.drawable.ic_appicon),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = name.ifBlank { "Unnamed User" }, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = user?.email ?: "", color = Color.Gray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(32.dp))

        ProfileOption("Edit Profile", Icons.Default.Edit) {
            Toast.makeText(context, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
        }

        ProfileOption("My Orders", Icons.Default.LocalShipping) {
            Toast.makeText(context, "My Orders clicked", Toast.LENGTH_SHORT).show()
        }

        ProfileOption("Help & Support", Icons.Default.Help) {
            Toast.makeText(context, "Help clicked", Toast.LENGTH_SHORT).show()
        }

        ProfileOption("Settings", Icons.Default.Settings) {
            navController.navigate("settings")
        }

        ProfileOption("Logout", Icons.Default.Logout, isDanger = true) {
            FirebaseAuth.getInstance().signOut()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleClient = GoogleSignIn.getClient(context, gso)
            googleClient.signOut().addOnCompleteListener {
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }
    }
}

@Composable
fun ProfileOption(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isDanger: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        tonalElevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onClick() }
                .padding(14.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isDanger) Color.Red else MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                color = if (isDanger) Color.Red else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

fun uploadProfileImageToFirebase(uri: Uri, onSuccess: (String) -> Unit) {
    val storageRef = FirebaseStorage.getInstance()
        .reference
        .child("profile_pics/${System.currentTimeMillis()}.jpg")

    storageRef.putFile(uri).continueWithTask {
        storageRef.downloadUrl
    }.addOnSuccessListener { downloadUri ->
        onSuccess(downloadUri.toString())
    }
}
