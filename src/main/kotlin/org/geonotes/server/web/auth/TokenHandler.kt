package org.geonotes.server.web.auth

import java.util.*
import java.time.LocalDateTime
import java.time.ZoneId
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Claims

import org.geonotes.server.logger
import org.geonotes.server.core.User

@Service
class TokenHandler {
    data class TokenInfo(val username: String, val password: String, val expirationTime: LocalDateTime)

    fun generateToken(user: User): String? {
        try {
            return Jwts.builder()
                .setId(user.username)
                .addClaims(mapOf("password" to user.password))
                .setExpiration(
                    Date.from(
                        LocalDateTime.now().plusHours(tokenValidityDuration!!)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                    )
                )
                .signWith(secretKey)
                .compact()
        } catch (e: Exception) {
            logger.error("Generation of token for '${user.username}' failed; Message: ${e.message}")
        }
        return null
    }

    fun extractTokenInfo(token: String): TokenInfo? {
        try {
            val claimsJws: Jws<Claims> = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            return TokenInfo(claimsJws.body.id,
                claimsJws.body["password"] as String,
                claimsJws.body.expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
        } catch (e: Exception) {
            logger.debug("Error while extracting token info. Message: ${e.message}")
        }
        return null
    }

    @Value("\${api.jwt.tokenValidityDuration}")
    private var tokenValidityDuration: Long? = null

    @Value("\${api.jwt.encodedSecret}")
    private lateinit var encodedSecretKey: String

    private val secretKey: SecretKey by lazy {
        val decode = Base64.getDecoder().decode(encodedSecretKey)
        SecretKeySpec(decode, 0, decode.size, "HmacSHA512")
    }

    private val logger: Logger by logger()
}