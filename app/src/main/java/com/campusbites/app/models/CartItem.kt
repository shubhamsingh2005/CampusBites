package com.campusbites.app.models

data class CartItem(
    val id: Int,                      // Menu item ID
    val shopId: Int,                  // To track which shop this item belongs to
    val name: String,
    val price: Double,
    val quantity: Int = 1,
    val imageUrl: String = "",
    val isVeg: Boolean = true,
    val ingredients: String = ""
)
