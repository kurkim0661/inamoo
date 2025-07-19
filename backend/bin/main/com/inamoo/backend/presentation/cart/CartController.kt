package com.inamoo.backend.presentation.cart

import com.inamoo.backend.application.cart.CartService
import com.inamoo.backend.infrastructure.user.UserRepository
import com.inamoo.backend.presentation.cart.dto.CartItemResponse
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/cart")
class CartController(
    private val cartService: CartService,
    private val userRepository: UserRepository
) {
    @GetMapping
    fun getCartItems(@RequestParam userId: Long): ResponseEntity<List<CartItemResponse>> {
        val user = userRepository.findById(userId).orElse(null)
            ?: return ResponseEntity.notFound().build()
        val cartItems = cartService.getCartItemsForUser(user)
        val response = cartItems.map {
            CartItemResponse(
                id = it.id,
                productId = it.productId,
                productName = it.productName,
                price = it.price,
                quantity = it.quantity,
                imageUrl = it.imageUrl
            )
        }
        return ResponseEntity.ok(response)
    }
}
