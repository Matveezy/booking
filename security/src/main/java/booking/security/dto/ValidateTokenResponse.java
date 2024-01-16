package booking.security.dto;

import booking.security.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateTokenResponse {

    Long userId;
    String login;
    List<Role> authorities;
    boolean isAuthenticated;
}
