package com.mateuswmachado.votesystem.port.inbound;

import com.mateuswmachado.votesystem.adapter.dto.ScheduleDTO;
import com.mateuswmachado.votesystem.adapter.dto.ScheduleResultDTO;
import com.mateuswmachado.votesystem.adapter.dto.VoteDTO;
import com.mateuswmachado.votesystem.domain.exception.ScheduleNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

/** Interface representing a Schedule service operations contract
 *
 * @author Mateus W. Machado
 * @since 17/07/2023
 * @version 1.0.0
 */


public interface ScheduleService {

    ScheduleDTO createSchedule(ScheduleDTO scheduleDTO);

    ScheduleResultDTO scheduleResult(Long id) throws ScheduleNotFoundException;

    ScheduleDTO verifyIfScheduleExists(Long id) throws ScheduleNotFoundException;

    VoteDTO voteSchedule(VoteDTO voteDTO) throws ScheduleNotFoundException;
}
