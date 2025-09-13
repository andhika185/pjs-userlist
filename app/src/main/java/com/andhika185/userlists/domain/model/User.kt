package com.andhika185.userlists.domain.model

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val imageUrl: String,
    // Properti baru
    val birthDate: String,
    val phone: String,
    val email: String,
    val companyName: String,
    val companyTitle: String
)