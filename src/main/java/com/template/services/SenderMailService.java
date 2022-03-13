package com.template.services;

import com.template.entities.UserEntity;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Component
@Service
public class SenderMailService {

    private static Logger logger = LoggerFactory.getLogger(SenderMailService.class);

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private UserEntityService userService;

    @Value("${site.url}")
    private String siteURL;

    @Value("${spring.profiles.active}")
    private String profile;

    public void sendVerificationEmail(UserEntity user)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "thymeleaf.template@gmail.com";
        String senderName = "Thymeleaf Template";
        String subject = "Please, verify your account";
        String content = "Hello [[name]],<br> <br><br>"
                + "Click the link below to verify your account.:<br><br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY ACCOUNT</a></h3><br><br>"
                + "This is an automatic e-mail and must not be answered.<br><br>"
                + "Thank you.<br><br>";


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        if (profile.equals("prod")) {
            mailSender.send(message);
        } else {
            System.out.println(content);
        }
    }

    public void sendNewPasswordEmail(UserEntity user) {
        String toAddress = user.getEmail();
        String randomCode = RandomString.make(64);
        String fromAddress = "thymeleaf.template@gmail.com";
        String senderName = "Thymeleaf Template";
        String subject = "Recover Password";
        String content = "Hello [[name]],<br><br>"
                + "Click the link below to create a new password: <br><br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">CREATE NEW PASSWORD</a></h3><br><br>"
                + "This is an automatic e-mail and must not be answered.<br><br>"
                + "Thank you.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            content = content.replace("[[name]]", user.getName());
            user.setVerificationCode(randomCode);
            String verifyURL = siteURL + "/newPassword?verificationCode=" + user.getVerificationCode();
            content = content.replace("[[URL]]", verifyURL);
            helper.setText(content, true);
            userService.update(user);
            if (profile.equals("prod")) {
                mailSender.send(message);
            } else {
                System.out.println(content);
            }
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}