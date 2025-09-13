package com.andhika185.userlists.data.remote

import com.andhika185.userlists.data.remote.dto.UserListResponse
import com.andhika185.userlists.data.remote.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("users")
    suspend fun getUsers(): UserListResponse
    @GET("users/{id}") // Endpoint baru
    suspend fun getUserById(@Path("id") userId: Int): UserDto
}