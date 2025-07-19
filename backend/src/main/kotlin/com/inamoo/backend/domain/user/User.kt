package com.inamoo.backend.domain.user

data class User(
    val id: Long? = null,
    val googleId: String = "",
    val email: String = "",
    val password: String = "",
    val name: String? = null,
    val profileImage: String? = null,
    val nickname: String = "",
    val phoneNumber: String? = null,
    val address: String? = null,
    val birthDate: String? = null // yyyy-MM-dd
)
