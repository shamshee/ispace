package com.ispace.practical_assignment;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAuthControllerLogin() throws Exception {
        String loginRequest = "{\n" +
                "  \"username\": \"shamshee_ruhani\",\n" +
                "  \"password\": \"Sham@123@Ruhani\"\n" +
                "}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").exists())
                .andExpect(jsonPath("$.userId").value(Matchers.isA(Integer.class)))
                .andExpect(jsonPath("$.username").value(instanceOf(String.class)))
                .andExpect(jsonPath("$.roles[0]").exists());
    }


    @Test
    public void testAuthControllerLoginWithOutUserName() throws Exception {
        String loginRequest = "{\n" +
               // "  \"username\": \"shamshee_ruhani\",\n" +
                "  \"password\": \"Sham@123@Ruhani\"\n" +
                "}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("must not be blank"))
               .andExpect(jsonPath("$.username").value(instanceOf(String.class)));



    }

    @Test
    public void testAuthControllerLoginWithOutPassword() throws Exception {
        String loginRequest = "{\n" +
                "  \"username\": \"shamshee_ruhani\"\n" +
                "}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value("must not be blank"))
                .andExpect(jsonPath("$.password").value(instanceOf(String.class)));



    }


    @Test
    public void testAuthControllerLoginWithOutUsernameAndPassword() throws Exception {
        String loginRequest = "{}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("must not be blank"))
                .andExpect(jsonPath("$.username").value(instanceOf(String.class)))
                .andExpect(jsonPath("$.password").value("must not be blank"))
                .andExpect(jsonPath("$.password").value(instanceOf(String.class)));




    }


    @Test
    public void testAuthControllerLoginWithInvalidBody() throws Exception {
        String loginRequest = "{\n" +
                "  \"username\": \"shamshee_ruhani\",\n" +
                " \n" +
                "}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").exists());
    }


    @Test
    public void testAuthControllerloginWithBadCredentials() throws Exception {
        String loginRequest = "{\n" +
                "  \"username\": \"shamshee_ruhani1\",\n" +
                "  \"password\": \"Sham@123@Ruhani\"\n" +
                "}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.statusCode").value(401))
                .andExpect(jsonPath("$.message").exists());
    }



    @Test
    public void testAuthControllerSignUp() throws Exception {

        String uniqueEmail = generateUniqueEmail();
        String uniqueUserId = generateUniqueUserId();
        String signUpRequest = String.format("{\n" +
                "  \"username\": \"%s\",\n" +
                "  \"email\": \"%s\",\n" +
                "  \"role\": [\"admi\"],\n" +
                "  \"password\": \"Tiger@754\"\n" +
                "}", uniqueUserId, uniqueEmail);

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signUpRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }


    @Test
    public void testAuthControllerSignUpExistingUserName() throws Exception {


        String signUpRequest = "{\n" +
                "  \"username\": \"sham1shee123_r123\",\n" +
                "  \"email\": \"sham1shee1231ruhani123@gmail.com\",\n" +
                "  \"role\": [\"admin\"],\n" +
                "  \"password\": \"Tiger@754\"\n" +
                "}";

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Error: Username is already taken!"));
    }

    @Test
    public void testAuthControllerSignUpExistingEmail() throws Exception {


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
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Error: Email is already in use!"));
    }


    @Test
    public void testAuthControllerSignUpWithTemperedJsonRequest() throws Exception {


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
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void testAuthControllerSignUpValidation() throws Exception {
        String loginRequest = "{\n" +
                "  \"username\": \"shamshee_ruhani\"\n" +
                "}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isBadRequest());




    }


    public static String generateUniqueEmail() {
        return "user_" +  UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "").substring(0, 10) + "@example.com";
    }

    public static String generateUniqueUserId() {
        return "user_" + UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "").substring(0, 10);
    }


    }
