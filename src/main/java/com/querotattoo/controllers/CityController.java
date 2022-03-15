package com.querotattoo.controllers;

import com.querotattoo.entities.City;
import com.querotattoo.entities.State;
import com.querotattoo.services.CityService;
import com.querotattoo.services.StateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController()
public class CityController {

    private static Logger logger = LoggerFactory.getLogger(CityController.class);

    @Autowired
    private CityService cityService;

    @Autowired
    private StateService stateService;

    @ResponseBody
    @GetMapping("/states")
    public ResponseEntity<List<State>> findAllStates() {
        List<State> list = stateService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @ResponseBody
    @GetMapping("states/{id}")
    public ResponseEntity<State> findStatesById(@PathVariable(value = "id") Long id) {
        State state = stateService.findById(id);
        return ResponseEntity.ok().body(state);
    }

    @ResponseBody
    @PostMapping("/states")
    public ResponseEntity<State> registerStates(@RequestBody State stateForm) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(stateForm.getId()).toUri();
        State state = stateService.insert(stateForm);
        return ResponseEntity.created(uri).body(state);
    }


    @ResponseBody
    @GetMapping("/cities")
    public ResponseEntity<List<City>> findAll() {
        List<City> list = cityService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @ResponseBody
    @GetMapping("cities/{id}")
    public ResponseEntity<City> findAll(@PathVariable(value = "id") Long id) {
        City city = cityService.findById(id);
        return ResponseEntity.ok().body(city);
    }

    @ResponseBody
    @DeleteMapping("cities/{id}")
    public void delete(@PathVariable(value = "id") Long id) {
        cityService.delete(id);
    }

    @ResponseBody
    @PostMapping("/cities")
    public ResponseEntity<City> registration(@RequestBody City cityForm) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cityForm.getId()).toUri();
        City city = cityService.insert(cityForm);
        return ResponseEntity.created(uri).body(city);
    }

}