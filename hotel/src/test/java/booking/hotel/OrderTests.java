package booking.hotel;

import booking.hotel.dto.MoneyTransferResponse;
import booking.hotel.dto.OrderCreateDto;
import booking.hotel.entity.Role;
import booking.hotel.feign.WalletServiceClient;
import booking.hotel.repo.OrderRepository;
import booking.hotel.security.WithMockCustomUser;
import booking.hotel.service.ReceiptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
class OrderTests {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    private final MockMvc mockMvc;
    private final OrderRepository orderRepository;

    @MockBean(classes = {WalletServiceClient.class})
    private final WalletServiceClient walletServiceClient;

    @MockBean(classes = {KafkaTemplate.class})
    private final KafkaTemplate<Long, Object> kafkaTemplate;

    @BeforeEach
    public void mockKafka() {
        when(kafkaTemplate.send(any(), any())).thenReturn(new CompletableFuture<>());
    }

    static {
        container.start();
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void makeOrder_success() throws Exception {
        mockSuccessTransfer();
        int countOfOrdersBefore = orderRepository.findAll().size();
        mockMvc.perform(post("/orders")
                .header("userId", "1")
                .content(asJsonString(orderCreateDto(1L, dateIn(), dateIn().plus(2, ChronoUnit.DAYS))))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());
        int countOfOrdersAfter = orderRepository.findAll().size();
        assertEquals(countOfOrdersAfter, countOfOrdersBefore + 1);
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void makeOrder_unsuccessful() throws Exception {
        mockUnSuccessTransfer();
        int countOfOrdersBefore = orderRepository.findAll().size();
        mockMvc.perform(post("/orders")
                .header("userId", "1")
                .content(asJsonString(orderCreateDto(1L, dateIn(), dateIn().plus(2, ChronoUnit.DAYS))))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
        int countOfOrdersAfter = orderRepository.findAll().size();
        assertEquals(countOfOrdersAfter, countOfOrdersBefore);
    }

    @Test
    @WithMockCustomUser(role = Role.USER)
    public void makeOrder_roomIsNotFree_unsuccessful() throws Exception {
        mockSuccessTransfer();
        mockMvc.perform(post("/orders")
                .header("userId", "1")
                .content(asJsonString(orderCreateDto(1L, dateIn(), dateIn().plus(2, ChronoUnit.DAYS))))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());
        int countOfOrdersBefore = orderRepository.findAll().size();

        MvcResult mvcResult = mockMvc.perform(post("/orders")
                .header("userId", "1")
                .content(asJsonString(orderCreateDto(1L, dateIn().plus(1, ChronoUnit.DAYS), dateIn().plus(3, ChronoUnit.DAYS))))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError()).andReturn();

        int countOfOrdersAfter = orderRepository.findAll().size();
        assertEquals(countOfOrdersAfter, countOfOrdersBefore);

        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertTrue(contentAsString.contains("Room is not free"));

    }

    public Instant dateIn() {
        return LocalDateTime.of(2024, 12, 1, 0, 0).toInstant(ZoneOffset.UTC);
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

    public void mockSuccessTransfer() {
        when(walletServiceClient.transferMoney(any()))
                .thenReturn(ResponseEntity.ok(
                        MoneyTransferResponse.builder().isCompleted(true).build()
                ));
    }

    public void mockUnSuccessTransfer() {
        when(walletServiceClient.transferMoney(any()))
                .thenReturn(ResponseEntity.ok(
                        MoneyTransferResponse.builder().isCompleted(false).build()
                ));
    }

    OrderCreateDto orderCreateDto(long roomId, Instant dateIn, Instant dateOut) {
        return OrderCreateDto.builder()
                .roomId(roomId)
                .dateIn(dateIn)
                .dateOut(dateOut)
                .build();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
    }
}
