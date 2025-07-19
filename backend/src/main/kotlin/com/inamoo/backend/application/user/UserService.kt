package com.inamoo.backend.application.user

import com.inamoo.backend.domain.user.User
import com.inamoo.backend.infrastructure.user.UserEntity
import com.inamoo.backend.infrastructure.user.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import java.util.Collections

import org.slf4j.LoggerFactory

@Service
class UserService(
    private val userRepository: UserRepository,
    @Value("\${google.oauth.client-id}")
    private val clientId: String
) {
    private val log = LoggerFactory.getLogger(UserService::class.java)
    /**
     * 구글 ID 토큰을 검증하고, 유저 정보를 저장/업데이트 후 User 도메인 객체로 반환합니다.
     * 실제 구글 토큰 검증은 GoogleIdTokenVerifier 등 외부 라이브러리 필요 (의존성 추가 필요).
     */
    @Transactional
    fun authenticateWithGoogle(idToken: String): User {
        log.info("authenticateWithGoogle called")
        val transport = GoogleNetHttpTransport.newTrustedTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()

        val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(Collections.singletonList(clientId))
            .build()

        val idTokenObj: GoogleIdToken? = verifier.verify(idToken)
        if (idTokenObj == null) {
            log.warn("Invalid Google ID token")
            throw IllegalArgumentException("Invalid Google ID token")
        }

        val payload = idTokenObj.payload

        val googleId = payload.subject
        val email = payload.email
        val name = payload["name"] as? String
        val profileImage = payload["picture"] as? String

        log.info("GoogleId: $googleId, email: $email, name: $name, profileImage: $profileImage")

        // DB에서 유저 조회 또는 생성/업데이트
        var userEntity = userRepository.findByGoogleId(googleId)
        val now = LocalDateTime.now()
        var isNewUser = false
        if (userEntity == null) {
            log.info("User not found, creating new user")
            userEntity = UserEntity(
                googleId = googleId,
                email = email,
                name = name,
                profileImage = profileImage,
                nickname = "google_${googleId}_${System.currentTimeMillis()}",
                createdAt = now,
                updatedAt = now
            )
            isNewUser = true
        } else {
            log.info("User found, updating user")
            userEntity.email = email
            userEntity.name = name
            userEntity.profileImage = profileImage
            userEntity.updatedAt = now
        }
        val saved = userRepository.save(userEntity)
        log.info("User saved: id=${saved.id}, googleId=${saved.googleId}, email=${saved.email}")
        return User(
            id = saved.id,
            googleId = saved.googleId,
            email = saved.email,
            name = saved.name,
            profileImage = saved.profileImage,
            nickname = saved.nickname ?: "",
            isNewUser = isNewUser
        )
    }

    /**
     * 사용자 삭제
     */
    @Transactional
    fun deleteUserById(id: Long) {
        userRepository.deleteById(id)
    }

    /**
     * 개인정보 수정
     */
    @Transactional
    fun updateUser(id: Long, request: com.inamoo.backend.presentation.user.dto.UpdateUserRequest): User {
        val userEntity = userRepository.findById(id).orElseThrow { IllegalArgumentException("User not found") }

        request.email?.let { userEntity.email = it }
        request.password?.let { userEntity.password = it } // 실제 서비스에서는 암호화 필요
        request.name?.let { userEntity.name = it }
        request.profileImage?.let { userEntity.profileImage = it }
        request.nickname?.let { userEntity.nickname = it }
        request.phoneNumber?.let { userEntity.phoneNumber = it }
        request.address?.let { userEntity.address = it }
        request.birthDate?.let { userEntity.birthDate = it }
        userEntity.updatedAt = LocalDateTime.now()

        val saved = userRepository.save(userEntity)
        return User(
            id = saved.id,
            googleId = saved.googleId ?: "",
            email = saved.email ?: "",
            password = saved.password ?: "",
            name = saved.name,
            profileImage = saved.profileImage,
            nickname = saved.nickname ?: "",
            phoneNumber = saved.phoneNumber,
            address = saved.address,
            birthDate = saved.birthDate
        )
    }

    /**
     * 전체 유저 목록을 반환합니다.
     */
    @Transactional(readOnly = true)
    fun getAllUsers(): List<User> {
        return userRepository.findAll().map { entity ->
            User(
                id = entity.id,
                googleId = entity.googleId ?: "",
                email = entity.email ?: "",
                name = entity.name,
                profileImage = entity.profileImage,
                nickname = entity.nickname ?: "",
                phoneNumber = entity.phoneNumber,
                address = entity.address,
                birthDate = entity.birthDate,
                gender = entity.gender
            )
        }
    }

    /**
     * 개별 유저 정보 반환
     */
    @Transactional(readOnly = true)
    fun getUserById(id: Long): User {
        val entity = userRepository.findById(id).orElseThrow { IllegalArgumentException("User not found") }
        return User(
            id = entity.id,
            googleId = entity.googleId ?: "",
            password = entity.password ?: "",
            email = entity.email ?: "",
            name = entity.name,
            profileImage = entity.profileImage,
            nickname = entity.nickname ?: "",
            phoneNumber = entity.phoneNumber,
            address = entity.address,
            birthDate = entity.birthDate,
            gender = entity.gender
        )
    }

    /**
     * 추가 정보(닉네임) 저장
     */
    @Transactional
    fun saveExtraUserInfo(request: com.inamoo.backend.presentation.user.dto.ExtraUserInfoRequest): User {
        val userEntity = userRepository.findById(request.userId).orElseThrow { IllegalArgumentException("User not found") }
        request.nickname?.let { userEntity.nickname = it }
        request.gender?.let { userEntity.gender = it }
        request.birthDate?.let { userEntity.birthDate = it }
        request.address?.let { userEntity.address = it }
        request.phoneNumber?.let { userEntity.phoneNumber = it }
        request.profileImage?.let { userEntity.profileImage = it }
        userEntity.updatedAt = LocalDateTime.now()
        val saved = userRepository.save(userEntity)
        return User(
            id = saved.id,
            googleId = saved.googleId ?: "",
            email = saved.email ?: "",
            name = saved.name,
            profileImage = saved.profileImage,
            nickname = saved.nickname ?: "",
            phoneNumber = saved.phoneNumber,
            address = saved.address,
            birthDate = saved.birthDate,
            gender = saved.gender
        )
    }
}
