package com.template.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {
    private static Logger logger = LoggerFactory.getLogger(AccessDeniedController.class);

    @GetMapping("/accessDenied")
    public String viewHomePage() {
        logger.info("Acesso negado para o endpoint Admin!");
        return "accessDenied";
    }
}