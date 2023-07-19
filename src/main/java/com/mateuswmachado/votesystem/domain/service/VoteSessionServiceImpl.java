package com.mateuswmachado.votesystem.domain.service;

import com.mateuswmachado.votesystem.adapter.dto.ScheduleDTO;
import com.mateuswmachado.votesystem.adapter.dto.VoteSessionDTO;
import com.mateuswmachado.votesystem.domain.exception.ScheduleException;
import com.mateuswmachado.votesystem.domain.exception.ScheduleNotFoundException;
import com.mateuswmachado.votesystem.domain.Schedule;
import com.mateuswmachado.votesystem.domain.VoteSession;
import com.mateuswmachado.votesystem.port.inbound.ScheduleService;
import com.mateuswmachado.votesystem.port.inbound.VoteSessionService;
import com.mateuswmachado.votesystem.port.outbound.VoteSessionRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/** Class of service responsible for opening voting sessions
 *
 * @author Mateus W. Machado
 * @since 17/07/2023
 * @version 1.0.0
 */

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class VoteSessionServiceImpl implements VoteSessionService {

    private ModelMapper modelMapper;
    private final VoteSessionRepository voteSessionRepository;
    private final ScheduleService scheduleService;

    public VoteSessionDTO openVotingSession(VoteSession voteSession) throws ScheduleNotFoundException {

        log.info("VoteSessionService.openVotingSession - starting a voting session");

        ScheduleDTO scheduleDTO = scheduleService.verifyIfScheduleExists(voteSession.getIdSchedule());

        if (Objects.nonNull(scheduleDTO.getVoteSession())) {
            throw new ScheduleException("Schedule already passed session");
        }

        setTimeSession(voteSession);

        voteSession.setEndTime(voteSession.getStartTime().plusMinutes(voteSession.getDuration()));
        voteSession.setSchedule(modelMapper.map(scheduleDTO, Schedule.class));
        voteSessionRepository.save(voteSession);

        log.info("VoteSessionService.openVotingSession - voting session created. results: [{}]", voteSession);

        return modelMapper.map(voteSession, VoteSessionDTO.class);
    }

    private void setTimeSession(VoteSession voteSession) {
        if (Objects.isNull(voteSession.getStartTime())) {
            voteSession.setStartTime(LocalDateTime.now());
        }
        if (Objects.isNull(voteSession.getDuration())) {
            voteSession.setDuration(1);
        }
    }

}