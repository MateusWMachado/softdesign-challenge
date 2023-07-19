package com.mateuswmachado.votesystem.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessageErrorDTO {

    private String code;

    private String title;

    private String detail;

    private String requestDateTime;
}
