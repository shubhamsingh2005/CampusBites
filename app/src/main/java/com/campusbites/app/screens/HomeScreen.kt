package com.campusbites.app.screens

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.campusbites.app.R
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    // Initialize MapLibre
    LaunchedEffect(Unit) {
        try {
            MapLibre.getInstance(context)
        } catch (_: Exception) {}
    }

    val mapView = rememberMapViewWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_appicon),
                            contentDescription = "App Icon",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .padding(end = 8.dp)
                        )

                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search...") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = "Search Icon")
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(30),
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("profile")
                    }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val buttonModifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .padding(horizontal = 4.dp)

                    val buttonColors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )

                    listOf(
                        Triple(Icons.Default.Home, "Home", "home"),
                        Triple(Icons.Default.Menu, "Categories", "categories"),
                        Triple(Icons.Default.Favorite, "Orders", "orders"),      // 🛠 implement if needed
                        Triple(Icons.Default.LocalMall, "Mirchibox", "mirchibox") // 🛠 implement if needed
                    ).forEach { (icon, label, route) ->
                        IconButton(
                            onClick = { navController.navigate(route) },
                            modifier = buttonModifier,
                            colors = buttonColors
                        ) {
                            Icon(icon, contentDescription = label)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            AndroidView(factory = {
                mapView.apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    getMapAsync { map ->
                        map.setStyle(
                            Style.Builder().fromUri(
                                "https://api.maptiler.com/maps/019796ae-d8be-7738-b932-604a13f5533c/style.json?key=tIFbJDNYEOgeiXz9UmsD"
                            )
                        ) {
                            val punjabLatLng = LatLng(31.1471, 75.3412)
                            val cameraPosition = CameraPosition.Builder()
                                .target(punjabLatLng)
                                .zoom(9.5)
                                .build()
                            map.cameraPosition = cameraPosition
                        }
                    }
                }
            })
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val mapView = remember {
        MapView(context).apply {
            onCreate(null)
        }
    }

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }

        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    return mapView
}

