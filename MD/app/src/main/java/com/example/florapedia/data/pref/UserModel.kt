package com.example.florapedia.data.pref

data class UserModel(
    val email: String,
    val username: String,
    val token: String,
    val isLogin: Boolean
)