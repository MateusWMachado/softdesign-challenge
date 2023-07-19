package com.mateuswmachado.votesystem.domain.exception;

public class ScheduleNotFoundException extends Exception{

    public ScheduleNotFoundException(Long id) {
        super("Schedule not found with id " + id);
    }
}
