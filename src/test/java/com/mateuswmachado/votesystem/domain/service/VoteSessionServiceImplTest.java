package com.mateuswmachado.votesystem.domain.service;

import com.mateuswmachado.votesystem.adapter.dto.ScheduleDTO;
import com.mateuswmachado.votesystem.adapter.dto.VoteSessionDTO;
import com.mateuswmachado.votesystem.domain.VoteSession;
import com.mateuswmachado.votesystem.domain.exception.ScheduleException;
import com.mateuswmachado.votesystem.domain.exception.ScheduleNotFoundException;
import com.mateuswmachado.votesystem.port.inbound.ScheduleService;
import com.mateuswmachado.votesystem.port.outbound.VoteSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VoteSessionServiceImplTest {

    private VoteSessionServiceImpl voteSessionService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private VoteSessionRepository voteSessionRepository;

    @Mock
    private ScheduleService scheduleService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        voteSessionService = new VoteSessionServiceImpl(modelMapper, voteSessionRepository, scheduleService);
    }

    @Test
    @DisplayName("Test openVotingSession with valid Schedule and new VoteSession")
    void openVotingSession_ValidScheduleAndNewVoteSession_ReturnsVoteSessionDTO() throws ScheduleNotFoundException {
        VoteSession voteSession = new VoteSession();
        voteSession.setIdSchedule(1L);
        voteSession.setDuration(3);

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setSubject("Mock");
        scheduleDTO.setId(1L);

        VoteSessionDTO expectedVoteSessionDTO = new VoteSessionDTO();
        expectedVoteSessionDTO.setIdSchedule(1L);

        when(scheduleService.verifyIfScheduleExists(1L)).thenReturn(scheduleDTO);
        when(voteSessionRepository.save(any(VoteSession.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(modelMapper.map(any(VoteSession.class), eq(VoteSessionDTO.class))).thenReturn(expectedVoteSessionDTO);

        VoteSessionDTO voteSessionDTO = voteSessionService.openVotingSession(voteSession);

        assertNotNull(voteSessionDTO);
        assertEquals(expectedVoteSessionDTO.getIdSchedule(), voteSessionDTO.getIdSchedule());

        verify(scheduleService).verifyIfScheduleExists(1L);
        verify(voteSessionRepository).save(any(VoteSession.class));
    }

    @Test
    @DisplayName("Test openVotingSession with valid Schedule and existing VoteSession")
    void openVotingSession_ValidScheduleAndExistingVoteSession_ThrowsScheduleException() throws ScheduleNotFoundException {
        VoteSession voteSession = new VoteSession();
        voteSession.setIdSchedule(1L);
        voteSession.setStartTime(null);
        voteSession.setDuration(null);

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setVoteSession(new VoteSessionDTO());

        when(scheduleService.verifyIfScheduleExists(1L)).thenReturn(scheduleDTO);

        assertThrows(ScheduleException.class, () -> {
            voteSessionService.openVotingSession(voteSession);
        });

        verify(scheduleService).verifyIfScheduleExists(1L);
        verify(voteSessionRepository, never()).save(any(VoteSession.class));
    }
}
