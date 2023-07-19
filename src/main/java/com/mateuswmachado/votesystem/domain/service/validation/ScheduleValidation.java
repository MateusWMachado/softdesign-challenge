package com.mateuswmachado.votesystem.domain.service.validation;

import com.mateuswmachado.votesystem.adapter.dto.UserDTO;
import com.mateuswmachado.votesystem.adapter.dto.UserStatusDTO;
import com.mateuswmachado.votesystem.domain.enums.UserStatus;
import com.mateuswmachado.votesystem.port.inbound.UserService;
import com.mateuswmachado.votesystem.domain.exception.UserAlreadyVotedException;
import com.mateuswmachado.votesystem.domain.exception.ScheduleException;
import com.mateuswmachado.votesystem.domain.service.rest.InfoUserCPF;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
public class ScheduleValidation {

    private UserService userService;
    private InfoUserCPF infoUserCPF;

    public void validateVotes(List<UserDTO> votes, String cpfUser) {
        Optional<UserDTO> vote = votes.stream().filter(v -> v.getCpf().equals(cpfUser)).findAny();
        if(vote.isPresent()) {
            log.info("User already voted");
            throw new UserAlreadyVotedException("User already voted");
        } else {
            UserDTO userDTO = userService.findByCpf(cpfUser);
            validateIfUserCanVote(cpfUser);
            votes.add(userDTO);
        }
    }

    public void validateIfUserCanVote(String cpf)  {
        UserStatusDTO userStatusDTO = infoUserCPF.validateUserCPF(cpf);
        if(UserStatus.ABLE_TO_VOTE.name().equals(userStatusDTO.getStatus())) {
            log.info("ScheduleValidation.validateIfUserCanVote - user cpf result: [{}]", cpf);
        } else if (UserStatus.UNABLE_TO_VOTE.name().equals(userStatusDTO.getStatus())) {
            log.info("ScheduleValidation.validateIfUserCanVote - user cpf result: [{}]", cpf);
            throw new ScheduleException("The User with CPF: "+ cpf +", is not allowed to vote");
        }
    }
}
