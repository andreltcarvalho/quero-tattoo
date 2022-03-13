package com.template.controllers;

import com.template.controllers.exceptions.StandardError;
import com.template.entities.Role;
import com.template.entities.UserEntity;
import com.template.services.RoleService;
import com.template.services.SenderMailService;
import com.template.services.UserEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController()
@RequestMapping(value = "/users")
public class UserEntityRestController {

    private static Logger logger = LoggerFactory.getLogger(UserEntityRestController.class);

    @Autowired
    private UserEntityService userService;

    @Autowired
    private SenderMailService mailSender;

    @ResponseBody
    @GetMapping()
    public ResponseEntity<List<UserEntity>> findAll() {
        List<UserEntity> list = userService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> findAll(@PathVariable(value = "id") Long id) {
        UserEntity user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public void changePassword(@PathVariable(value = "id") Long id, UserEntity userForm) {

        UserEntity user = userService.findById(id);
        if (userService.checkValidPassword(user.getPassword(), user.getPassword())) {
            user.setPassword(new BCryptPasswordEncoder().encode(userForm.getPassword()));
            userService.update(user);
            logger.info("Usuario " + user.getEmail() + " trocou de senha.");
        }
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value="id") Long id){
        userService.delete(id);
    }

    @ResponseBody
    @PostMapping()
    public ResponseEntity<UserEntity> registration(@RequestBody UserEntity userForm) throws MessagingException, UnsupportedEncodingException, StandardError {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userForm.getId()).toUri();
        if (userService.findByEmail(userForm.getEmail()) != null) {
            throw new StandardError( HttpStatus.INTERNAL_SERVER_ERROR.value(),"Erro na criação do usuário","Já existe um usuário com este e-mail",uri.getPath());
        }
        if (userService.findByTelefone(userForm.getPhone()) != null) {
            throw new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Erro na criação do usuário","Já existe um usuário com este telefone",uri.getPath());
        }
        UserEntity user = userService.create(userForm);
        mailSender.sendVerificationEmail(userForm);
        return ResponseEntity.created(uri).body(user);
    }

    @GetMapping("/verify")
    public void verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            //retorna 200
        } else {
            //ai depende 500 ou 400
        }
    }
}