package kim.wonung.security_sample.adapter.outbound.security

import kim.wonung.security_sample.domain.port.PasswordEncoderPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncoderAdapter(
    private val passwordEncoder: PasswordEncoder
) : PasswordEncoderPort {
    
    override fun encode(rawPassword: String): String {
        return passwordEncoder.encode(rawPassword)
    }
    
    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }
}
