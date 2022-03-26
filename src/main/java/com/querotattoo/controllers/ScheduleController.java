package com.querotattoo.controllers;

import com.querotattoo.entities.Artist;
import com.querotattoo.entities.Schedule;
import com.querotattoo.services.ScheduleService;
import com.querotattoo.services.TattooEstimateService;
import com.querotattoo.services.UserService;
import com.querotattoo.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/schedules")
public class ScheduleController {

    private static Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private UserService userService;
    @Autowired
    private TattooEstimateService tattooEstimateService;

    @ResponseBody
    @GetMapping()
    public ResponseEntity<Page<Schedule>> findAll(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        Page<Schedule> schedulePage = scheduleService.findAll(page, size);
        return ResponseEntity.ok().body(schedulePage);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Schedule> findById(@PathVariable(value = "id") Long id) {
        Schedule schedule = scheduleService.findById(id);
        return ResponseEntity.ok().body(schedule);
    }

    @ResponseBody
    @PostMapping()
    public ResponseEntity<Schedule> create(@RequestBody @Valid Schedule schedule) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(schedule.getId()).toUri();

        schedule.setDateToLog(DateUtils.getNow());
        schedule.setStatus("Pedido em aberto, aguardando confirmação do tatuador.");
        return ResponseEntity.created(uri).body(scheduleService.create(schedule));
    }

    private void validateArtistAndSetAddress(Long artistId, Schedule schedule) {
        try {
            Artist artist = (Artist) userService.findById(artistId);
            schedule.setAddress(artist.getAddresses().stream().filter(studioAddress -> studioAddress.getCep().equals(schedule.getAddress())).findFirst().get().toString());
        } catch (ClassCastException e) {
            throw new ClassCastException("Este usuário não é um Tatuador.");
        }
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") Long id) {
        scheduleService.delete(id);
    }
}