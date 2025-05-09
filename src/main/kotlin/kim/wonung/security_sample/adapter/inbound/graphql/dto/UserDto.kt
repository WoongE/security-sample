package kim.wonung.security_sample.adapter.inbound.graphql.dto

import kim.wonung.security_sample.domain.model.User

/**
 * GraphQL 응답용 사용자 DTO
 */
data class UserDto(
    val id: String,
    val username: String,
    val email: String,
    val roles: List<String>,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun fromDomain(user: User): UserDto {
            return UserDto(
                id = user.id.toString(),
                username = user.username,
                email = user.email,
                roles = user.roles.map { it.name },
                createdAt = user.createdAt.toString(),
                updatedAt = user.updatedAt.toString()
            )
        }
    }
}

/**
 * 회원가입 요청 DTO
 */
data class SignUpInput(
    val username: String,
    val email: String,
    val password: String
)

/**
 * 로그인 요청 DTO
 */
data class SignInInput(
    val username: String,
    val password: String
)

/**
 * 인증 토큰 응답 DTO
 */
data class AuthTokenDto(
    val token: String,
    val tokenType: String = "Bearer"
)
