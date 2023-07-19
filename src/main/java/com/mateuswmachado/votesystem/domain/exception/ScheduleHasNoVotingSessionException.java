package com.mateuswmachado.votesystem.domain.exception;

public class ScheduleHasNoVotingSessionException extends RuntimeException{

    public ScheduleHasNoVotingSessionException(String message) {
        super(message);
    }
}
