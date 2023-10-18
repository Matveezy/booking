package com.example.booking.controller;

import com.example.booking.dto.UserPermissionUpdateDto;
import com.example.booking.dto.UserReadDto;
import com.example.booking.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/users/permission")
    public ResponseEntity<?> updatePermission(@RequestBody UserPermissionUpdateDto userPermissionUpdateDto) {
        if (adminService.updatePermission(userPermissionUpdateDto)) {
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .build();
        } else {
            throw new UsernameNotFoundException("Can't retrieve user with login: " + userPermissionUpdateDto.getLogin());
        }
    }

}