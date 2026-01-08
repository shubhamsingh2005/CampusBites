package com.campusbites.app.api

import com.campusbites.app.model.AuthRequest
import com.campusbites.app.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("/api/auth/signup")
    suspend fun signup(@Body authRequest: AuthRequest): Response<String>
}
