package kim.wonung.security_sample.adapter.inbound.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {
    @GetMapping("/hello")
    fun hello(): String = "Hello, Security Sample!"
}
