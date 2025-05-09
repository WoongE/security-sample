package kim.wonung.security_sample.adapter.inbound.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {
    
    @GetMapping("/health")
    fun healthCheck(): Map<String, String> {
        return mapOf("status" to "UP")
    }
}
