package booking.hotel;

import booking.hotel.dto.RoomCreateDto;
import booking.hotel.dto.RoomUpdateDto;
import booking.hotel.entity.*;
import booking.hotel.repo.RoomRepository;
import booking.hotel.security.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(value = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration",
        classes = HotelApplication.class)
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(value = "classpath:db-init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RoomTests {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    private final MockMvc mockMvc;
    private final RoomRepository roomRepository;

    static {
        container.start();
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void createRoom_loggedByUser_receivesForbiddenHttpStatus() throws Exception {
        mockMvc.perform(post("/rooms")
                .header("userId", 1)
                .content(asJsonString(roomCreateDto(1L)))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(403));
    }

    @Test
    @WithMockCustomUser(role = Role.OWNER)
    public void createRoom_loggedByAnotherHotelOwner_receivesForbiddenHttpStatus() throws Exception {
        mockMvc.perform(post("/rooms")
                .header("userId", 1)
                .content(asJsonString(roomCreateDto(5L)))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(403));
    }

    @Test
    @WithMockCustomUser(role = Role.OWNER)
    public void createRoom_loggedByHotelOwner_success() throws Exception {
        int countOfRoomBeforeCreating = roomRepository.findRoomsByHotel_Id(1, Pageable.unpaged()).size();
        RoomCreateDto roomCreateDto = roomCreateDto(1L);
        mockMvc.perform(post("/rooms")
                .header("userId", 1)
                .content(asJsonString(roomCreateDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());
        List<Room> roomsAfterCreating = roomRepository.findRoomsByHotel_Id(1, Pageable.unpaged());
        assertEquals(roomsAfterCreating.size(), countOfRoomBeforeCreating + 1);
        Optional<Room> createdRoom = roomsAfterCreating.stream().filter(room -> room.getRoomNumber() == roomCreateDto.getRoomNumber()).findAny();
        assertTrue(createdRoom.isPresent());
        Room room = createdRoom.get();
        assertEquals(roomCreateDto.getPrice(), room.getPrice());
        assertEquals(roomCreateDto.getHotelId(), room.getHotel().getId());
        assertEquals(roomCreateDto.getRoomClass(), room.getRoomClass());
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void update_loggedByUser_receivesForbiddenHttpStatus() throws Exception {
        mockMvc.perform(put("/rooms")
                .header("userId", 1)
                .content(asJsonString(roomUpdateDto(1L)))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(403));
    }

    @Test
    @WithMockCustomUser(role = Role.OWNER)
    public void update_loggedByAnotherHotelOwner_receivesForbiddenHttpStatus() throws Exception {
        mockMvc.perform(put("/rooms")
                .header("userId", 1)
                .content(asJsonString(roomUpdateDto(5L)))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(403));
    }

    @Test
    @WithMockCustomUser(role = Role.OWNER)
    public void update_loggedHotelOwner_success() throws Exception {
        Optional<Room> roomBeforeUpdatingOptional = roomRepository.findRoomById(9L);
        assertTrue(roomBeforeUpdatingOptional.isPresent());
        RoomUpdateDto roomUpdateDto = roomUpdateDto(9L);
        mockMvc.perform(put("/rooms")
                .header("userId", 1)
                .content(asJsonString(roomUpdateDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());

        Optional<Room> roomAfterUpdatingOptional = roomRepository.findRoomById(9L);
        assertTrue(roomAfterUpdatingOptional.isPresent());
        Room roomAfterUpdating = roomAfterUpdatingOptional.get();
        assertEquals(roomUpdateDto.getRoomClass(), roomAfterUpdating.getRoomClass());
        assertEquals(roomUpdateDto.getPrice(), roomAfterUpdating.getPrice());
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void delete_loggedByUser_receivesForbiddenHttpStatus() throws Exception {
        mockMvc.perform(delete("/rooms")
                .header("userId", 1)
                .param("roomId", "1")
                .content(asJsonString(roomUpdateDto(1L)))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(403));
    }

    @Test
    @WithMockCustomUser(role = Role.OWNER)
    public void delete_loggedByAnotherHotelOwner_receivesForbiddenHttpStatus() throws Exception {
        mockMvc.perform(delete("/rooms")
                .header("userId", 1)
                .param("roomId", "1")
                .content(asJsonString(roomUpdateDto(1L)))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(403));
    }

    @Test
    @WithMockCustomUser(role = Role.OWNER)
    public void delete_loggedByHotelOwner_success() throws Exception {
        int countOfRoomBeforeDeleting = roomRepository.findRoomsByHotel_Id(1, Pageable.unpaged()).size();
        mockMvc.perform(delete("/rooms")
                .header("userId", 1)
                .param("roomId", "9")
                .content(asJsonString(roomUpdateDto(1L)))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());
        int countOfRoomAfterDeleting = roomRepository.findRoomsByHotel_Id(1, Pageable.unpaged()).size();
        assertEquals(countOfRoomAfterDeleting, countOfRoomBeforeDeleting - 1);
        Optional<Room> maybeRoom = roomRepository.findRoomById(9L);
        assertTrue(maybeRoom.isEmpty());
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

    RoomUpdateDto roomUpdateDto(Long roomId) {
        return RoomUpdateDto.builder()
                .id(roomId)
                .price(1000L)
                .roomClass(RoomClass.DOUBLE)
                .build();
    }

    RoomCreateDto roomCreateDto(Long hotelId) {
        return RoomCreateDto.builder()
                .hotelId(hotelId)
                .roomNumber(1000)
                .roomClass(RoomClass.SINGLE)
                .price(5000)
                .build();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
    }
}
