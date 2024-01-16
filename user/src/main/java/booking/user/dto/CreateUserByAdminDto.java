package booking.user.dto;

import booking.user.entity.Role;
import booking.user.validation.EnumNamePattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserByAdminDto {

    @NotBlank(message = "name cannot be blank!")
    private String name;

    @NotBlank(message = "login cannot be blank!")
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "login must be an email!")
    private String login;

    @NotBlank(message = "password cannot be blank!")
    private String pass;

    @NotNull
    private Instant dateOfBirth;

    @EnumNamePattern(regexp = "USER|OWNER|ADMIN",
            message = "role can take the following values: USER, OWNER, ADMIN")
    private Role role;
}
