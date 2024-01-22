package booking.wallet;

import booking.wallet.dto.MoneyTransferRequest;
import booking.wallet.dto.MoneyTransferResponse;
import booking.wallet.dto.UpdateBalanceRequest;
import booking.wallet.entity.Wallet;
import booking.wallet.repository.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = WalletApplication.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@RequiredArgsConstructor
class WalletApplicationTests {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");

    private final MockMvc mockMvc;
    private final WalletRepository walletRepository;

    static {
        container.start();
    }

    @Test
    @WithAnonymousUser
    public void transferMoney_success() throws Exception {
        Wallet firstUserWalletBeforeTransfer = walletRepository.findWalletByUserid(1L).block();
        Wallet secondUserWalletBeforeTransfer = walletRepository.findWalletByUserid(2L).block();
        long sumOfTransfer = firstUserWalletBeforeTransfer.getBalance() - 100;
        mockMvc.perform(put("/transfer")
                        .content(asJsonString(MoneyTransferRequest.builder()
                                .fromUserId(1)
                                .toUserId(2)
                                .amount(sumOfTransfer).build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        Wallet firstUserWalletAfterTransfer = walletRepository.findWalletByUserid(1L).block();
        Wallet secondUserWalletAfterTransfer = walletRepository.findWalletByUserid(2L).block();
        assertEquals(firstUserWalletBeforeTransfer.getBalance() - sumOfTransfer, firstUserWalletAfterTransfer.getBalance());
        assertEquals(secondUserWalletBeforeTransfer.getBalance() + sumOfTransfer, secondUserWalletAfterTransfer.getBalance());
    }

    @Test
    @WithAnonymousUser
    public void transferMoney_notEnoughMoney_transferStatusInNotCompleted() throws Exception {
        Wallet firstUserWalletBeforeTransfer = walletRepository.findWalletByUserid(1L).block();
        Wallet secondUserWalletBeforeTransfer = walletRepository.findWalletByUserid(2L).block();
        long sumOfTransfer = firstUserWalletBeforeTransfer.getBalance() * 2;
        MvcResult mvcResult = mockMvc.perform(put("/transfer")
                .content(asJsonString(MoneyTransferRequest.builder()
                        .fromUserId(1)
                        .toUserId(2)
                        .amount(sumOfTransfer).build())
                )
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        MoneyTransferResponse moneyTransferResponse = fromJson(contentAsString);
        assertFalse(moneyTransferResponse.isCompleted());
        Wallet firstUserWalletAfterTransfer = walletRepository.findWalletByUserid(1L).block();
        Wallet secondUserWalletAfterTransfer = walletRepository.findWalletByUserid(2L).block();
        assertEquals(firstUserWalletBeforeTransfer.getBalance(), firstUserWalletAfterTransfer.getBalance());
        assertEquals(secondUserWalletBeforeTransfer.getBalance(), secondUserWalletAfterTransfer.getBalance());
    }

    @Test
    @WithAnonymousUser
    public void updateBalance_success() throws Exception {
        Wallet firstUserWalletBeforeUpdate = walletRepository.findWalletByUserid(1L).block();
        long amountToUpdate = 2500L;
        mockMvc.perform(put("/balance")
                        .content(asJsonString(UpdateBalanceRequest.builder()
                                .userId(1)
                                .amount(amountToUpdate)
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
        Wallet firstUserWalletAfterUpdate = walletRepository.findWalletByUserid(1L).block();
        assertEquals(firstUserWalletBeforeUpdate.getBalance() + amountToUpdate, firstUserWalletAfterUpdate.getBalance());
    }

    @Test
    @WithAnonymousUser
    public void updateBalance_userIdNotExists_throwsException() {
        long amountToUpdate = 2500L;
        assertThrows(ServletException.class, () -> {
            mockMvc.perform(put("/balance")
                            .content(asJsonString(UpdateBalanceRequest.builder()
                                    .userId(10L)
                                    .amount(amountToUpdate)
                                    .build())
                            )
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();
        });
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", container::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", container::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", container::getPassword);

        String jdbcUrl = container.getJdbcUrl();
        dynamicPropertyRegistry.add("spring.r2dbc.url", () -> jdbcUrl.replace("jdbc", "r2dbc"));
        dynamicPropertyRegistry.add("spring.r2dbc.username", container::getUsername);
        dynamicPropertyRegistry.add("spring.r2dbc.password", container::getPassword);

        dynamicPropertyRegistry.add("spring.liquibase.url", container::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.liquibase.user", container::getUsername);
        dynamicPropertyRegistry.add("spring.liquibase.password", container::getPassword);

    }

    public String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MoneyTransferResponse fromJson(final String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, MoneyTransferResponse.class);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
