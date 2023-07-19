package com.mateuswmachado.votesystem.adapter.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class VoteDTO {

    @NotBlank(message = "Field cannot be empty")
    private String cpf;
    @NotBlank(message = "Field cannot be empty")
    private String vote;
    @NotNull(message = "Field cannot be null")
    private Long idSchedule;

}