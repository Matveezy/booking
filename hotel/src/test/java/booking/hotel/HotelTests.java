package booking.hotel;

import booking.hotel.dto.HotelCreateDto;
import booking.hotel.dto.HotelUpdateDto;
import booking.hotel.entity.Hotel;
import booking.hotel.entity.HotelClass;
import booking.hotel.entity.Role;
import booking.hotel.repo.HotelRepository;
import booking.hotel.security.WithMockCustomUser;
import booking.hotel.service.ReceiptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBean(ReceiptService.class)
@SpringBootTest(value = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration",
        classes = HotelApplication.class)
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(value = "classpath:db-init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class HotelTests {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    private final MockMvc mockMvc;
    private final HotelRepository hotelRepository;

    static {
        container.start();
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void createHotel_loggedByUser_receivesForbiddenHttpStatus() throws Exception {
        int countOfHotelsBefore = hotelRepository.findAll().size();
        mockMvc.perform(post("/hotels")
                .header("userId", 1)
                .content(asJsonString(hotelCreateDto()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(403));
        assertEquals(countOfHotelsBefore, hotelRepository.findAll().size());
    }

    @Test
    @WithMockCustomUser(role = Role.OWNER)
    public void createHotel_loggedByOwner_success() throws Exception {
        int countOfHotelsBefore = hotelRepository.findAll().size();
        mockMvc.perform(post("/hotels")
                .header("userId", 1)
                .content(asJsonString(hotelCreateDto()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());
        int countOfHotelsAfter = hotelRepository.findAll().size();
        assertEquals(countOfHotelsAfter, countOfHotelsBefore + 1);
        Optional<Hotel> createdHotelOptional = hotelRepository.findAll()
                .stream()
                .filter(hotel -> hotel.getName().equals(hotelCreateDto().getName()))
                .findAny();
        assertEquals(createdHotelOptional.get().getUserId().get(0), 1);
        assertEquals(createdHotelOptional.get().getCity(), hotelCreateDto().getCity());
        assertEquals(createdHotelOptional.get().getHotelClass(), hotelCreateDto().getHotelClass());
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void updateHotel_loggedByUser_receivesForbiddenHttpStatus() throws Exception {
        int countOfHotelsBefore = hotelRepository.findAll().size();
        mockMvc.perform(put("/hotels")
                .header("userId", 1)
                .content(asJsonString(hotelUpdateDto(1L)))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(403));
        int countOfHotelsAfter = hotelRepository.findAll().size();
        assertEquals(countOfHotelsAfter, countOfHotelsBefore);
    }

    @Test
    @WithMockCustomUser(role = Role.OWNER)
    public void updateHotel_loggedByAnotherHotelOwner_receivesForbiddenHttpStatus() throws Exception {
        int countOfHotelsBefore = hotelRepository.findAll().size();
        mockMvc.perform(put("/hotels")
                .header("userId", 2)
                .content(asJsonString(hotelUpdateDto(1L)))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(403));
        int countOfHotelsAfter = hotelRepository.findAll().size();
        assertEquals(countOfHotelsAfter, countOfHotelsBefore);
    }

    @Test
    @WithMockCustomUser(role = Role.OWNER)
    public void updateHotel_loggedByHotelOwner_success() throws Exception {
        Optional<Hotel> hotelOptional = hotelRepository.findById(1L);
        assertTrue(hotelOptional.isPresent());
        Hotel hotel = hotelOptional.get();
        HotelUpdateDto hotelUpdateDto = hotelUpdateDto(1L);
        mockMvc.perform(put("/hotels")
                .header("userId", 1)
                .content(asJsonString(hotelUpdateDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());
        Hotel updatedHotel = hotelRepository.findById(1L).get();
        assertEquals(hotelUpdateDto.getHotelClass(), updatedHotel.getHotelClass());
        assertEquals(hotelUpdateDto.getName(), updatedHotel.getName());
        assertEquals(hotelUpdateDto.getCity(), updatedHotel.getCity());
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void delete_loggedByUser_receivesForbiddenHttpStatus() throws Exception {
        int countOfHotelsBefore = hotelRepository.findAll().size();
        mockMvc.perform(delete("/hotels")
                .header("userId", 2)
                .param("hotelId", String.valueOf(1))
        ).andExpect(status().is(403));
        int countOfHotelsAfter = hotelRepository.findAll().size();
        assertEquals(countOfHotelsAfter, countOfHotelsBefore);
    }

    @Test
    @WithMockCustomUser(role = Role.OWNER)
    public void delete_loggedByAnotherHotelOwner_receivesForbiddenHttpStatus() throws Exception {
        int countOfHotelsBefore = hotelRepository.findAll().size();
        mockMvc.perform(delete("/hotels")
                .header("userId", 2)
                .param("hotelId", String.valueOf(1))
        ).andExpect(status().is(403));
        int countOfHotelsAfter = hotelRepository.findAll().size();
        assertEquals(countOfHotelsAfter, countOfHotelsBefore);
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

    HotelUpdateDto hotelUpdateDto(Long hotelId) {
        return HotelUpdateDto.builder()
                .id(hotelId)
                .name("updated-test-hotel")
                .city("Moscow")
                .hotelClass(HotelClass.FIVE_STARS)
                .build();
    }

    HotelCreateDto hotelCreateDto() {
        return HotelCreateDto.builder()
                .name("test-hotel")
                .city("Saint-P")
                .hotelClass(HotelClass.FIVE_STARS)
                .build();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
    }

}
