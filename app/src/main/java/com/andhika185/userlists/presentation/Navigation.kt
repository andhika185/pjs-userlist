package com.andhika185.userlists.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.andhika185.userlists.presentation.detail.UserDetailScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "user_list") {
        composable("user_list") {
            UserListScreen(
                onUserClick = { userId ->
                    // Aksi navigasi ke layar detail
                    navController.navigate("user_detail/$userId")
                }
            )
        }
        composable(
            route = "user_detail/{userId}", // Rute dengan argumen
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) {
            UserDetailScreen(
                onNavigateUp = { navController.navigateUp() } // Aksi untuk kembali
            )
        }
    }
}