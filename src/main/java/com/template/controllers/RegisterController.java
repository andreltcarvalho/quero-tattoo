package com.template.controllers;


import com.template.entities.UserEntity;
import com.template.services.SenderMailService;
import com.template.services.UserEntityService;
import com.template.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Controller
public class RegisterController {

    @Autowired
    private SenderMailService mailSender;

    @Autowired
    private UserEntityService userEntityService;

    @GetMapping("/register")
    public ModelAndView registerPage() {
        if (HttpUtils.isLogged()) {
            return new ModelAndView("redirect:/home");
        }
        return new ModelAndView("register")
                .addObject("UserEntity", new UserEntity());
    }

    @PostMapping("/register")
    public ModelAndView registration(@ModelAttribute("UserEntity") UserEntity userForm) throws MessagingException, UnsupportedEncodingException {
        if (HttpUtils.isLogged()) {
            return new ModelAndView("redirect:/home");
        }
        if (userEntityService.findByEmail(userForm.getEmail()) != null) {
            return new ModelAndView("register")
                    .addObject("errors", "There is already an account registered with the e-mail: " + userForm.getEmail());
        }
        if (userEntityService.findByTelefone(userForm.getPhone()) != null) {
            return new ModelAndView("register")
                    .addObject("errors", "There is already an account registered with the phone: " + userForm.getPhone());
        }
        userEntityService.create(userForm);
        mailSender.sendVerificationEmail(userForm);
        return new ModelAndView("redirect:/postRegister")
                .addObject("verificationCode", userForm.getVerificationCode());
    }

    @GetMapping("/postRegister")
    public ModelAndView postRegister(@RequestParam(required = false) String verificationCode) {
        if (HttpUtils.isLogged()) {
            return new ModelAndView("redirect:/home");
        }
        UserEntity user = userEntityService.findByVerificationCode(verificationCode);
        return new ModelAndView("home").addObject("messages", postRegsiterMessage(user));
    }

    public String postRegsiterMessage(UserEntity user) {
        return "Account Created Successfully! \n \n"
                + "A verification link has been sent to the email: " + user.getEmail();
    }

    @GetMapping("/verify")
    public ModelAndView verifyUser(@Param("code") String code) {
        if (HttpUtils.isLogged()) {
            return new ModelAndView("redirect:/home");
        }
        if (userEntityService.verify(code)) {
            return new ModelAndView("redirect:/home").addObject("messages", "User successfully verified! You can log in now.");
        } else {
            return new ModelAndView("redirect:/home").addObject("errors", "This user is already verified, you can log in now.");
        }
    }
}