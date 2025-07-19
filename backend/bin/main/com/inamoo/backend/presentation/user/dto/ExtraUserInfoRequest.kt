package com.inamoo.backend.presentation.user.dto

data class ExtraUserInfoRequest(
    val userId: Long,
    val nickname: String?,
    val gender: String?,
    val birthDate: String?,
    val address: String?,
    val phoneNumber: String?,
    val profileImage: String?
)
