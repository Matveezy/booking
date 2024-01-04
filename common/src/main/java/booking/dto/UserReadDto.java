package booking.dto;

import booking.entity.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReadDto {

    Long id;
    String login;
    String name;
    Role role;
}
