package com.mateuswmachado.votesystem.adapter.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteSessionDTO {

    private Long id;
    private ScheduleDTO schedule;
    private LocalDateTime endTime;
    private LocalDateTime startTime;
    private Integer duration;
    @JsonIgnore
    private Long idSchedule;
}
