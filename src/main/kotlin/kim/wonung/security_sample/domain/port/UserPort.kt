package kim.wonung.security_sample.domain.port

import kim.wonung.security_sample.domain.model.User

/**
 * 사용자 데이터 접근을 위한 포트 (인터페이스)
 */
interface UserRepositoryPort {
    fun save(user: User): User
    fun findById(id: Long): User?
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?
    fun findAll(): List<User>
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}

/**
 * 암호화 서비스를 위한 포트
 */
interface PasswordEncoderPort {
    fun encode(rawPassword: String): String
    fun matches(rawPassword: String, encodedPassword: String): Boolean
}

/**
 * JWT 토큰 생성 및 검증을 위한 포트
 */
interface JwtPort {
    fun generateToken(username: String): String
    fun validateToken(token: String): Boolean
    fun getUsernameFromToken(token: String): String
}
