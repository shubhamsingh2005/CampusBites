package com.campusbites.app.models

data class OrderItem(
    val id: Int,
    val name: String,
    val price: Double,
    val quantity: Int
)