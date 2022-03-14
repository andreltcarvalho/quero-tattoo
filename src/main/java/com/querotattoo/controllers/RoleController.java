package com.querotattoo.controllers;

import com.querotattoo.entities.Role;
import com.querotattoo.services.RoleService;
import com.querotattoo.services.SenderMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    private static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private SenderMailService mailSender;

    @Autowired
    private RoleService roleService;


    @GetMapping("/roles")
    public ResponseEntity<List<Role>> findAllRoles() {
        List<Role> list = roleService.findAll();
        return ResponseEntity.ok().body(list);
    }
}