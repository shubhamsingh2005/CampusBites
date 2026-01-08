package com.campusbites.app.model

data class AuthRequest(
    val fullName: String? = null,
    val email: String,
    val password: String
)
