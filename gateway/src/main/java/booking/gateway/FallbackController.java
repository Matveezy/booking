package booking.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
    @GetMapping("/message")
    public ResponseEntity<String> fallback() {
        return new ResponseEntity<>("Service is taking too long to respond or is down. Please try again later", HttpStatus.GATEWAY_TIMEOUT);
    }
}
