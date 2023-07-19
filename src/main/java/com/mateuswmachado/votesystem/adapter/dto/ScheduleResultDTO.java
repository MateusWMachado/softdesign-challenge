package com.mateuswmachado.votesystem.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ScheduleResultDTO {

    private Long id;
    private int yesVote;
    private int noVote;
    private String subject;
    private String result;
    private List<UserDTO> votes;
}

