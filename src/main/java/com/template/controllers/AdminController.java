package com.template.controllers;

import com.template.services.UserEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Controller
public class AdminController {

    @Autowired
    private UserEntityService userEntityService;


    private static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/admin")
    public ModelAndView admin() {
        return new ModelAndView("admin")
                .addObject("users", userEntityService.findAll());
    }

    @GetMapping("/userData/{email}")
    public ModelAndView userData(@PathVariable String email) {
        return new ModelAndView("userData").addObject("user", userEntityService.findByEmail(email));
    }
}