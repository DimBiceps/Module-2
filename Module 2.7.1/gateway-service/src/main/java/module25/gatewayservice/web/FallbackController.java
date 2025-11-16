package module25.gatewayservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class FallbackController {

    @RequestMapping("/fallback/users")
    public ResponseEntity<?> usersFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "User service is temporarily unavailable"));
    }

    @RequestMapping("/fallback/notify")
    public ResponseEntity<?> notifyFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Notification service is temporarily unavailable"));
    }
}
