package com.andhika185.userlists.presentation

import com.andhika185.userlists.domain.model.User

sealed class UserListState {
    object Loading : UserListState()
    data class Success(val users: List<User>) : UserListState()
    data class Error(val message: String) : UserListState()
}