package com.querotattoo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querotattoo.controllers.exceptions.StandardError;
import com.querotattoo.entities.Artist;
import com.querotattoo.entities.Customer;
import com.querotattoo.entities.Schedule;
import com.querotattoo.entities.TattooEstimate;
import com.querotattoo.entities.dto.DTOMapper;
import com.querotattoo.entities.dto.TattooEstimateReadDTO;
import com.querotattoo.services.TattooEstimateService;
import com.querotattoo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Map;

import static com.querotattoo.Constants.ORÇAMENTO_APROVADO;
import static com.querotattoo.Constants.ORÇAMENTO_CRIADO;

@RestController
@RequestMapping("/estimates")
public class TattooEstimateController {

    private static Logger logger = LoggerFactory.getLogger(TattooEstimateController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private TattooEstimateService tattooEstimateService;
    @Autowired
    private DTOMapper mapper;


    @ResponseBody
    @PatchMapping("/approve/{id}")
    public ResponseEntity<TattooEstimateReadDTO> approve(@PathVariable(value = "id") Long id, @RequestBody Map<String, Object> fieldsToUpdate) throws StandardError {
        TattooEstimate estimateToApprove = tattooEstimateService.findById(id);
        if (estimateToApprove == null || estimateToApprove.getStatus() == null) {
            throw new StandardError(500, "Erro na aprovação de orçamento", "Orçamento Não encontrado");
        }
        if (estimateToApprove.getStatus().equals(ORÇAMENTO_APROVADO)) {
            throw new StandardError(500, "Erro na aprovação de orçamento", "Este orçamento já foi aprovado");
        }
        if (estimateToApprove.getStatus().equals(ORÇAMENTO_CRIADO)) {
            estimateToApprove.setStatus(ORÇAMENTO_APROVADO);
            mergeTattooEstimate(estimateToApprove, fieldsToUpdate);
            tattooEstimateService.merge(estimateToApprove);
        }
        return ResponseEntity.ok().body(mapper.toDto(estimateToApprove));

    }

    private void mergeTattooEstimate(TattooEstimate estimateToApprove, Map<String, Object> fieldsToUpdate) {
        ObjectMapper mapper = new ObjectMapper();
        Object newEstimate = mapper.convertValue(fieldsToUpdate, TattooEstimate.class);


        Object finalEstimate = newEstimate;
        fieldsToUpdate.forEach((fieldName, fieldValue) -> {
            Field field = ReflectionUtils.findField(TattooEstimate.class, fieldName);
            field.setAccessible(true);

            Object newValue = ReflectionUtils.getField(field, finalEstimate);
            ReflectionUtils.setField(field, estimateToApprove, newValue);
        });
    }

    @ResponseBody
    @GetMapping()
    public ResponseEntity<Page<TattooEstimate>> findAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        Page<TattooEstimate> tattooEstimatePage = tattooEstimateService.findAll(page, size);
        return ResponseEntity.ok().body(tattooEstimatePage);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<TattooEstimateReadDTO> findById(@PathVariable(value = "id") Long id) {
        TattooEstimate tattooEstimate = tattooEstimateService.findById(id);
        return ResponseEntity.ok().body(mapper.toDto(tattooEstimate));
    }


    @ResponseBody
    @PostMapping()
    public ResponseEntity<TattooEstimate> create(@RequestBody @Valid TattooEstimate estimate) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(estimate.getId()).toUri();
        try {
            estimate.setArtist((Artist) userService.findById(estimate.getArtist().getId()));
            estimate.setCustomer((Customer) userService.findById(estimate.getCustomer().getId()));
            estimate.setStatus(ORÇAMENTO_CRIADO);
            return ResponseEntity.created(uri).body(tattooEstimateService.save(estimate));
        } catch (ClassCastException e) {
            throw new ClassCastException("Este usuário não é um Tatuador.");
        }
    }

    private void setScheduleAddress(Schedule schedule, Long artistId) {
        Artist artist = (Artist) userService.findById(artistId);
        schedule.setAddress(artist.getAddresses().stream().filter(studioAddress -> studioAddress.getCep().equals(schedule.getAddress())).findFirst().get().toString());
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") Long id) {
        tattooEstimateService.delete(id);
    }

}