package kim.wonung.security_sample.adapter.inbound.config

import kim.wonung.security_sample.application.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataInitializer {

    private val logger = LoggerFactory.getLogger(DataInitializer::class.java)

    @Bean
    fun initData(userService: UserService): CommandLineRunner {
        return CommandLineRunner {
            logger.info("Initializing sample data...")
            
            try {
                // 일반 사용자 생성
                val user = userService.signUp(
                    username = "user",
                    email = "user@example.com",
                    password = "password"
                )
                logger.info("Created user: ${user.username}")
                
                // 관리자 생성
                val admin = userService.createAdminUser(
                    username = "admin",
                    email = "admin@example.com",
                    password = "password"
                )
                logger.info("Created admin: ${admin.username}")
                
                logger.info("Sample data initialization completed")
            } catch (e: Exception) {
                logger.error("Error initializing sample data", e)
            }
        }
    }
}
