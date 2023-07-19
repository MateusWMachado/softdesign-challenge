package com.mateuswmachado.votesystem.domain.service;

import com.mateuswmachado.votesystem.adapter.dto.UserDTO;
import com.mateuswmachado.votesystem.adapter.dto.UserStatusDTO;
import com.mateuswmachado.votesystem.domain.User;
import com.mateuswmachado.votesystem.port.outbound.UserRepository;
import com.mateuswmachado.votesystem.domain.service.rest.InfoUserCPF;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private InfoUserCPF infoUserCPF;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, modelMapper, infoUserCPF);
    }

    @Test
    @DisplayName("Test validateUserCPF with valid CPF")
    void validateUserCPF_ValidCPF_ReturnsUserStatusDTO() {
        String cpf = "12345678900";
        UserStatusDTO expectedStatusDTO = new UserStatusDTO("ABLE_TO_VOTE");

        when(infoUserCPF.validateUserCPF(cpf)).thenReturn(expectedStatusDTO);

        UserStatusDTO statusDTO = userService.validateUserCPF(cpf);

        assertEquals(expectedStatusDTO, statusDTO);
        verify(infoUserCPF).validateUserCPF(cpf);
    }

    @Test
    @DisplayName("Test saveUser with valid UserDTO")
    void saveUser_ValidUserDTO_ReturnsCreatedResponse() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        User user = new User();
        user.setId(1L);
        URI uri = URI.create("https://example.com/user/");

        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        String response = userService.saveUser(userDTO);

        assertNotNull(response);
        assertEquals("Created user with id 1", response);
        verify(modelMapper).map(userDTO, User.class);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Test findByCpf with existing CPF")
    void findByCpf_ExistingCPF_ReturnsUserDTO() {
        String cpf = "12345678900";
        User user = new User();
        UserDTO expectedUserDTO = new UserDTO();

        when(userRepository.findByCpf(cpf)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(expectedUserDTO);

        UserDTO userDTO = userService.findByCpf(cpf);

        assertEquals(expectedUserDTO, userDTO);
        verify(userRepository).findByCpf(cpf);
        verify(modelMapper).map(user, UserDTO.class);
    }

    @Test
    @DisplayName("Test findByCpf with non-existent CPF")
    void findByCpf_NonExistentCPF_ThrowsUserNotFoundException() {
        String cpf = "12345678900";

        when(userRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.findByCpf(cpf);
        });

        verify(userRepository).findByCpf(cpf);
    }
}