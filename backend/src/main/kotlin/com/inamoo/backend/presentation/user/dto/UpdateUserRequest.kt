package com.inamoo.backend.presentation.user.dto

data class UpdateUserRequest(
    val email: String?,
    val password: String?,
    val name: String?,
    val profileImage: String?,
    val nickname: String?,
    val phoneNumber: String?,
    val address: String?,
    val birthDate: String?
)
