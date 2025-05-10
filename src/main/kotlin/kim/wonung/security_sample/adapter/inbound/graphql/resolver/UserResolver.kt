package kim.wonung.security_sample.adapter.inbound.graphql.resolver

import kim.wonung.security_sample.adapter.inbound.graphql.dto.AuthTokenDto
import kim.wonung.security_sample.adapter.inbound.graphql.dto.SignInInput
import kim.wonung.security_sample.adapter.inbound.graphql.dto.SignUpInput
import kim.wonung.security_sample.adapter.inbound.graphql.dto.UserDto
import kim.wonung.security_sample.application.service.UserService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller

@Controller
class UserResolver(
    private val userService: UserService
) {
    @MutationMapping
    fun signUp(@Argument input: SignUpInput): UserDto {
        val user = userService.signUp(
            username = input.username,
            email = input.email,
            password = input.password
        )
        return UserDto.fromDomain(user)
    }

    @MutationMapping
    fun signIn(@Argument input: SignInInput): AuthTokenDto {
        val token = userService.signIn(
            username = input.username,
            password = input.password
        )
        return AuthTokenDto(token = token)
    }

    @QueryMapping
    fun me(@AuthenticationPrincipal userDetails: UserDetails?): UserDto? {
        if (userDetails == null) {
            return null
        }

        val user = userService.getUserByUsername(userDetails.username)
        return user?.let { UserDto.fromDomain(it) }
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun users(): List<UserDto> {
        return userService.getAllUsers().map { UserDto.fromDomain(it) }
    }
}
