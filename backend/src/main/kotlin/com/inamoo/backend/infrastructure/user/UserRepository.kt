package com.inamoo.backend.infrastructure.user

import com.inamoo.backend.infrastructure.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByGoogleId(googleId: String): UserEntity?
    fun findByEmail(email: String): UserEntity?
}
