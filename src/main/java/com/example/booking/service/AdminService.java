package com.example.booking.service;

import com.example.booking.dto.UserPermissionUpdateDto;
import com.example.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final UserRepository userRepository;

    @Transactional
    public boolean updatePermission(UserPermissionUpdateDto userPermissionUpdateDto) {
        return userRepository.findByLogin(userPermissionUpdateDto.getLogin())
                .map(user -> {
                            int countOfUpdateEntities = userRepository.updateRole(userPermissionUpdateDto.getRole(), userPermissionUpdateDto.getLogin());
                            return countOfUpdateEntities > 0;
                        }
                )
                .orElse(false);
    }
}