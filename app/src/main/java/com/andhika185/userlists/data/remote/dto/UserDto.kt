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
    val image: String
)