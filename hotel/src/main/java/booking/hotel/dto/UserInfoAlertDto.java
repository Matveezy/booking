package booking.hotel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoAlertDto {
    private String login;
    private String name;
}
