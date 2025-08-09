package com.campusbites.app.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(navController: NavHostController) {
    val blocks = listOf(
        "Block A1 Front", "Block A1", "Block A3", "Block B2", "Block B1 Front",
        "Block B4", "Block C1", "Food Republic FR", "Block C3"
    )

    val blockA1FrontShops = listOf("X Burgers", "Mummy di Roti", "Chef's on Fire", "Singh Bakers")
    val blockB2Shops = listOf("The Corner Cafe")

    var selectedBlock by remember { mutableStateOf(blocks[0]) }
    var isSidebarOpen by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categories", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = { isSidebarOpen = !isSidebarOpen }) {
                        Icon(Icons.Default.Menu, contentDescription = "Toggle Sidebar")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                    }
                    IconButton(onClick = { navController.navigate("cart") }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
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
                        Icons.Default.Home to "Home",
                        Icons.Default.Menu to "Categories",
                        Icons.Default.Favorite to "Orders",
                        Icons.Default.LocalMall to "Mirchibox"
                    ).forEach { (icon, label) ->
                        IconButton(
                            onClick = {
                                when (label) {
                                    "Home" -> navController.navigate("home") {
                                        popUpTo("categories") { inclusive = true }
                                    }
                                    "Categories" -> {}
                                    "Orders" -> navController.navigate("orders")
                                    "Mirchibox" -> navController.navigate("mirchibox")
                                }
                            },
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
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (isSidebarOpen) {
                Column(
                    modifier = Modifier
                        .width(180.dp)
                        .fillMaxHeight()
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    blocks.forEach { block ->
                        Surface(
                            onClick = { selectedBlock = block },
                            shape = RoundedCornerShape(12.dp),
                            color = if (block == selectedBlock) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            tonalElevation = 2.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Text(
                                text = block,
                                modifier = Modifier.padding(12.dp),
                                color = if (block == selectedBlock) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Content Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                val currentShops = when (selectedBlock) {
                    "Block A1 Front" -> blockA1FrontShops
                    "Block B2" -> blockB2Shops
                    else -> null
                }

                currentShops?.chunked(3)?.forEachIndexed { chunkIndex, rowShops ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        rowShops.forEachIndexed { shopIndex, shop ->
                            val shopId = chunkIndex * 3 + shopIndex
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        navController.navigate("shop/$shopId")
                                    }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Store,
                                        contentDescription = shop,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = shop,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        repeat(3 - rowShops.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                } ?: Text(
                    "Shops in $selectedBlock will appear here.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
