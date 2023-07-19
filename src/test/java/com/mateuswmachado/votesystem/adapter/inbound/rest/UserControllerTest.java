package com.mateuswmachado.votesystem.adapter.inbound.rest;

import com.mateuswmachado.votesystem.adapter.dto.UserDTO;
import com.mateuswmachado.votesystem.adapter.dto.UserStatusDTO;
import com.mateuswmachado.votesystem.domain.enums.UserStatus;
import com.mateuswmachado.votesystem.port.inbound.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.mateuswmachado.votesystem.utils.JsonTestUtil.asJsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }

    @DisplayName("Test validate user's CPF - Valid CPF")
    @Test
    void validateUsersCPF_ValidCPF_ReturnsUserStatusDTO() throws Exception {
        String cpf = "12345678901";
        UserStatusDTO userStatusDTO = new UserStatusDTO(UserStatus.ABLE_TO_VOTE.name());

        when(userService.validateUserCPF(eq(cpf))).thenReturn(userStatusDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{cpf}", cpf))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(UserStatus.ABLE_TO_VOTE.name()));
    }

    @DisplayName("Test validate user's CPF - Invalid CPF")
    @Test
    void validateUsersCPF_InvalidCPF_ReturnsNotFound() throws Exception {
        String cpf = "12345678901";
        UserStatusDTO userStatusDTO = new UserStatusDTO(UserStatus.UNABLE_TO_VOTE.name());

        when(userService.validateUserCPF(eq(cpf))).thenReturn(userStatusDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{cpf}", cpf))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(UserStatus.UNABLE_TO_VOTE.name()));
    }

    @DisplayName("Test create user - Valid UserDTO")
    @Test
    void createUser_ValidUserDTO_ReturnsCreatedResponse() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "John Doe");

        when(userService.saveUser(any(UserDTO.class))).thenReturn("User with id 1 created");

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .content(asJsonString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn().getResponse().getContentAsString();

        assertNotNull(response);
        assertEquals("User with id 1 created", response);
    }

}