package com.inamoo.backend.infrastructure.product

import jakarta.persistence.*

@Entity
@Table(name = "products")
class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = true)
    var description: String? = null,

    @Column(nullable = false)
    var price: Int = 0,

    @Column(nullable = true)
    var imageUrl: String? = null,

    @Column(nullable = false)
    var category: String = "",

    @Column(nullable = false, updatable = false)
    var createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: java.time.LocalDateTime = java.time.LocalDateTime.now(),

    @Column(nullable = true)
    var userId: Long? = null
)
