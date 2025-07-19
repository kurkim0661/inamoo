package com.inamoo.backend.application.cart

import org.springframework.stereotype.Service
import com.inamoo.backend.infrastructure.cart.CartRepository
import com.inamoo.backend.infrastructure.user.UserEntity
import com.inamoo.backend.infrastructure.cart.CartEntity

@Service
class CartService(
    private val cartRepository: CartRepository
) {
    fun getCartItemsForUser(user: UserEntity): List<CartEntity> {
        return cartRepository.findAllByUser(user)
    }
}
