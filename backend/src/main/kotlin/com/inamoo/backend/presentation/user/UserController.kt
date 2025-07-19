package com.inamoo.backend.presentation.user

import com.inamoo.backend.application.user.UserService
import com.inamoo.backend.domain.user.User
import com.inamoo.backend.presentation.user.dto.UpdateUserRequest
import com.inamoo.backend.presentation.user.dto.ExtraUserInfoRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping
    fun getAllUsers(): List<User> {
        return userService.getAllUsers()
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): User {
        return userService.getUserById(id)
    }

    @PatchMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody request: UpdateUserRequest
    ): User {
        return userService.updateUser(id, request)
    }

    @PostMapping("/extra-info")
    fun saveExtraUserInfo(@RequestBody request: ExtraUserInfoRequest): User {
        return userService.saveExtraUserInfo(request)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUserById(id)
    }
}
