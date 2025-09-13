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

    // State untuk menampung hasil mentah dari API (loading, success, error)
    private val _apiState = MutableStateFlow<UserListState>(UserListState.Loading)

    // State untuk menampung query pencarian dari UI
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // State final yang akan diobservasi oleh UI.
    // Ini adalah gabungan (combine) dari hasil API dan query pencarian.
    val state: StateFlow<UserListState> = combine(_apiState, _searchQuery) { apiState, query ->
        when (apiState) {
            is UserListState.Success -> {
                val filteredList = if (query.isBlank()) {
                    apiState.users // Jika query kosong, tampilkan semua
                } else {
                    // Jika ada query, filter berdasarkan nama depan atau belakang
                    apiState.users.filter { user ->
                        val fullName = "${user.firstName} ${user.lastName}"
                        fullName.contains(query, ignoreCase = true)
                    }
                }
                UserListState.Success(filteredList)
            }
            // Untuk state Loading dan Error, langsung teruskan saja
            else -> apiState
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L), // Tahan state selama 5 detik
        initialValue = UserListState.Loading
    )

    init {
        fetchUsers()
    }

    // Fungsi ini sekarang hanya fokus mengambil data dan mengisi _apiState
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

    // Fungsi publik yang akan dipanggil oleh UI saat teks pencarian berubah
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}