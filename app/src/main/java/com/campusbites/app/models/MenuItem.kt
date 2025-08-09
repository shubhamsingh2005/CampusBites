package com.campusbites.app.models

data class MenuItem(
    val id: Int,
    val shopId: Int,
    val name: String,
    val type: String, // veg / non-veg
    val category: String,
    val price: Double,
    val imageUrl: String,
    val ingredients: String
)