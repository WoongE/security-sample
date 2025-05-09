package kim.wonung.security_sample.adapter.inbound.graphql.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer

@Configuration
class GraphQLConfig {
    
    @Bean
    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { wiringBuilder ->
            // 스칼라 타입 등 추가 설정이 필요한 경우 여기에 추가
            wiringBuilder
        }
    }
}
