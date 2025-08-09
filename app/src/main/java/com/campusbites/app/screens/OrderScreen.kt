package com.campusbites.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.campusbites.app.models.OrderItem
import com.campusbites.app.utils.PriceUtils

@Composable
fun OrderScreen(orderItems: List<OrderItem>) {
    val total = PriceUtils.calculateTotal(orderItems)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Your Orders", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(orderItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(item.name, style = MaterialTheme.typography.titleMedium)
                        Text("Qty: ${item.quantity}")
                        Text("Price: Rs. ${item.price}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Total: Rs. $total", style = MaterialTheme.typography.titleLarge)
    }
}