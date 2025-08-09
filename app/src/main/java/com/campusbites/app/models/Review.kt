package com.campusbites.app.models

data class Review(
    val id: Int,
    val userId: String,
    val shopId: Int,
    val rating: Int,
    val comment: String,
    val createdAt: String
)