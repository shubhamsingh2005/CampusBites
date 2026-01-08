package com.campusbites.app.model

data class Shop(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String
)

data class MenuItem(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double
)
