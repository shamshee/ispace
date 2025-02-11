package com.ispace.practical_assignment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoginSuccess() throws Exception {
        String loginRequest = "{\"username\": \"shamshee_ruhani\", \"password\": \"Sham@123@Ruhani\"}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.jwtToken").exists())
                .andExpect(jsonPath("$.data.userId", isA(Integer.class)))
                .andExpect(jsonPath("$.data.username", instanceOf(String.class)))
                .andExpect(jsonPath("$.data.roles[0]").exists());
    }

    @Test
    public void testLoginWithoutUsername() throws Exception {
        String loginRequest = "{\"password\": \"Sham@123@Ruhani\"}";


        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("property validation failed"))
                .andExpect(jsonPath("$.data.username").value("must not be blank"))
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.timestamp").exists()); // Ensure timestamp is present
    }

    @Test
    public void testLoginWithoutPassword() throws Exception {
        String loginRequest = "{\"username\": \"shamshee_ruhani\"}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("property validation failed"))
                .andExpect(jsonPath("$.data.password").value("must not be blank"))
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.timestamp").exists()); // Ensure timestamp is present
    }

    @Test
    public void testLoginWithInvalidBody() throws Exception {
        String loginRequest = "{\n" +
                "  \"username\": \"shamshee_ruhani\",\n" +
                " \n" +
                "}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void testLoginWithBadCredentials() throws Exception {
        String loginRequest = "{\"username\": \"invalid_user\", \"password\": \"wrongPass123\"}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode", is(401)))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void testSignUpSuccess() throws Exception {
        String signUpRequest = String.format("{\"username\": \"%s\", \"email\": \"%s\", \"role\": [\"admin\"], \"password\": \"Tiger@754\"}",
                generateUniqueUserId(), generateUniqueEmail());

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode", is(201)))
                .andExpect(jsonPath("$.message", is("User created Successfully")));
    }

    @Test
    public void testSignUpExistingUsername() throws Exception {
        String signUpRequest = "{\"username\": \"existing_user\", \"email\": \"newemail@example.com\", \"role\": [\"admin\"], \"password\": \"Tiger@754\"}";

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("Error: Username is already taken!")));
    }

    @Test
    public void testSignUpExistingEmail() throws Exception {
        String signUpRequest = "{\n" +
                "  \"username\": \"sham11shee123_r123\",\n" +
                "  \"email\": \"sham1shee1231ruhani123@gmail.com\",\n" +
                "  \"role\": [\"admin\"],\n" +
                "  \"password\": \"Tiger@754\"\n" +
                "}";

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("Error: Email is already in use!")));
    }

    @Test
    public void testSignUpWithInvalidJson() throws Exception {
        String signUpRequest = "{\n" +
                "  \"username\": \"sham11shee123_r123\",\n" +
                "  \"email\": \"sham1shee1231ruhani123@gmail.com\",\n" +
                "  \"role\": [\"admin\"]\n" +
                "  \"password\": \"Tiger@754\"\n" +
                "}";

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message").exists());
    }


    @Test
    public void testSignUpValidation() throws Exception {
        String loginRequest = "{\n" +
                "  \"username\": \"shamshee_ruhani\"\n" +
                "}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isBadRequest());
    }

    private static String generateUniqueEmail() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    private static String generateUniqueUserId() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
