package com.mateuswmachado.votesystem.port.inbound;

import com.mateuswmachado.votesystem.adapter.dto.UserDTO;
import com.mateuswmachado.votesystem.adapter.dto.UserStatusDTO;
import com.mateuswmachado.votesystem.domain.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/** Interface that represents an operations contract on User
 *
 * @author Mateus W. Machado
 * @since 17/07/2023
 * @version 1.0.0
 */

public interface UserService {

    String saveUser(UserDTO userDTO);

    UserStatusDTO validateUserCPF(String cpf);

    UserDTO findByCpf(String cpf);
}
