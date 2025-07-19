package com.inamoo.backend.presentation.user

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/api/sms")
class SmsController(
    @Value("\${naver.sens.accessKey}") private val accessKey: String,
    @Value("\${naver.sens.secretKey}") private val secretKey: String,
    @Value("\${naver.sens.serviceId}") private val serviceId: String,
    @Value("\${naver.sens.sender}") private val sender: String
) {
    private val codeStore = ConcurrentHashMap<String, Pair<String, Long>>() // phone -> (code, expireTime)

    @PostMapping("/send")
    fun sendSms(@RequestBody req: SendSmsRequest): ResponseEntity<Any> {
        val code = (100000..999999).random().toString()
        val expire = Instant.now().epochSecond + 180 // 3분 유효
        codeStore[req.phone] = code to expire

        // Naver SENS SMS API 호출
        val url = "https://sens.apigw.ntruss.com/sms/v2/services/$serviceId/messages"
        val timestamp = System.currentTimeMillis().toString()
        val body = mapOf(
            "type" to "SMS",
            "from" to sender,
            "content" to "[인증번호] $code",
            "messages" to listOf(mapOf("to" to req.phone))
        )
        val headers = mutableMapOf(
            "Content-Type" to "application/json; charset=utf-8",
            "x-ncp-apigw-timestamp" to timestamp,
            "x-ncp-iam-access-key" to accessKey,
            "x-ncp-apigw-signature-v2" to makeSignature(timestamp, accessKey, secretKey, serviceId)
        )
        val restTemplate = RestTemplate()
        val entity = org.springframework.http.HttpEntity(body, org.springframework.http.HttpHeaders().apply {
            headers.forEach { (k, v) -> add(k, v) }
        })
        val resp = restTemplate.postForEntity(url, entity, String::class.java)
        return ResponseEntity.ok(mapOf("result" to "sent"))
    }

    @PostMapping("/verify")
    fun verifyCode(@RequestBody req: VerifySmsRequest): ResponseEntity<Any> {
        val (code, expire) = codeStore[req.phone] ?: return ResponseEntity.badRequest().body(mapOf("result" to "fail", "reason" to "no_code"))
        if (Instant.now().epochSecond > expire) {
            codeStore.remove(req.phone)
            return ResponseEntity.badRequest().body(mapOf("result" to "fail", "reason" to "expired"))
        }
        if (req.code == code) {
            codeStore.remove(req.phone)
            return ResponseEntity.ok(mapOf("result" to "success"))
        }
        return ResponseEntity.badRequest().body(mapOf("result" to "fail", "reason" to "wrong_code"))
    }

    private fun makeSignature(timestamp: String, accessKey: String, secretKey: String, serviceId: String): String {
        // 실제 구현에서는 HMAC-SHA256 서명 생성 필요 (네이버 SENS 문서 참고)
        // 여기서는 예시로 빈 문자열 반환
        return ""
    }
}

data class SendSmsRequest(val phone: String)
data class VerifySmsRequest(val phone: String, val code: String)
