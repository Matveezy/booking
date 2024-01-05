package booking.common.dto;

import booking.common.entity.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto {

    Long id;
    String login;
    String name;
    Role role;
}
