package com.inamoo.backend.domain.product

data class Product(
    val id: Long? = null,
    val name: String,
    val description: String? = null,
    val price: Int,
    val imageUrl: String? = null,
    val category: String,
    val createdAt: java.time.LocalDateTime? = null,
    val updatedAt: java.time.LocalDateTime? = null,
    val userId: Long? = null
)
