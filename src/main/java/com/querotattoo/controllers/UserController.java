package com.querotattoo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querotattoo.controllers.exceptions.StandardError;
import com.querotattoo.entities.Artist;
import com.querotattoo.entities.User;
import com.querotattoo.services.CityService;
import com.querotattoo.services.SenderMailService;
import com.querotattoo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping(value = "/users")
@Validated
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CityService cityService;

    @Autowired
    private SenderMailService mailSender;

    @ResponseBody
    @GetMapping()
    public ResponseEntity<List<User>> findAll() {
        List<User> list = userService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<User> findAll(@PathVariable(value = "id") Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") Long id) {
        userService.delete(id);
    }

    @ResponseBody
    @PostMapping()
    public ResponseEntity<User> registration(@RequestBody User userForm) throws MessagingException, UnsupportedEncodingException, StandardError {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userForm.getId()).toUri();
        if (userService.findByEmail(userForm.getEmail()) != null) {
            throw new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro na criação do usuário", "Já existe um usuário com este e-mail");
        }
        if (userService.findByTelefone(userForm.getPhone()) != null) {
            throw new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro na criação do usuário", "Já existe um usuário com este telefone");
        }
        User user = userService.create(userForm);
        mailSender.sendVerificationEmail(user);
        return ResponseEntity.created(uri).body(user);
    }

    @ResponseBody
    @PostMapping("/artists")
    public ResponseEntity<User> artistRegistration(@RequestBody @Valid Artist artistForm) throws MessagingException, UnsupportedEncodingException, StandardError {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(artistForm.getId()).toUri();
        if (userService.findByEmail(artistForm.getEmail()) != null) {
            throw new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro na criação do usuário", "Já existe um usuário com este e-mail");
        }
        if (userService.findByTelefone(artistForm.getPhone()) != null) {
            throw new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro na criação do usuário", "Já existe um usuário com este telefone");
        }
        Artist artist = (Artist) userService.create(artistForm);
        mailSender.sendVerificationEmail(artistForm);
        return ResponseEntity.created(uri).body(artist);
    }

    @ResponseBody
    @PatchMapping("/artists/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") Long artistId, @RequestBody Map<String, Object> fieldsToUpdate) {
        Artist artistToUpdate = (Artist) userService.findById(artistId);
        if (artistToUpdate == null) {
            return ResponseEntity.notFound().build();
        }

        merge(fieldsToUpdate, artistToUpdate);
        return ResponseEntity.ok().body(userService.update(artistToUpdate));
    }

    private void merge(Map<String, Object> fieldsToUpdate, Artist artistToUpdate) {
        ObjectMapper mapper = new ObjectMapper();
        Artist newArtist = mapper.convertValue(fieldsToUpdate, Artist.class);

        fieldsToUpdate.forEach((fieldName, fieldValue) -> {
            Field field = ReflectionUtils.findField(Artist.class, fieldName);
            field.setAccessible(true);

            Object newValue = ReflectionUtils.getField(field, newArtist);

            ReflectionUtils.setField(field, artistToUpdate, newValue);
        });
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@Param("code") String code) {
        return userService.verify(code) ? ResponseEntity.ok().body("Usuário verificado com Sucesso!")
                : ResponseEntity.status(500).body("Erro na verificação do usuário");
    }
}