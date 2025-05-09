package kim.wonung.security_sample.domain.model

import java.time.Instant

/**
 * 사용자 도메인 모델
 */
data class User(
    val id: Long? = null,
    val username: String,
    val email: String,
    val password: String,
    val roles: Set<Role> = setOf(Role.USER),
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

enum class Role {
    USER, ADMIN
}
