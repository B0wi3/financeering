package com.bowie.financeering.transaction;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TransactionOwnershipIT {

    @Autowired
    private MockMvc mockMvc;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor asUser(String userSub) {
        return jwt().jwt(jwt -> jwt.claim("sub", userSub));
    }

    @Test
    @DisplayName("User can create and read own transactions")
    void userCanCreateAndReadOwnTransactions() throws Exception {
        String createBody = """
                {
                    "amount": 125.50,
                    "currency": "BRL",
                    "transactionType": "EXPENSE",
                    "category": "food"
                }
                """;

        MvcResult createResult = mockMvc.perform(post("/api/finance")
                .with(asUser("user1"))
                .contentType("application/json")
                .content(createBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("food"))
                .andReturn();

        Long id = JsonPath.parse(createResult.getResponse().getContentAsString())
                .read("$.id", Long.class);

        mockMvc.perform(get("/api/finance/{id}", id)
                .with(asUser("user1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    @DisplayName("User cannot read another user's transactions")
    void userCannotReadAnotherUsersTransactions() throws Exception {
        String createBody = """
                {
                    "amount": 200.00,
                    "currency": "USD",
                    "transactionType": "INCOME",
                    "category": "salary"
                }
                """;

        MvcResult createResult = mockMvc.perform(post("/api/finance")
                        .with(asUser("user1"))
                        .contentType("application/json")
                        .content(createBody))
                .andExpect(status().isOk())
                .andReturn();

        Long id = JsonPath.parse(createResult.getResponse().getContentAsString())
                .read("$.id", Long.class);

        mockMvc.perform(get("/api/finance/{id}", id)
                        .with(asUser("user2")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("User cannot update or delete another user's transactions")
    void userCannotUpdateAnotherUsersTransactions() throws Exception {
        String createBody = """
                {
                    "amount": 300.00,
                    "currency": "EUR",
                    "transactionType": "EXPENSE",
                    "category": "entertainment"
                }
                """;

        MvcResult createResult = mockMvc.perform(post("/api/finance")
                        .with(asUser("user1"))
                        .contentType("application/json")
                        .content(createBody))
                        .andExpect(status().isOk())
                        .andReturn();

        Long id = JsonPath.parse(createResult.getResponse().getContentAsString())
                .read("$.id", Long.class);

        String updateBody = """
                {
                    "amount": 999.00,
                    "currency": "EUR",
                    "transactionType": "EXPENSE",
                    "category": "hacked"
                }
                """;

        mockMvc.perform(put("/api/finance/{id}", id)
                        .with(asUser("user2"))
                        .contentType("application/json")
                        .content(updateBody))
                        .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/finance/{id}", id)
                        .with(asUser("user2")))
                        .andExpect(status().isForbidden());
    }
}
