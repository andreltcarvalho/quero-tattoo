package com.querotattoo.controllers;

import com.querotattoo.controllers.exceptions.StandardError;
import com.querotattoo.entities.Artist;
import com.querotattoo.entities.Schedule;
import com.querotattoo.entities.TattooEstimate;
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

import static com.querotattoo.Constants.*;

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
    public ResponseEntity<Schedule> create(@RequestBody @Valid Schedule schedule) throws StandardError {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(schedule.getId()).toUri();
        TattooEstimate tattooEstimate = tattooEstimateService.findById(schedule.getTattooEstimate().getId());

        if (!tattooEstimate.getStatus().equals(ORÇAMENTO_APROVADO)) {
            throw new StandardError(500, "Erro no agendamento", "Não é possível agendar, pois o orçamento ainda não foi aprovado.");
        }
        checkIfDateIsAvailableToSchedule(schedule);
        schedule.setDateToLog(DateUtils.getNow());
        schedule.setStatus(AGENDAMENTO_CRIADO);
        logger.info("Agendamento criado para o dia: " + schedule.getEventDate() + ". ID-Cliente: " + tattooEstimate.getCustomer().getId() + ". ID-Artista: " + tattooEstimate.getArtist().getId());
        return ResponseEntity.created(uri).body(scheduleService.save(schedule));
    }

    private void checkIfDateIsAvailableToSchedule(Schedule schedule) throws StandardError {
        Schedule scheduleToCompareDate = scheduleService.findByEventDate(schedule.getEventDate());
        if (scheduleToCompareDate.getEventDate() != null && scheduleToCompareDate.getEventDate().compareTo(schedule.getEventDate()) == 0) {
            throw new StandardError(500, "Erro no agendamento", "O horário " + scheduleToCompareDate.getEventDate() + " não está disponível, escolha outro horário");
        }
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
        logger.info("Agendamento deletado com o id: " + id);
    }

    @ResponseBody
    @PatchMapping("/approve/{id}")
    public ResponseEntity<Schedule> approve(@PathVariable(value = "id") Long id) throws StandardError {

        Schedule scheduleToApprove = scheduleService.findById(id);

        if (scheduleToApprove == null || scheduleToApprove.getStatus() == null) {
            throw new StandardError(500, "Erro na aprovação de agendamento", "Agendamento não encontrado");
        }
        if (scheduleToApprove.getStatus().equals(AGENDAMENTO_APROVADO)) {
            throw new StandardError(500, "Erro na aprovação de agendamento", "Este agendamento já foi aprovado");
        }
        if (scheduleToApprove.getStatus().equals(AGENDAMENTO_CRIADO)) {
            scheduleToApprove.setStatus(AGENDAMENTO_APROVADO);
            scheduleService.merge(scheduleToApprove);
            logger.info("Agendamento aprovado para o dia: " + scheduleToApprove.getEventDate() + ". ID-Cliente: " + scheduleToApprove.getTattooEstimate().getCustomer().getId() + ". ID-Artista: " + scheduleToApprove.getTattooEstimate().getArtist().getId());
        }

        return ResponseEntity.ok().body(scheduleToApprove);
    }
}