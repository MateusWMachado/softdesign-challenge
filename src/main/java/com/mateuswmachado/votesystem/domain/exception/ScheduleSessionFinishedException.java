package com.mateuswmachado.votesystem.domain.exception;

public class ScheduleSessionFinishedException extends RuntimeException{

    public ScheduleSessionFinishedException(String message) {
        super(message);
    }
}
