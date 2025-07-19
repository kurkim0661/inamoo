package com.inamoo.backend.infrastructure.cart

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.inamoo.backend.infrastructure.user.UserEntity

@Repository
interface CartRepository : JpaRepository<CartEntity, Long> {
    fun findAllByUser(user: UserEntity): List<CartEntity>
}
