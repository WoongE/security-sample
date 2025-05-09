package kim.wonung.security_sample.adapter.inbound.security

import kim.wonung.security_sample.domain.port.UserRepositoryPort
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepositoryPort: UserRepositoryPort
) : UserDetailsService {
    
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepositoryPort.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")
        
        val authorities = user.roles.map { SimpleGrantedAuthority("ROLE_${it.name}") }
        
        return User.builder()
            .username(user.username)
            .password(user.password)
            .authorities(authorities)
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build()
    }
}
