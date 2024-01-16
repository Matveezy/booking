package booking.user.dto;

import booking.user.entity.Role;
import booking.user.validation.EnumNamePattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    @NotBlank(message = "login cannot be blank!")
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "login must be an email!")
    private String login;

    @EnumNamePattern(regexp = "USER|OWNER|ADMIN",
            message = "role can take the following values: USER, OWNER, ADMIN")
    private Role role;
}
