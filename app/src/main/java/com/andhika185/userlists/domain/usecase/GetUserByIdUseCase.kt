package com.andhika185.userlists.domain.usecase

import com.andhika185.userlists.domain.repository.UserRepository

class GetUserByIdUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: Int) = userRepository.getUserById(userId)
}