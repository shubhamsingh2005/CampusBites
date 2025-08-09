package com.campusbites.app.models

data class Shop(
    val id: Int,
    val name: String,
    val tagline: String,
    val isOpen: Boolean,
    val contact: String,
    val images: List<String>,
    val rating: Double,
    val reviews: Int
)