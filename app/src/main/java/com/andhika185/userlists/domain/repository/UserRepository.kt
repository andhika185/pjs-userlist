package com.andhika185.userlists.domain.repository

import com.andhika185.userlists.domain.model.User

interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
    suspend fun getUserById(userId: Int): Result<User>
}