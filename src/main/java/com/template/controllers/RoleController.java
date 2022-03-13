package com.template.controllers;

import com.template.entities.Role;
import com.template.entities.UserEntity;
import com.template.services.RoleService;
import com.template.services.SenderMailService;
import com.template.services.UserEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
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