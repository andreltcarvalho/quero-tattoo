package com.querotattoo.controllers;

import com.querotattoo.entities.User;
import com.querotattoo.services.SenderMailService;
import com.querotattoo.services.UserEntityService;
import com.querotattoo.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserEntityService userEntityService;

    @Autowired
    private SenderMailService mailService;

    @GetMapping("/login")
    public ModelAndView viewLoginPage() {
        ModelAndView login = new ModelAndView("login");
        if (HttpUtils.isLogged()) {
            return new ModelAndView("home");
        }
        login.addObject("login", "true");
        return login;
    }


    @PostMapping("/forgotPassword")
    public void changePassword(@RequestParam String email) {
        User user = userEntityService.findByEmail(email);
        if (user != null && user.isEnabled() == true) {
            mailService.sendNewPasswordEmail(user);
            //200 ok
        } else {
            //500 erro
        }
    }


    @PostMapping("/newPassword")
    public void postNewPassword(@RequestParam String verificationCode, @RequestParam String password) {
        User user = userEntityService.findByVerificationCode(verificationCode);
        if (user != null && user.isEnabled() == true) {
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            user.setVerificationCode(null);
            userEntityService.update(user);
            logger.info("User " + user.getEmail() + " changed password");
            //200 ok
        } else {
            //400 erro
        }
    }
}