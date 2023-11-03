package com.example.booking.service;

import com.example.booking.entity.User;
import com.example.booking.repository.UserRepository;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
@Sql("classpath:sql/data.sql")
class UserServiceTest extends IntegrationTestBase {

    private final UserService userService;
    private final UserRepository userRepository;

    private static final String EXISTING_USER_USERNAME = "ivan@gmail.com";
    private static final String NOT_EXISTING_USER_USERNAME = "123" + EXISTING_USER_USERNAME;


    @Test
    void loadUserByUsernameExisting() {
        UserDetails userDetails = userService.loadUserByUsername(EXISTING_USER_USERNAME);
        assertNotNull(userDetails);
        assertTrue(userDetails.getUsername().contains(EXISTING_USER_USERNAME));
    }

    @Test
    void loadUserByUsernameNotExisting() {
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(NOT_EXISTING_USER_USERNAME);
        });

        String expectedMessage = "Failed to retrieve user: " + NOT_EXISTING_USER_USERNAME;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void findExistingUserById() {
        Optional<User> byLogin = userRepository.findByLogin(EXISTING_USER_USERNAME);
        assertTrue(byLogin.isPresent());
        Optional<User> userById = userRepository.findUserById(byLogin.get().getId());
        assertTrue(userById.isPresent());
    }

    @Test
    void findNotExistingUserById() {
        Optional<User> userById = userRepository.findUserById(0L);
        assertTrue(userById.isEmpty());
    }
}