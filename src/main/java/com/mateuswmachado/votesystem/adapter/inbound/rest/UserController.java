package com.mateuswmachado.votesystem.adapter.inbound.rest;

import com.mateuswmachado.votesystem.adapter.dto.UserDTO;
import com.mateuswmachado.votesystem.adapter.dto.UserStatusDTO;
import com.mateuswmachado.votesystem.domain.enums.UserStatus;
import com.mateuswmachado.votesystem.port.inbound.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

/** Class that represents a controller to handle information from Users
 *
 * @author Mateus W. Machado
 * @since 17/07/2023
 * @version 1.0.0
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private UserService userService;

    @GetMapping("/{cpf}")
    @ApiOperation("Validate user's CPF")
    public ResponseEntity<UserStatusDTO> validateUsersCPF(@PathVariable String cpf) {
        UserStatusDTO userStatusDTO = userService.validateUserCPF(cpf);
        log.info("UserController.validateUsersCPF - user status [{}] ", userStatusDTO.getStatus());

        if (userStatusDTO.getStatus().equals(UserStatus.UNABLE_TO_VOTE.name())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userStatusDTO);
        }

        return ResponseEntity.ok(userStatusDTO);
    }

    @PostMapping
    @ApiOperation("Create an User")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDTO associateDTO) {
        log.info("UserController.createUser - start creating user - user info [{}] ", associateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(associateDTO));
    }
}
