package com.querotattoo.controllers;

import com.querotattoo.entities.Role;
import com.querotattoo.services.RoleService;
import com.querotattoo.services.SenderMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/roles")
public class RoleController {

    private static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private SenderMailService mailSender;

    @Autowired
    private RoleService roleService;


    @GetMapping()
    public ResponseEntity<List<Role>> findAllRoles() {
        List<Role> list = roleService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Role>> search(@RequestParam("searchTerm") String searchTerm,
                                             @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                             @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        Page<Role> rolePage = roleService.search(searchTerm, page, size);
        return ResponseEntity.ok(rolePage);
    }
}