package edu.ms.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/users")
    public ResponseEntity<?> usersFallback() {
        return ResponseEntity.ok(Map.of(
                "message", "User Service временно недоступен. Показан fallback из API Gateway.",
                "service", "api-gateway"));
    }
}
