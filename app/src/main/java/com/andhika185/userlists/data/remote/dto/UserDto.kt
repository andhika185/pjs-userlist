package com.andhika185.userlists.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserListResponse(
    @SerializedName("users")
    val users: List<UserDto>
)

data class UserDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("age")
    val age: Int,
    @SerializedName("image")
    val image: String,
    // Field baru
    @SerializedName("birthDate")
    val birthDate: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("company")
    val company: CompanyDto
)

data class CompanyDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("title")
    val title: String
)