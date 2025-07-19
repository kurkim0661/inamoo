package com.inamoo.backend.presentation.cart.dto

data class CartItemResponse(
    val id: Long,
    val productId: Long,
    val productName: String,
    val price: Int,
    val quantity: Int,
    val imageUrl: String?
)
