package com.mateuswmachado.votesystem.adapter.inbound.rest;

import com.mateuswmachado.votesystem.adapter.dto.ScheduleDTO;
import com.mateuswmachado.votesystem.adapter.dto.ScheduleResultDTO;
import com.mateuswmachado.votesystem.domain.exception.ScheduleNotFoundException;
import com.mateuswmachado.votesystem.port.inbound.ScheduleService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/** Class that represents a controller to handle information about schedules
 *
 * @author Mateus W. Machado
 * @since 17/07/2023
 * @version 1.0.0
 */

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/schedule")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ScheduleController {

    private ScheduleService scheduleService;

    @PostMapping
    @ApiOperation("Create a Schedule")
    public ResponseEntity<ScheduleDTO> createSchedule(@RequestBody @Valid ScheduleDTO scheduleDTO, UriComponentsBuilder uriBuilder) {
        ScheduleDTO schedule = scheduleService.createSchedule(scheduleDTO);
        URI uri = uriBuilder.path("/api/v1/schedule/{id}").buildAndExpand(schedule.getId()).toUri();
        log.info("UserController.createSchedule - start creating schedule - schedule info [{}] ", schedule);
        return ResponseEntity.created(uri).body(schedule);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get Schedule Result")
    public ResponseEntity<ScheduleResultDTO> getScheduleResult(@PathVariable Long id) throws ScheduleNotFoundException {
        log.info("ScheduleController.getResult - get schedule by id - schedule id: [{}]", id);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.scheduleResult(id));
    }

}
