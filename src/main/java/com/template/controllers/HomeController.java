package com.template.controllers;

import com.template.entities.IpAdress;
import com.template.services.IpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @Autowired
    IpService ipService;
    private static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping({"/", "/home"})
    public ModelAndView start() {
        ModelAndView home = new ModelAndView("home").addObject("showDialog", false);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (ipService.findByIp(request.getRemoteAddr()) == null) {
            home.addObject("showDialog", true);
        }
        return home;
    }

    @ResponseBody
    @GetMapping("/loaderio-6588074480623f33d655dc4bc3445277")
    public String test() {
        return "loaderio-6588074480623f33d655dc4bc3445277";
    }

    @PostMapping("/ip")
    public ModelAndView getIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        ipService.insert(new IpAdress(null, request.getRemoteAddr()));
        return new ModelAndView("redirect:/home");
    }
}