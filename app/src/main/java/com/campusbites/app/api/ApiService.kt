package com.campusbites.app.api

import com.campusbites.app.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    // Auth
    @POST("/api/auth/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("/api/auth/signup")
    suspend fun signup(@Body authRequest: AuthRequest): Response<String>

    // Shops
    @GET("/api/shops")
    suspend fun getShops(): Response<List<Shop>>

    // Menu
    @GET("/api/shops/{shopId}/menu")
    suspend fun getMenu(@Path("shopId") shopId: Long): Response<List<MenuItem>>
}
