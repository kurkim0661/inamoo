package com.inamoo.backend.infrastructure.cart

import jakarta.persistence.*
import jakarta.persistence.FetchType
import com.inamoo.backend.infrastructure.user.UserEntity

@Entity
@Table(name = "cart_items")
data class CartEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @Column(nullable = false)
    val productId: Long,

    @Column(nullable = false)
    val productName: String,

    @Column(nullable = false)
    val price: Int,

    @Column(nullable = false)
    val quantity: Int,

    @Column(nullable = true)
    val imageUrl: String? = null
)
