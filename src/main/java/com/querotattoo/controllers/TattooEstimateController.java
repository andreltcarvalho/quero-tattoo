package com.querotattoo.controllers;

import com.querotattoo.entities.Artist;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

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
    @GetMapping()
    public ResponseEntity<Page<TattooEstimate>> findAll(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
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
    @PostMapping("/artistId/{artistId}")
    public ResponseEntity<TattooEstimate> create(@RequestBody @Valid TattooEstimate estimate, @PathVariable(value = "artistId") Long artistId) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(estimate.getId()).toUri();
        try {
            estimate.setArtist((Artist) userService.findById(artistId));
            estimate.setStatus("Orçamento Criado.");
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