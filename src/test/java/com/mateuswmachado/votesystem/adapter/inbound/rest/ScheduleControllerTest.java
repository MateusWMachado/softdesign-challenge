package com.mateuswmachado.votesystem.adapter.inbound.rest;

import com.mateuswmachado.votesystem.adapter.dto.ScheduleDTO;
import com.mateuswmachado.votesystem.adapter.dto.ScheduleResultDTO;
import com.mateuswmachado.votesystem.adapter.dto.UserDTO;
import com.mateuswmachado.votesystem.port.inbound.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.mateuswmachado.votesystem.utils.JsonTestUtil.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ScheduleController.class)
public class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Valid creation of schedule test")
    public void testCreateValidSchedule() throws Exception {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setSubject("Test Mock Subject");
        scheduleDTO.setVotes(List.of());

        when(scheduleService.createSchedule(any(ScheduleDTO.class))).thenReturn(scheduleDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(scheduleDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Invalid creation of schedule test")
    public void testCreateInvalidSchedule() throws Exception {
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(scheduleDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Get schedule result test")
    public void testGetScheduleResult() throws Exception {
        ScheduleResultDTO scheduleResultDTO = new ScheduleResultDTO();
        scheduleResultDTO.setResult("SIM");
        scheduleResultDTO.setSubject("Mock");
        scheduleResultDTO.setNoVote(1);
        scheduleResultDTO.setYesVote(5);
        scheduleResultDTO.setId(1L);
        scheduleResultDTO.setVotes(List.of(new UserDTO(1L, "12312312300")));

        when(scheduleService.scheduleResult(scheduleResultDTO.getId())).thenReturn(scheduleResultDTO);

        mockMvc.perform(get("/api/v1/schedule/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
    }



}
