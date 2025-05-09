package kim.wonung.security_sample.application.service

import kim.wonung.security_sample.domain.model.Role
import kim.wonung.security_sample.domain.model.User
import kim.wonung.security_sample.domain.port.JwtPort
import kim.wonung.security_sample.domain.port.PasswordEncoderPort
import kim.wonung.security_sample.domain.port.UserRepositoryPort
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepositoryPort: UserRepositoryPort,
    private val passwordEncoderPort: PasswordEncoderPort,
    private val jwtPort: JwtPort
) {
    /**
     * 회원가입 기능
     */
    fun signUp(username: String, email: String, password: String): User {
        // 중복 체크
        if (userRepositoryPort.existsByUsername(username)) {
            throw IllegalArgumentException("Username is already taken")
        }
        
        if (userRepositoryPort.existsByEmail(email)) {
            throw IllegalArgumentException("Email is already in use")
        }
        
        // 비밀번호 암호화
        val encodedPassword = passwordEncoderPort.encode(password)
        
        // 사용자 생성 및 저장
        val user = User(
            username = username,
            email = email,
            password = encodedPassword
        )
        
        return userRepositoryPort.save(user)
    }
    
    /**
     * 관리자 계정 생성 (테스트/개발용)
     */
    fun createAdminUser(username: String, email: String, password: String): User {
        // 중복 체크
        if (userRepositoryPort.existsByUsername(username)) {
            throw IllegalArgumentException("Username is already taken")
        }
        
        if (userRepositoryPort.existsByEmail(email)) {
            throw IllegalArgumentException("Email is already in use")
        }
        
        // 비밀번호 암호화
        val encodedPassword = passwordEncoderPort.encode(password)
        
        // 관리자 사용자 생성 및 저장
        val user = User(
            username = username,
            email = email,
            password = encodedPassword,
            roles = setOf(Role.USER, Role.ADMIN)
        )
        
        return userRepositoryPort.save(user)
    }
    
    /**
     * 로그인 기능
     */
    fun signIn(username: String, password: String): String {
        val user = userRepositoryPort.findByUsername(username)
            ?: throw IllegalArgumentException("Invalid username or password")
        
        if (!passwordEncoderPort.matches(password, user.password)) {
            throw IllegalArgumentException("Invalid username or password")
        }
        
        // JWT 토큰 생성
        return jwtPort.generateToken(username)
    }
    
    /**
     * 사용자 조회
     */
    fun getUserByUsername(username: String): User? {
        return userRepositoryPort.findByUsername(username)
    }
    
    /**
     * 모든 사용자 조회 (관리자 전용)
     */
    fun getAllUsers(): List<User> {
        return userRepositoryPort.findAll()
    }
}
