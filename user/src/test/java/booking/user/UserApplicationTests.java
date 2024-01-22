package booking.user;

import booking.user.dto.CreateUserByAdminDto;
import booking.user.dto.RegisterRequest;
import booking.user.dto.UpdateUserDto;
import booking.user.entity.Role;
import booking.user.entity.User;
import booking.user.feign.WalletServiceClient;
import booking.user.repository.UserRepository;
import booking.user.security.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(value = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration",
        classes = UserApplication.class)
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(value = "classpath:users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserApplicationTests {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    private final UserRepository userRepository;

    private final MockMvc mockMvc;

    private final static String USER_LOGIN = "user@yandex.ru";
    private final static String ADMIN_LOGIN = "admin@yandex.ru";
    private final static String OWNER_LOGIN = "owner@yandex.ru";

    @MockBean(classes = {WalletServiceClient.class})
    private final WalletServiceClient walletServiceClient;

    static {
        container.start();
    }

    @Test
    @WithAnonymousUser
    void createUser_success() throws Exception {
        List<User> allUsers = userRepository.findAll();
        assertEquals(3, allUsers.size());
        mockMvc.perform(post("/users")
                .content(asJsonString(registerRequest()))
                .contentType(MediaType.APPLICATION_JSON));
        List<User> allUsersAfterRegister = userRepository.findAll();
        assertEquals(4, allUsersAfterRegister.size());
    }

    @Test
    @WithMockCustomUser(username = USER_LOGIN)
    void createUserByAdmin_loggedByUser_receivesForbiddenHttpStatus() throws Exception {
        List<User> allUsers = userRepository.findAll();
        assertEquals(3, allUsers.size());
        mockMvc.perform(post("/admin")
                        .content(asJsonString(adminRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
        List<User> allUsersAfterRegister = userRepository.findAll();
        assertEquals(3, allUsersAfterRegister.size());
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void createUserByAdmin_loggedByOwner_receivesForbiddenHttpStatus() throws Exception {
        List<User> allUsers = userRepository.findAll();
        assertEquals(3, allUsers.size());
        mockMvc.perform(post("/admin")
                        .content(asJsonString(adminRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
        List<User> allUsersAfterRegister = userRepository.findAll();
        assertEquals(3, allUsersAfterRegister.size());
    }

    @Test
    @WithMockCustomUser(username = ADMIN_LOGIN)
    void createUserByAdmin_loggedByOwner_success() throws Exception {
        List<User> allUsers = userRepository.findAll();
        mockMvc.perform(post("/admin")
                .content(asJsonString(adminRequest()))
                .contentType(MediaType.APPLICATION_JSON));
        List<User> allUsersAfterRegister = userRepository.findAll();
        assertEquals(4, allUsersAfterRegister.size());
        Optional<User> createdUserOptional = allUsersAfterRegister.stream().filter(user -> adminRequest().getLogin().equals(user.getLogin())).findAny();
        assertTrue(createdUserOptional.isPresent());
    }

    @Test
    @WithMockCustomUser(username = USER_LOGIN)
    void updateUser_loggedByUser_receivesForbiddenHttpStatus() throws Exception {
        UpdateUserDto updateUserRequest = UpdateUserDto.builder().login(OWNER_LOGIN).role(Role.ADMIN).build();
        mockMvc.perform(put("/admin")
                        .content(asJsonString(updateUserRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
        User byLogin = userRepository.findByLogin(OWNER_LOGIN);
        assertEquals(Role.OWNER, byLogin.getRole());
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void updateUser_loggedByOwner_receivesForbiddenHttpStatus() throws Exception {
        UpdateUserDto updateUserRequest = UpdateUserDto.builder().login(OWNER_LOGIN).role(Role.ADMIN).build();
        mockMvc.perform(put("/admin")
                        .content(asJsonString(updateUserRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
        User byLogin = userRepository.findByLogin(OWNER_LOGIN);
        assertEquals(Role.OWNER, byLogin.getRole());
    }

    public CreateUserByAdminDto adminRequest() {
        return CreateUserByAdminDto.builder()
                .name("Added user")
                .login("added-user@yandex.ru")
                .pass("pass")
                .dateOfBirth(Instant.now())
                .role(Role.USER)
                .build();
    }

    public RegisterRequest registerRequest() {
        return RegisterRequest.builder()
                .name("Added user")
                .login("added-user@yandex.ru")
                .pass("pass")
                .dateOfBirth(Instant.now())
                .build();
    }

    public String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
    }
}
