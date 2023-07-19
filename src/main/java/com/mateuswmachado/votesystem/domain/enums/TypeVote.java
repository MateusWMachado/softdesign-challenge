package com.mateuswmachado.votesystem.domain.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TypeVote {

    @JsonProperty("SIM")
    SIM,
    @JsonProperty("NÃO")
    NAO;
}

