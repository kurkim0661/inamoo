package com.inamoo.backend.presentation.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.inamoo.backend.infrastructure.user.UserEntity
import com.inamoo.backend.infrastructure.user.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val userRepository: UserRepository,
    val objectMapper: ObjectMapper
) {
    lateinit var user: UserEntity

    @BeforeEach
    fun setup() {
        userRepository.deleteAll()
        user = userRepository.save(
            UserEntity(
                googleId = "test-google-id",
                email = "test@example.com",
                password = "oldpassword",
                name = "홍길동",
                profileImage = "",
                nickname = "tester"
            )
        )
    }

    @Test
    fun `개인정보 수정 성공`() {
        val updateRequest = mapOf(
            "email" to "new@example.com",
            "password" to "newpassword",
            "name" to "김철수",
            "profileImage" to "http://img.com/profile.png",
            "nickname" to "newnick",
            "phoneNumber" to "01012345678",
            "address" to "서울시 강남구",
            "birthDate" to "1990-01-01"
        )
        mockMvc.perform(
            patch("/api/users/${user.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value("new@example.com"))
            .andExpect(jsonPath("$.name").value("김철수"))
            .andExpect(jsonPath("$.nickname").value("newnick"))
            .andExpect(jsonPath("$.phoneNumber").value("01012345678"))
            .andExpect(jsonPath("$.address").value("서울시 강남구"))
            .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
    }
}
