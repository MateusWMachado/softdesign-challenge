package com.mateuswmachado.votesystem.adapter.inbound.rest;

import com.mateuswmachado.votesystem.adapter.dto.ScheduleDTO;
import com.mateuswmachado.votesystem.adapter.dto.VoteDTO;
import com.mateuswmachado.votesystem.domain.exception.ScheduleNotFoundException;
import com.mateuswmachado.votesystem.port.inbound.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.mateuswmachado.votesystem.utils.JsonTestUtil.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ScheduleService scheduleService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        VoteController voteController = new VoteController(scheduleService);
        mockMvc = MockMvcBuilders.standaloneSetup(voteController).build();
    }

    @DisplayName("Test vote - Valid VoteDTO")
    @Test
    void vote_ValidVoteDTO_ReturnsCreatedResponse() throws Exception {
        VoteDTO voteDTO = new VoteDTO();
        voteDTO.setIdSchedule(1L);
        voteDTO.setVote("SIM");
        voteDTO.setCpf("12312312300");

        when(scheduleService.verifyIfScheduleExists(any(Long.class)))
                .thenReturn(new ScheduleDTO());
        when(scheduleService.voteSchedule(any(VoteDTO.class)))
                .thenReturn(new VoteDTO());

        mockMvc.perform(post("/api/v1/vote")
                        .content(asJsonString(voteDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());
    }

    @DisplayName("Test vote - Invalid VoteDTO")
    @Test
    void vote_InvalidVoteDTO_ReturnsBadRequest() throws Exception {
        VoteDTO voteDTO = new VoteDTO();

        mockMvc.perform(post("/api/v1/vote")
                        .content(asJsonString(voteDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
    }

    @DisplayName("Test vote - ScheduleNotFoundException")
    @Test
    void vote_ScheduleNotFoundException_ReturnsNotFoundResponse() throws Exception {
        VoteDTO voteDTO = new VoteDTO();

        when(scheduleService.voteSchedule(voteDTO)).thenThrow(new ScheduleNotFoundException(1L));

        mockMvc.perform(post("/api/v1/vote")
                        .content(asJsonString(voteDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
    }

}


