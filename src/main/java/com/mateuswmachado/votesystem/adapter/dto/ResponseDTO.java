package com.mateuswmachado.votesystem.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {

    private String field;
    private String error;

}

