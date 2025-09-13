package com.andhika185.userlists.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andhika185.userlists.domain.usecase.GetUserByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<UserDetailState>(UserDetailState.Loading)
    val state: StateFlow<UserDetailState> = _state

    init {
        savedStateHandle.get<Int>("userId")?.let { userId ->
            fetchUserDetail(userId)
        } ?: run {
            _state.value = UserDetailState.Error("User ID not found")
        }
    }

    private fun fetchUserDetail(userId: Int) {
        viewModelScope.launch {
            _state.value = UserDetailState.Loading
            getUserByIdUseCase(userId)
                .onSuccess { user ->
                    _state.value = UserDetailState.Success(user)
                }
                .onFailure { error ->
                    _state.value = UserDetailState.Error(error.message ?: "An unknown error occurred")
                }
        }
    }
}