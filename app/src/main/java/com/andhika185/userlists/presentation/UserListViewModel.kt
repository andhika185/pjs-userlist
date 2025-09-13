package com.andhika185.userlists.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andhika185.userlists.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserListViewModel(private val getUsersUseCase: GetUsersUseCase) : ViewModel() {

    private val _state = MutableStateFlow<UserListState>(UserListState.Loading)
    val state: StateFlow<UserListState> = _state

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _state.value = UserListState.Loading
            getUsersUseCase()
                .onSuccess { users ->
                    _state.value = UserListState.Success(users)
                }
                .onFailure { error ->
                    _state.value = UserListState.Error(error.message ?: "An unknown error occurred")
                }
        }
    }
}