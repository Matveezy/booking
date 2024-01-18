package booking.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @RequestMapping("/orderFallBack")
    public ResponseEntity<String> fallback() {
        return new ResponseEntity<>("It is taking too long to respond. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
