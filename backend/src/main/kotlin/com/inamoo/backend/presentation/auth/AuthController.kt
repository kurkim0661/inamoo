package com.inamoo.backend.presentation.auth

import com.inamoo.backend.application.user.UserService
import com.inamoo.backend.domain.user.User
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService
) {
    data class GoogleLoginRequest(val idToken: String)
    data class GoogleLoginResponse(val user: User)

    @PostMapping("/google")
    fun googleLogin(@RequestBody request: GoogleLoginRequest): GoogleLoginResponse {
        val user = userService.authenticateWithGoogle(request.idToken)
        return GoogleLoginResponse(user)
    }
}
