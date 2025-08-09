package com.campusbites.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.campusbites.app.models.CartItem
import com.campusbites.app.utils.PriceUtils
import androidx.compose.ui.text.font.FontWeight
import com.campusbites.app.viewmodel.CartViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavHostController, cartViewModel: CartViewModel) {
    var cartItems by remember {
        mutableStateOf(
            mutableListOf(
                CartItem(id = 1, shopId = 101, name = "Cheese Burger", price = 80.0, quantity = 1),
                CartItem(id = 2, shopId = 101, name = "Veg Pizza", price = 120.0, quantity = 2)
            )
        )
    }

    val totalAmount = cartItems.sumOf { it.price * it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                BottomAppBar(
                    modifier = Modifier.height(70.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total: ₹${PriceUtils.format(totalAmount)}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Button(onClick = {
                            // TODO: Checkout flow
                        }) {
                            Text("Checkout")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Your cart is empty.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(cartItems) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Text("₹${item.price}", color = Color.Gray)
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    IconButton(onClick = {
                                        cartItems = cartItems.map {
                                            if (it.id == item.id && it.quantity > 1) it.copy(quantity = it.quantity - 1) else it
                                        }.toMutableList()
                                    }) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                    }

                                    Text("${item.quantity}", modifier = Modifier.width(24.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)

                                    IconButton(onClick = {
                                        cartItems = cartItems.map {
                                            if (it.id == item.id) it.copy(quantity = it.quantity + 1) else it
                                        }.toMutableList()
                                    }) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase")
                                    }
                                }

                                IconButton(onClick = {
                                    cartItems = cartItems.filterNot { it.id == item.id }.toMutableList()
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
