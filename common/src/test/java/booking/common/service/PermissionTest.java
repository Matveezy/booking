package booking.common.service;

import booking.common.dto.*;
import booking.common.entity.HotelClass;
import booking.common.entity.Role;
import booking.common.entity.RoomClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import integration.IntegrationTestBase;
import integration.annotation.IT;
import integration.annotation.WithMockCustomUser;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@IT
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class PermissionTest extends IntegrationTestBase {

    private final MockMvc mockMvc;

    private static final String ADMIN_LOGIN = "matveezy@gmail.com";

    private static final String USER_LOGIN = "bob123@yahoo.com";

    private static final String OWNER_LOGIN = "alexandr@gmail.com";

    @Test
    @WithAnonymousUser
    void loginByAnyUser() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .content(asJsonString(RegisterRequest.builder()
                                .login("login@gmail.com")
                                .pass("pass")
                                .name("name")
                                .dateOfBirth(Instant.now())
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithAnonymousUser
    void registerByAnyUser() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .content(asJsonString(RegisterRequest.builder()
                                .login("login@gmail.com")
                                .pass("pass")
                                .name("name")
                                .dateOfBirth(Instant.now())
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithAnonymousUser
    void updateRoleByUser() throws Exception {
        mockMvc.perform(put("/admin/users/permission")
                        .content(
                                asJsonString(UserPermissionUpdateDto.builder()
                                        .login("users@gmail.com")
                                        .role(Role.OWNER)
                                        .build()
                                )
                        ).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    @WithMockCustomUser(username = OWNER_LOGIN)
    void updateRoleByOwner() throws Exception {
        mockMvc.perform(put("/admin/users/permission")
                        .content(
                                asJsonString(UserPermissionUpdateDto.builder()
                                        .login(USER_LOGIN)
                                        .role(Role.OWNER)
                                        .build()
                                )
                        ).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    @WithMockCustomUser(username = ADMIN_LOGIN)
    void updateRoleByAdmin() throws Exception {
        mockMvc.perform(put("/admin/users/permission")
                        .content(
                                asJsonString(UserPermissionUpdateDto.builder()
                                        .login(USER_LOGIN)
                                        .role(Role.OWNER)
                                        .build()
                                )
                        ).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    @WithMockCustomUser(username = USER_LOGIN)
    void createHotelByUser() throws Exception {
        mockMvc.perform(post("/hotel")
                        .content(asJsonString(
                                HotelCreateDto.builder()
                                        .name("Hotel Name")
                                        .city("Saint-P")
                                        .hotelClass(HotelClass.FIVE_STARS)
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    @WithMockCustomUser(username = USER_LOGIN)
    void updateHotelByUser() throws Exception {
        mockMvc.perform(put("/hotel" + "/1")
                        .content(asJsonString(
                                HotelUpdateDto.builder()
                                        .hotelClass(HotelClass.FIVE_STARS)
                                        .city("Saint-P")
                                        .name("Hotel Name")
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    @WithMockCustomUser(username = USER_LOGIN)
    void deleteHotelByUser() throws Exception {
        mockMvc.perform(delete("/hotel" + "/1"))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    @WithMockCustomUser(username = USER_LOGIN)
    void createRoomByUser() throws Exception {
        mockMvc.perform(post("/room")
                        .content(asJsonString(
                                RoomCreateDto.builder()
                                        .hotelId(1)
                                        .roomNumber(123)
                                        .roomClass(RoomClass.DOUBLE)
                                        .price(52)
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    @WithMockCustomUser(username = USER_LOGIN)
    void updateRoomByUser() throws Exception {
        mockMvc.perform(post("/room")
                        .content(asJsonString(
                                RoomCreateDto.builder()
                                        .hotelId(1)
                                        .roomNumber(123)
                                        .roomClass(RoomClass.DOUBLE)
                                        .price(52)
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(403));
    }

    @Test
    @WithMockCustomUser(username = USER_LOGIN)
    void deleteRoomByUser() throws Exception {
        mockMvc.perform(delete("/room/" + "1"))
                .andExpect(MockMvcResultMatchers.status().is(403));
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
}
