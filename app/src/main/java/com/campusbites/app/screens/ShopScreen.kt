package com.campusbites.app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.campusbites.app.R
import com.campusbites.app.models.MenuItem
import com.campusbites.app.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopPageScreen(
    navController: NavHostController,
    shopId: Int,
    cartViewModel: CartViewModel
) {
    val scrollState = rememberScrollState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var isVegOnly by remember { mutableStateOf(false) }
    var isMenuPopupVisible by remember { mutableStateOf(false) }

    val shopName = "X Burgers"
    val tagline = "Taste the legacy of crunch!"
    val isOpen = true
    val contactNumber = "+91-9876543210"
    val avgRating = 4.3
    val totalReviews = 152

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isOpen) "Open" else "Closed",
                color = if (isOpen) Color.Green else Color.Red,
                fontWeight = FontWeight.Bold
            )
            Text(text = shopName, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            IconButton(onClick = { }) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = "Save")
            }
        }

        Text(text = tagline, fontStyle = FontStyle.Italic, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        // Contact
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = {}) {
                Icon(Icons.Default.Map, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Show Map")
            }
            TextButton(onClick = {}) {
                Icon(Icons.Default.Call, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(contactNumber)
            }
        }

        // Shop Images
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            repeat(5) {
                Card(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(200.dp, 140.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_appicon),
                        contentDescription = "Shop Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Ratings
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("⭐ $avgRating / 5", fontWeight = FontWeight.Bold)
            Text("🗨 $totalReviews Reviews", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search & Veg Toggle
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search items...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = isVegOnly, onCheckedChange = { isVegOnly = it })
        }

        // Show Menu
        TextButton(onClick = { isMenuPopupVisible = true }) {
            Icon(Icons.Default.MenuBook, contentDescription = null)
            Text("Show Menu")
        }

        if (isMenuPopupVisible) {
            AlertDialog(
                onDismissRequest = { isMenuPopupVisible = false },
                confirmButton = {},
                text = {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                dismissButton = {
                    IconButton(onClick = { isMenuPopupVisible = false }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            )
        }

        // Filter by Category
        Spacer(modifier = Modifier.height(12.dp))
        Text("Filter By", fontWeight = FontWeight.SemiBold)
        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            listOf("All", "Beverages", "Quick Meals", "Chinese", "Bakery").forEach { category ->
                FilterChip(
                    selected = selectedFilter == category,
                    onClick = { selectedFilter = category },
                    label = { Text(category) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Featured Items
        Text("Featured Items", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            repeat(5) {
                Card(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(160.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Box(
                            modifier = Modifier
                                .height(90.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Burger Deluxe", fontWeight = FontWeight.Bold)
                        Text("Rs. 80")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Price Filters
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("Under Rs. 50", "Under Rs. 100", "Under Rs. 200").forEach { label ->
                AssistChip(onClick = { /* price filter logic */ }, label = { Text(label) })
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // All Items
        Text("All Items", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        repeat(5) { index ->
            val dummyItem = MenuItem(
                id = index,
                shopId = shopId,
                name = "Paneer Tikka Pizza $index",
                type = "veg",
                category = "Pizza",
                price = 120.0,
                ingredients = "Cheese, paneer, spices",
                imageUrl = ""
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(dummyItem.name, fontWeight = FontWeight.Bold)
                        Text(dummyItem.ingredients, fontSize = 12.sp, color = Color.Gray)
                        Text("Rs. ${dummyItem.price}")
                    }
                    Button(onClick = {
                        cartViewModel.addToCart(dummyItem)
                        navController.navigate("cart")
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}
