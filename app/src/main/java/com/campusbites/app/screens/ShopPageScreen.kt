package com.campusbites.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.campusbites.app.viewmodel.CartViewModel
import com.campusbites.app.viewmodel.ShopPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopPageScreen(
    navController: NavController,
    shopId: Long,
    shopPageViewModel: ShopPageViewModel = viewModel(),
    cartViewModel: CartViewModel
) {
    val context = LocalContext.current
    val menu by shopPageViewModel.menu.collectAsState()

    LaunchedEffect(shopId) {
        shopPageViewModel.fetchMenu(context, shopId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(menu) { menuItem ->
                Card {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(menuItem.name, style = MaterialTheme.typography.titleMedium)
                            Text(menuItem.description, style = MaterialTheme.typography.bodyMedium)
                            Text("₹${menuItem.price}", style = MaterialTheme.typography.bodyLarge)
                        }
                        Button(onClick = { cartViewModel.addToCart(menuItem) }) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}
