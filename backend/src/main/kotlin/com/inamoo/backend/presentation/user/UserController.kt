package com.inamoo.backend.presentation.user

import com.inamoo.backend.application.user.UserService
import com.inamoo.backend.domain.user.User
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService
) {
    @GetMapping("/users")
    fun getAllUsers(): List<User> {
        return userService.getAllUsers()
    }

    // 추가 정보 저장용 DTO
    data class ExtraUserInfoRequest(
        val userId: Long,
        val nickname: String
    )

    @PostMapping("/users/extra-info")
    fun saveExtraUserInfo(@RequestBody request: ExtraUserInfoRequest): User {
        return userService.saveExtraUserInfo(request.userId, request.nickname)
    }
}
