package com.andhika185.userlists.data.repository

import com.andhika185.userlists.data.remote.ApiService
import com.andhika185.userlists.domain.model.User
import com.andhika185.userlists.domain.repository.UserRepository

class UserRepositoryImpl(private val apiService: ApiService) : UserRepository {

    private fun mapDtoToDomain(dto: com.andhika185.userlists.data.remote.dto.UserDto): User {
        return User(
            id = dto.id,
            firstName = dto.firstName,
            lastName = dto.lastName,
            age = dto.age,
            imageUrl = dto.image,
            birthDate = dto.birthDate,
            phone = dto.phone,
            email = dto.email,
            companyName = dto.company.name,
            companyTitle = dto.company.title
        )
    }

    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val response = apiService.getUsers()
            // Gunakan helper function
            val users = response.users.map { dto -> mapDtoToDomain(dto) }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(userId: Int): Result<User> {
        return try {
            val dto = apiService.getUserById(userId)
            // Gunakan helper function
            val user = mapDtoToDomain(dto)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}