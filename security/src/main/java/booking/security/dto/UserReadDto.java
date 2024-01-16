package booking.security.dto;

import booking.security.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReadDto {

    Long id;
    String login;
    String name;
    String pass;
    Role role;
}
