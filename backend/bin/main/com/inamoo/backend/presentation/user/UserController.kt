package com.inamoo.backend.presentation.user

import com.inamoo.backend.application.user.UserService
import com.inamoo.backend.domain.user.User
import com.inamoo.backend.presentation.user.dto.UpdateUserRequest
import com.inamoo.backend.presentation.user.dto.ExtraUserInfoRequest
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService
) {
    @GetMapping("/users")
    fun getAllUsers(): List<User> {
        return userService.getAllUsers()
    }

    @PatchMapping("/users/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody request: UpdateUserRequest
    ): User {
        return userService.updateUser(id, request)
    }

    @PostMapping("/users/extra-info")
    fun saveExtraUserInfo(@RequestBody request: ExtraUserInfoRequest): User {
        return userService.saveExtraUserInfo(request)
    }
}
