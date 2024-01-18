package booking.security.feign;

import booking.security.dto.RegisterRequest;
import booking.security.dto.UserReadDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user")
public interface UserServiceClient {

    @PostMapping("/users")
    UserReadDto createUser(@RequestBody @Validated RegisterRequest registerRequest);

    @GetMapping("/users/{login}")
    UserReadDto findUserByLogin(@PathVariable String login);
}
