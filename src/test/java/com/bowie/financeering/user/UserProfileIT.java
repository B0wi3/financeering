package com.bowie.financeering.user;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserProfileIT {

    @Autowired
    private MockMvc mockMvc;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor asUser(String userSub) {
        return jwt().jwt(jwt -> jwt.claim("sub", userSub));
    }

    @Test
    @DisplayName("User can create and read their own profile")
    void userCanCreateAndReadOwnProfile() throws Exception {
        String createBody = """
                {
                    "currency": "BRL",
                    "locale": "pt_BR"
                }
                """;

        mockMvc.perform(post("/api/user")
                .with(asUser("user1"))
                .contentType("application/json")
                .content(createBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userSub").value("user1"));

        mockMvc.perform(get("/api/user")
                .with(asUser("user1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userSub").value("user1"))
                .andExpect(jsonPath("$.currency").value("BRL"));
    }

    @Test
    @DisplayName("User can update own profile")
    void userCanUpdateOwnProfile() throws Exception {
        String createBody = """
                {
                    "currency": "BRL",
                    "locale": "pt_BR"
                }
                """;

        mockMvc.perform(post("/api/user")
                .with(asUser("user1"))
                .contentType("application/json")
                .content(createBody))
                .andExpect(status().isCreated());

        String updateBody = """
                {
                    "currency": "USD",
                    "locale": "en_US"
                }
                """;

        mockMvc.perform(put("/api/user")
                .with(asUser("user1"))
                .contentType("application/json")
                .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.locale").value("en_US"));
    }

    @Test
    @DisplayName("User profile validation rejects invalid locale and currency")
    void invalidProfileValuesAreRejected() throws Exception {
        String invalidBody = """
                {
                    "currency": "JPY",
                    "locale": "invalid"
                }
                """;

        mockMvc.perform(post("/api/user")
                .with(asUser("user1"))
                .contentType("application/json")
                .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

}
