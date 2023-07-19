package com.mateuswmachado.votesystem.adapter.inbound.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateuswmachado.votesystem.adapter.dto.VoteSessionDTO;
import com.mateuswmachado.votesystem.domain.VoteSession;
import com.mateuswmachado.votesystem.domain.exception.ScheduleNotFoundException;
import com.mateuswmachado.votesystem.domain.service.VoteSessionServiceImpl;
import com.mateuswmachado.votesystem.port.inbound.VoteSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(VoteSessionController.class)
public class VoteSessionControllerTest {

    @MockBean
    private VoteSessionService voteSessionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Test create voting session - Valid VoteSession")
    @Test
    void createVotingSession_ValidVoteSession_ReturnsCreatedResponse() throws Exception {
        VoteSession voteSession = new VoteSession();
        voteSession.setIdSchedule(1L);
        voteSession.setStartTime(LocalDateTime.now());
        voteSession.setDuration(60);

        VoteSessionDTO voteSessionDTO = new VoteSessionDTO();
        voteSessionDTO.setId(1L);
        voteSessionDTO.setIdSchedule(1L);
        voteSessionDTO.setStartTime(voteSession.getStartTime());
        voteSessionDTO.setDuration(voteSession.getDuration());

        Mockito.when(voteSessionService.openVotingSession(Mockito.any(VoteSession.class)))
                .thenReturn(voteSessionDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/vote-session")
                        .content(objectMapper.writeValueAsString(voteSession))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @DisplayName("Test create voting session - Invalid VoteSession")
    @Test
    void createVotingSession_InvalidVoteSession_ReturnsCreatedResponse() throws Exception {
        VoteSession voteSession = new VoteSession();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/vote-session")
                        .content(objectMapper.writeValueAsString(voteSession))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("Test create voting session - Schedule not found")
    @Test
    void createVotingSession_ScheduleNotFound_ReturnsNotFoundResponse() throws Exception {
        VoteSession voteSession = new VoteSession();
        voteSession.setIdSchedule(1L);
        voteSession.setStartTime(LocalDateTime.now());
        voteSession.setDuration(60);

        Mockito.when(voteSessionService.openVotingSession(Mockito.any(VoteSession.class)))
                .thenThrow(new ScheduleNotFoundException(1L));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/vote-session")
                        .content(objectMapper.writeValueAsString(voteSession))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}