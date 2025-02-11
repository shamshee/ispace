package com.ispace.practical_assignment;


import com.ispace.practical_assignment.controller.RecordController;
import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecordControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    private String adminJwtToken;
    private String userJwtToken;

    @BeforeAll
    void setUp() throws Exception {
        String adminLoginRequest = "{\n" +
                "  \"username\": \"shamshee_ruhani\",\n" +
                "  \"password\": \"Sham@123@Ruhani\"\n" +
                "}";

        String userLoginRequest="{\n" +
                "  \"username\": \"sham1\",\n" +
                "  \"password\": \"Tiger@754\"\n" +
                "}";

        MvcResult adminResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminLoginRequest))
                .andExpect(status().isOk())
                .andReturn();
        adminJwtToken = JsonPath.read(adminResult.getResponse().getContentAsString(), "$.data.jwtToken");


        MvcResult userResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLoginRequest))
                .andExpect(status().isOk())
                .andReturn();
        userJwtToken = JsonPath.read(userResult.getResponse().getContentAsString(), "$.data.jwtToken");
    }

    @Test
    public void testRecordControllerSaveRecordAdminToken() throws Exception {

        String request = "{\n" +
                "  \"recordType\": \"Toys\",\n" +
                "  \"deviceId\": \"3573666015966055\",\n" +
                "  \"eventDateTime\": \"2025-02-09T12:30:00Z\",\n" +
                "  \"quantity\": 1,\n" +
                "  \"deviceName\": \"Car\",\n" +
                "  \"devicePrice\": 500.99\n" +
                "}";

        mockMvc.perform(post("/api/")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void testRecordControllerSaveRecordUserToken() throws Exception {

        String request = "{\n" +
                "  \"recordType\": \"Toys\",\n" +
                "  \"deviceId\": \"3573666015966055\",\n" +
                "  \"eventDateTime\": \"2025-02-09T12:30:00Z\",\n" +
                "  \"quantity\": 1,\n" +
                "  \"deviceName\": \"Car\",\n" +
                "  \"devicePrice\": 500.99\n" +
                "}";

        mockMvc.perform(post("/api/")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(403))
                .andExpect(jsonPath("$.message").value("Access Denied"));

    }


    @Test
    public void testRecordControllerSaveRecordWithoutToken() throws Exception {

        String request = "{\n" +
                "  \"recordType\": \"Toys\",\n" +
                "  \"deviceId\": \"3573666015966055\",\n" +
                "  \"eventDateTime\": \"2025-02-09T12:30:00Z\",\n" +
                "  \"quantity\": 1,\n" +
                "  \"deviceName\": \"Car\",\n" +
                "  \"devicePrice\": 500.99\n" +
                "}";

        mockMvc.perform(post("/api/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(401))
                .andExpect(jsonPath("$.message").value("Unauthorized"));

    }

    @Test
    public void testRecordControllerSaveRecordInvalidJson() throws Exception {


        String signUpRequest = "{\n" +
                "  \"recordType\": \"Toys\",\n" +
                "  \"deviceId\": \"3573666015966055\",\n" +
                "  \"eventDateTime\": \"2025-02-09T12:30:00Z\",\n" +
                "  \"quantity\": 1,\n" +
                "  \"deviceName\": \"Car\"\n" +
                "  \"devicePrice\": 500.99\n" +
                "}";

        mockMvc.perform(post("/api/").header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").exists());
    }



    @Test
    public void testRecordControllerSaveRecordvalidation() throws Exception {


        String signUpRequest = "{\n" +
                "  \"recordType\": \"Toys\",\n" +
                "  \"deviceId\": \"3573666015966055GT\",\n" +
                "  \"eventDateTime\": \"2025-02-09T12:30:00Z\",\n" +
                "  \"quantity\": 1,\n" +
                "  \"deviceName\": \"Car\"\n" +
                "  \"devicePrice\": 500.99\n" +
                "}";

        mockMvc.perform(post("/api/").header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequest))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testRecordControllerNoContent() throws Exception {
        mockMvc.perform(get("/api/nocontent")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken))
                .andExpect(status().isNoContent());

    }

    @Test
    public void testRecordControllerNoContentWithoutToken() throws Exception {
        mockMvc.perform(get("/api/nocontent"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(401))
                .andExpect(jsonPath("$.message").value("Unauthorized"));

    }


    @Test
    public void testRecordControllerEcho() throws Exception {
        mockMvc.perform(get("/api/echo/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testRecordControllerEchoWithoutToken() throws Exception {
        mockMvc.perform(get("/api/echo/2"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(401))
                .andExpect(jsonPath("$.message").value("Unauthorized"));

    }

    @Test
    public void testRecordControllerEchoWithoutId() throws Exception {
        mockMvc.perform(get("/api/echo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(404));


    }


    @Test
    public void testRecordControllerEChoResourceNotFound() throws Exception {
        mockMvc.perform(get("/api/echo/112")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("No Data found"));


    }

    @Test
    public void testRecordControllerDevice() throws Exception {
        mockMvc.perform(get("/api/device/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.deviceId").exists());;
    }


    @Test
    public void testRecordControllerdeviceWithoutToken() throws Exception {
        mockMvc.perform(get("/api/device/2"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(401))
                .andExpect(jsonPath("$.message").value("Unauthorized"));

    }

    @Test
    public void testRecordControllerDeviceWithoutId() throws Exception {
        mockMvc.perform(get("/api/device")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(404));


    }

    @Test
    public void testRecordControllerDeviceResourceNotFound() throws Exception {
        mockMvc.perform(get("/api/device/112")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwtToken))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("No Data found"));


    }
}