package kim.wonung.security_sample.adapter.outbound.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import kim.wonung.security_sample.domain.port.JwtPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtAdapter(
    @Value("\${jwt.secret}")
    private val secret: String,

    @Value("\${jwt.expiration}")
    private val expirationTime: Long
) : JwtPort {

    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray())
    }

    override fun generateToken(username: String): String {
        val now = Date()
        val expiration = Date(now.time + expirationTime)

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(key)
            .compact()
    }

    override fun validateToken(token: String): Boolean {
        return try {
            val claims = getClaims(token)
            !claims.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    override fun getUsernameFromToken(token: String): String {
        return getClaims(token).subject
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}
