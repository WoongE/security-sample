package kim.wonung.security_sample.adapter.outbound.persistence.table

import kim.wonung.security_sample.domain.model.Role
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

/**
 * User 테이블 정의
 */
object Users : LongIdTable("users") {
    val username = varchar("username", 255).uniqueIndex()
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val createdAt = timestamp("created_at").default(Instant.now())
    val updatedAt = timestamp("updated_at").default(Instant.now())
}

/**
 * UserRole 테이블 정의
 */
object UserRoles : LongIdTable("user_roles") {
    val userId = reference("user_id", Users)
    val role = enumerationByName("role", 50, Role::class)
    
    init {
        uniqueIndex("user_role_idx", userId, role)
    }
}
