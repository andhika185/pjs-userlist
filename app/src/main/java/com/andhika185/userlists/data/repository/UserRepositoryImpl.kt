package com.andhika185.userlists.data.repository

import com.andhika185.userlists.data.remote.ApiService
import com.andhika185.userlists.domain.model.User
import com.andhika185.userlists.domain.repository.UserRepository

class UserRepositoryImpl(private val apiService: ApiService) : UserRepository {
    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val response = apiService.getUsers()
            val users = response.users.map { dto ->
                User(
                    id = dto.id,
                    firstName = dto.firstName,
                    lastName = dto.lastName,
                    age = dto.age,
                    imageUrl = dto.image
                )
            }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun getUserById(userId: Int): Result<User> {
        return try {
            val dto = apiService.getUserById(userId)
            val user = User(
                id = dto.id,
                firstName = dto.firstName,
                lastName = dto.lastName,
                age = dto.age,
                imageUrl = dto.image
            )
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}