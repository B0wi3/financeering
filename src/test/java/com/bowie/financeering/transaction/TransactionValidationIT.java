package com.bowie.financeering.transaction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TransactionValidationIT {

    @Autowired
    private MockMvc mockMvc;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor asUser(String userSub) {
        return jwt().jwt(jwt -> jwt.claim("sub", userSub));
    }

    @Test
    @DisplayName("Amount must be positive")
    void amountMustBePositive() throws Exception {
        String body = """
                {
                    "amount": 0,
                    "currency": "BRL",
                    "transactionType": "EXPENSE",
                    "category": "food"
                }
                """;

        mockMvc.perform(post("/api/finance")
                        .with(asUser("user1"))
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Currency must be valid")
    void currencyMustBeValid() throws Exception {
        String body = """
                {
                    "amount": 100,
                    "currency": "JPY",
                    "transactionType": "EXPENSE",
                    "category": "food"
                }
                """;

        mockMvc.perform(post("/api/finance")
                        .with(asUser("user1"))
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Category cannot be blank")
    void categoryCannotBeBlank() throws Exception {
        String body = """
                {
                    "amount": 100,
                    "currency": "BRL",
                    "transactionType": "EXPENSE",
                    "category": ""
                }
                """;

        mockMvc.perform(post("/api/finance")
                        .with(asUser("user1"))
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Transaction type must not be null")
    void transactionTypeMustNotBeNull() throws Exception {
        String body = """
                {
                    "amount": 100,
                    "currency": "BRL",
                    "category": "food"
                }
                """;

        mockMvc.perform(post("/api/finance")
                        .with(asUser("user1"))
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());
    }

}