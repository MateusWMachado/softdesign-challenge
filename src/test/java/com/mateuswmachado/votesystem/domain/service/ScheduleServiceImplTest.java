package com.mateuswmachado.votesystem.domain.service;

import com.mateuswmachado.votesystem.adapter.dto.ScheduleDTO;
import com.mateuswmachado.votesystem.adapter.dto.VoteDTO;
import com.mateuswmachado.votesystem.adapter.dto.VoteSessionDTO;
import com.mateuswmachado.votesystem.domain.Schedule;
import com.mateuswmachado.votesystem.domain.exception.ScheduleHasNoVotingSessionException;
import com.mateuswmachado.votesystem.domain.exception.ScheduleNotFoundException;
import com.mateuswmachado.votesystem.domain.service.validation.ScheduleValidation;
import com.mateuswmachado.votesystem.port.outbound.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ScheduleServiceImplTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleValidation scheduleValidation;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create Schedule - Valid ScheduleDTO - Returns Created Response")
    void createSchedule_ValidScheduleDTO_ReturnsCreatedResponse() {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(1L);
        Schedule schedule = new Schedule();
        schedule.setId(1L);

        when(modelMapper.map(scheduleDTO, Schedule.class)).thenReturn(schedule);

        when(scheduleRepository.save(schedule)).thenReturn(schedule);

        ScheduleDTO createdScheduleDTO = scheduleService.createSchedule(scheduleDTO);

        verify(modelMapper).map(scheduleDTO, Schedule.class);
        verify(scheduleRepository).save(schedule);
    }

    @Test
    @DisplayName("Verify If Schedule Exists - Existing ScheduleId - Returns ScheduleDTO")
    void verifyIfScheduleExists_ExistingScheduleId_ReturnsScheduleDTO() throws ScheduleNotFoundException {
        Long scheduleId = 1L;
        Schedule schedule = new Schedule();
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(modelMapper.map(schedule, ScheduleDTO.class)).thenReturn(scheduleDTO);

        ScheduleDTO result = scheduleService.verifyIfScheduleExists(scheduleId);

        assertEquals(scheduleDTO, result);
        verify(scheduleRepository).findById(scheduleId);
        verify(modelMapper).map(schedule, ScheduleDTO.class);
    }

    @Test
    @DisplayName("Verify If Schedule Exists - Nonexistent ScheduleId - Throws ScheduleNotFoundException")
    void verifyIfScheduleExists_NonexistentScheduleId_ThrowsScheduleNotFoundException() {
        Long scheduleId = 1L;
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        assertThrows(ScheduleNotFoundException.class, () -> scheduleService.verifyIfScheduleExists(scheduleId));
        verify(scheduleRepository).findById(scheduleId);
    }

    @Test
    @DisplayName("Vote Schedule - Valid VoteDTO - Returns VoteDTO")
    void voteSchedule_ValidVoteDTO_ReturnsVoteDTO() throws ScheduleNotFoundException {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setIdSchedule(1L);
        voteDTO.setVote("SIM");
        voteDTO.setCpf("12312312300");
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setVotes(List.of());
        VoteSessionDTO voteSessionDTO = new VoteSessionDTO();
        voteSessionDTO.setEndTime(LocalDateTime.now().plusMinutes(1));
        scheduleDTO.setVoteSession(voteSessionDTO);

        when(scheduleRepository.findById(voteDTO.getIdSchedule())).thenReturn(Optional.of(new Schedule()));
        when(scheduleService.verifyIfScheduleExists(voteDTO.getIdSchedule())).thenReturn(scheduleDTO);

        doNothing().when(scheduleValidation).validateVotes(anyList(), anyString());
        when(modelMapper.map(scheduleDTO, Schedule.class)).thenReturn(new Schedule());

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(new Schedule());

        VoteDTO result = scheduleService.voteSchedule(voteDTO);

        assertEquals(voteDTO, result);
        verify(scheduleValidation).validateVotes(List.of(), "12312312300");
        verify(modelMapper).map(scheduleDTO, Schedule.class);
        verify(scheduleRepository).save(any(Schedule.class));
    }

    @Test
    @DisplayName("Vote Schedule - Schedule Has No Voting Session - Throws ScheduleHasNoVotingSessionException")
    public void voteSchedule_ScheduleHasNoVotingSession_ThrowsScheduleHasNoVotingSessionException() throws ScheduleNotFoundException {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setIdSchedule(1L);
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(1L);

        when(scheduleRepository.findById(voteDTO.getIdSchedule())).thenReturn(Optional.of(new Schedule()));
        when(scheduleService.verifyIfScheduleExists(voteDTO.getIdSchedule())).thenReturn(scheduleDTO);

        assertThrows(ScheduleHasNoVotingSessionException.class, () -> scheduleService.voteSchedule(voteDTO));
        verify(scheduleRepository, times(2)).findById(voteDTO.getIdSchedule());
    }

    @Test
    @DisplayName("Vote Schedule - Schedule Not Found - Throws ScheduleNotFoundException")
    void voteSchedule_ScheduleNotFound_ThrowsScheduleNotFoundException() {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setIdSchedule(1L);

        assertThrows(ScheduleNotFoundException.class, () -> scheduleService.voteSchedule(voteDTO));
    }
}