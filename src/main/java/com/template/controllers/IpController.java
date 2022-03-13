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
public class IpController {

    @Autowired
    IpService ipService;
    private static Logger logger = LoggerFactory.getLogger(IpController.class);

    @ResponseBody
    @GetMapping("/loaderio-6588074480623f33d655dc4bc3445277")
    public String test() {
        return "loaderio-6588074480623f33d655dc4bc3445277";
    }

    @PostMapping("/ip")
    public void getIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        ipService.insert(new IpAdress(null, request.getRemoteAddr()));
        //200ok
    }
}