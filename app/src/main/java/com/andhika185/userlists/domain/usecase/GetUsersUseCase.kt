package com.andhika185.userlists.domain.usecase

import com.andhika185.userlists.domain.repository.UserRepository

class GetUsersUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.getUsers()
}