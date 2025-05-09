package kim.wonung.security_sample.adapter.outbound.persistence

import kim.wonung.security_sample.adapter.outbound.persistence.table.UserRoles
import kim.wonung.security_sample.adapter.outbound.persistence.table.Users
import kim.wonung.security_sample.domain.model.Role
import kim.wonung.security_sample.domain.model.User
import kim.wonung.security_sample.domain.port.UserRepositoryPort
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class UserRepositoryAdapter : UserRepositoryPort {

    override fun save(user: User): User {
        return transaction {
            val userId = if (user.id != null) {
                // Update existing user
                Users.update({ Users.id eq user.id }) {
                    it[username] = user.username
                    it[email] = user.email
                    it[password] = user.password
                    it[updatedAt] = Instant.now()
                }

                // Delete and re-create roles
                UserRoles.deleteWhere { UserRoles.userId eq user.id }

                // Insert roles
                user.roles.forEach { role ->
                    UserRoles.insert {
                        it[UserRoles.userId] = user.id
                        it[UserRoles.role] = role
                    }
                }

                user.id
            } else {
                // Insert new user
                val insertStatement = Users.insert {
                    it[username] = user.username
                    it[email] = user.email
                    it[password] = user.password
                    it[createdAt] = user.createdAt
                    it[updatedAt] = user.updatedAt
                }

                val newId = insertStatement[Users.id].value

                // Insert roles
                user.roles.forEach { role ->
                    UserRoles.insert {
                        it[UserRoles.userId] = newId
                        it[UserRoles.role] = role
                    }
                }

                newId
            }

            // Return updated user
            findById(userId)!!
        }
    }

    override fun findById(id: Long): User? {
        return transaction {
            Users.select(Users.columns)
                .where { Users.id eq id }
                .limit(1)
                .singleOrNull()
                ?.let { resultRow -> mapToUser(resultRow) }
        }
    }

    override fun findByUsername(username: String): User? {
        return transaction {
            Users.select(Users.columns)
                .where { Users.username eq username }
                .limit(1)
                .singleOrNull()
                ?.let { resultRow -> mapToUser(resultRow) }
        }
    }

    override fun findByEmail(email: String): User? {
        return transaction {
            Users.select(Users.columns)
                .where { Users.email eq email }
                .limit(1)
                .singleOrNull()
                ?.let { resultRow -> mapToUser(resultRow) }
        }
    }

    override fun findAll(): List<User> {
        return transaction {
            Users.selectAll().map { resultRow -> mapToUser(resultRow) }
        }
    }

    override fun existsByUsername(username: String): Boolean {
        return transaction {
            Users.select(Users.id)
                .where { Users.username eq username }
                .count() > 0
        }
    }

    override fun existsByEmail(email: String): Boolean {
        return transaction {
            Users.select(Users.id)
                .where { Users.email eq email }
                .count() > 0
        }
    }

    private fun mapToUser(row: ResultRow): User {
        val userId = row[Users.id].value

        // 해당 사용자의 권한 조회
        val roles = UserRoles.select(UserRoles.role)
            .where { UserRoles.userId eq userId }
            .map { it[UserRoles.role] }
            .toSet()

        return User(
            id = userId,
            username = row[Users.username],
            email = row[Users.email],
            password = row[Users.password],
            roles = roles.ifEmpty { setOf(Role.USER) },
            createdAt = row[Users.createdAt],
            updatedAt = row[Users.updatedAt]
        )
    }
}