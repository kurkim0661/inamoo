package com.inamoo.backend.infrastructure.user

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var googleId: String = "",

    @Column(nullable = false, unique = true)
    var email: String = "",

    @Column(nullable = true)
    var name: String? = null,

    @Column(nullable = true)
    var profileImage: String? = null,

    @Column(nullable = false, unique = true)
    var nickname: String = "",

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
