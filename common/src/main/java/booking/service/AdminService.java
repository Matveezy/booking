package booking.service;

import booking.dto.UserPermissionUpdateDto;
import booking.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final UserService userService;

    @Transactional
    public void updatePermission(UserPermissionUpdateDto userPermissionUpdateDto) {
        userService.findByLogin(userPermissionUpdateDto.getLogin())
                .map(userReadDto -> userService.updateRole(userPermissionUpdateDto.getRole(), userPermissionUpdateDto.getLogin()))
                .orElseThrow((() -> new EntityNotFoundException("Can't retrieve user with login :" + userPermissionUpdateDto.getLogin())));
    }
}
