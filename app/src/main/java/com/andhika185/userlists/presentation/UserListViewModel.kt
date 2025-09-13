package com.andhika185.userlists.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andhika185.userlists.domain.model.User
import com.andhika185.userlists.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserListViewModel(private val getUsersUseCase: GetUsersUseCase) : ViewModel() {

    private val _apiState = MutableStateFlow<UserListState>(UserListState.Loading)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // 1. State baru untuk menampung order pengurutan
    private val _sortOrder = MutableStateFlow(SortOrder.NONE)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    // 2. Tambahkan _sortOrder ke dalam combine
    val state: StateFlow<UserListState> = combine(
        _apiState,
        _searchQuery,
        _sortOrder
    ) { apiState, query, sortOrder ->
        when (apiState) {
            is UserListState.Success -> {
                // Langkah 1: Filtering (sama seperti sebelumnya)
                val filteredList = if (query.isBlank()) {
                    apiState.users
                } else {
                    apiState.users.filter { user ->
                        val fullName = "${user.firstName} ${user.lastName}"
                        fullName.contains(query, ignoreCase = true)
                    }
                }

                // Langkah 2: Sorting (logika baru)
                val sortedList = when (sortOrder) {
                    SortOrder.ASCENDING -> filteredList.sortedBy { "${it.firstName} ${it.lastName}" }
                    SortOrder.DESCENDING -> filteredList.sortedByDescending { "${it.firstName} ${it.lastName}" }
                    SortOrder.NONE -> filteredList // Tidak melakukan sorting
                }

                UserListState.Success(sortedList)
            }
            else -> apiState
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = UserListState.Loading
    )

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _apiState.value = UserListState.Loading
            getUsersUseCase()
                .onSuccess { users ->
                    _apiState.value = UserListState.Success(users)
                }
                .onFailure { error ->
                    _apiState.value = UserListState.Error(error.message ?: "An unknown error occurred")
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // 3. Fungsi publik untuk mengubah sort order dari UI
    fun onSortOrderChanged(newSortOrder: SortOrder) {
        // Jika tombol yang sama ditekan lagi, reset ke NONE
        _sortOrder.value = if (_sortOrder.value == newSortOrder) SortOrder.NONE else newSortOrder
    }
}