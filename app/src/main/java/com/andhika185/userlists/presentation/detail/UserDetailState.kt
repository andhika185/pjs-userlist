package com.andhika185.userlists.presentation.detail

import com.andhika185.userlists.domain.model.User

sealed class UserDetailState {
    object Loading : UserDetailState()
    data class Success(val user: User) : UserDetailState()
    data class Error(val message: String) : UserDetailState()
}