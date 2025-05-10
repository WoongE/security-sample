package kim.wonung.security_sample.adapter.inbound.graphql

import kim.wonung.security_sample.adapter.inbound.graphql.resolver.UserResolver
import kim.wonung.security_sample.application.service.UserService
import kim.wonung.security_sample.domain.model.Role
import kim.wonung.security_sample.domain.model.User
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.graphql.test.tester.GraphQlTester
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant

@GraphQlTest(UserResolver::class)
class UserResolverTest {

    @Autowired
    private lateinit var graphQlTester: GraphQlTester

    @MockitoBean
    private lateinit var userService: UserService

    @Test
    @WithMockUser(username = "testuser")
    fun shouldReturnCurrentUser() {
        // Given
        val testUser = User(
            id = 1L,
            username = "testuser",
            email = "test@example.com",
            password = "encoded_password",
            roles = setOf(Role.USER),
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        `when`(userService.getUserByUsername("testuser")).thenReturn(testUser)

        // When/Then
        graphQlTester.document(
            """
            query {
                me {
                    id
                    username
                    email
                    roles
                }
            }
        """
        )
            .execute()
            .path("me.username").entity(String::class.java).isEqualTo("testuser")
            .path("me.email").entity(String::class.java).isEqualTo("test@example.com")
            .path("me.roles").entityList(String::class.java).contains("USER")
    }
}
