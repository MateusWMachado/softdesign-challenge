package com.mateuswmachado.votesystem.adapter.inbound.rest;

import com.mateuswmachado.votesystem.adapter.dto.ResponseDTO;
import com.mateuswmachado.votesystem.adapter.dto.ResponseMessageErrorDTO;
import com.mateuswmachado.votesystem.domain.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Class responsible for handling form errors
 *
 * @author Mateus W. Machado
 * @since 17/07/2023
 * @version 1.0.0
 */

@RestControllerAdvice
public class ControllerAdvice {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ResponseDTO> handlePayloadNotValidException(MethodArgumentNotValidException exception) {
        List<ResponseDTO> errorDTO = new ArrayList<>();

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ResponseDTO error = new ResponseDTO(e.getField(), message);
            errorDTO.add(error);
        });

        return errorDTO;
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class, ScheduleHasNoVotingSessionException.class, ScheduleNotFoundException.class, EntityNotFoundException.class})
    public ResponseMessageErrorDTO handleNotFoundException(Exception ex) {
        return handleMessagePayloadError("404","NOT_FOUND_404", ex.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserAlreadyVotedException.class, InvalidVoteException.class, ScheduleException.class, ScheduleSessionFinishedException.class})
    public ResponseMessageErrorDTO handleUserAlreadyVotedException(Exception ex) {
        return handleMessagePayloadError("400", "BAD_REQUEST_400", ex.getMessage());
    }

    private ResponseMessageErrorDTO handleMessagePayloadError(String code, String title, String detail) {
        ResponseMessageErrorDTO responseMessageErrorDTO = new ResponseMessageErrorDTO();

        responseMessageErrorDTO.setCode(code);
        responseMessageErrorDTO.setDetail(detail);
        responseMessageErrorDTO.setTitle(title);
        responseMessageErrorDTO.setRequestDateTime(String.valueOf(LocalDateTime.now()));

        return responseMessageErrorDTO;
    }

}
