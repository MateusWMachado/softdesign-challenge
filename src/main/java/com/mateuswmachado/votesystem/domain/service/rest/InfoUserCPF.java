package com.mateuswmachado.votesystem.domain.service.rest;

import br.com.caelum.stella.validation.CPFValidator;
import com.mateuswmachado.votesystem.adapter.dto.UserStatusDTO;
import com.mateuswmachado.votesystem.domain.enums.UserStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** Validation class to know if the associate's cpf is valid
 *
 * @author Mateus W. Machado
 * @since 17/07/2023
 * @version 1.0.0
 */

@Slf4j
@Component
public class InfoUserCPF {

    public UserStatusDTO validateUserCPF(String cpf) {
        log.info("InfoUserCPF.validateUserCPF - CPF: [{}]", cpf);
        CPFValidator cpfValidator = new CPFValidator();

        boolean eligible = cpfValidator.isEligible(cpf);

        if (eligible) {
            return new UserStatusDTO(UserStatus.ABLE_TO_VOTE.name());
        }

        log.info("InfoUserCPF.validateUserCPF - results: [{}]", eligible);

        return new UserStatusDTO(UserStatus.UNABLE_TO_VOTE.name());
    }

}
