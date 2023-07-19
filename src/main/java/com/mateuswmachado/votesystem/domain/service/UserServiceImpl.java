package com.mateuswmachado.votesystem.domain.service;

import com.mateuswmachado.votesystem.adapter.dto.UserDTO;
import com.mateuswmachado.votesystem.adapter.dto.UserStatusDTO;
import com.mateuswmachado.votesystem.domain.User;
import com.mateuswmachado.votesystem.port.inbound.UserService;
import com.mateuswmachado.votesystem.port.outbound.UserRepository;
import com.mateuswmachado.votesystem.domain.service.rest.InfoUserCPF;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

/** UserService contract implementation class
 *
 * @author Mateus W. Machado
 * @since 17/07/2023
 * @version 1.0.0
 */

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    private ModelMapper mapper;

    private InfoUserCPF infoUserCPF;

    @Override
    public UserStatusDTO validateUserCPF(String cpf) {
        return infoUserCPF.validateUserCPF(cpf);
    }

    @Override
    public String saveUser(UserDTO userDTO) {
        User savedUser = repository.save(mapper.map(userDTO, User.class));

        log.info("UserServiceImpl.saveUser - End creating associate - Saved User [{}] ", savedUser);

        return "Created user with id "+ savedUser.getId();
    }

    @Override
    public UserDTO findByCpf(String cpf) {
        User user = repository.findByCpf(cpf).orElseThrow(() -> new EntityNotFoundException("User not found"));

        return mapper.map(user, UserDTO.class);
    }
}
