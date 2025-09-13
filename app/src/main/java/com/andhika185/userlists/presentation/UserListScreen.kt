package com.andhika185.userlists.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.andhika185.userlists.domain.model.User
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.foundation.layout.Column // Import baru
import androidx.compose.material.icons.Icons // Import baru
import androidx.compose.material.icons.filled.Search // Import baru
import androidx.compose.material3.Icon // Import baru
import androidx.compose.material3.OutlinedTextField // Import baru

@Composable
fun UserListScreen(
    viewModel: UserListViewModel = koinViewModel(),
    onUserClick: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState() // Ambil state query

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) { // Bungkus dengan Column
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChanged, // Panggil fungsi di ViewModel
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text("Search by Name") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search Icon")
                },
                singleLine = true
            )

            // Tampilkan konten berdasarkan state
            when (val currentState = state) {
                is UserListState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UserListState.Success -> {
                    UserList(users = currentState.users, onUserClick = onUserClick)
                }
                is UserListState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error: ${currentState.message}", color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun UserList(users: List<User>, onUserClick: (Int) -> Unit) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    LazyVerticalGrid(
        columns = GridCells.Fixed(if (isPortrait) 1 else 2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(users, key = { it.id }) { user ->
            UserListItem(user = user, onUserClick = onUserClick)
        }
    }
}

@Composable
fun UserListItem(user: User, onUserClick: (Int) -> Unit) {
    val backgroundColor = when {
        user.age <= 30 -> Color(0xFF4CAF50) // Green
        user.age in 31..40 -> Color(0xFFFFEB3B) // Yellow
        else -> Color(0xFFFF9800) // Orange
    }

    // Tentukan warna teks yang kontras
    val contentColor = if (user.age in 31..40) Color.Black else Color.White

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onUserClick(user.id) }
    ) {
        Row(
            modifier = Modifier
                .background(backgroundColor)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.imageUrl,
                contentDescription = "User Image",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = contentColor // Gunakan warna kontras
                )
                Text(
                    text = "Age: ${user.age}",
                    fontSize = 14.sp,
                    color = contentColor.copy(alpha = 0.8f) // Sedikit transparan
                )
                // TAMBAHKAN Text ini untuk menampilkan jabatan
                Text(
                    text = user.companyTitle,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic, // Gaya miring agar terlihat seperti jabatan
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}