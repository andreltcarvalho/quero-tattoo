package com.querotattoo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querotattoo.controllers.exceptions.StandardError;
import com.querotattoo.controllers.exceptions.StandardErrorMessage;
import com.querotattoo.entities.Artist;
import com.querotattoo.entities.Customer;
import com.querotattoo.entities.User;
import com.querotattoo.entities.dto.DTOMapper;
import com.querotattoo.entities.dto.UserReadDTO;
import com.querotattoo.services.CityService;
import com.querotattoo.services.RoleService;
import com.querotattoo.services.SenderMailService;
import com.querotattoo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/users")
@Validated
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CityService cityService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SenderMailService mailSender;

    @Autowired
    private DTOMapper DTOMapper;

    @ResponseBody
    @GetMapping()
    public ResponseEntity<Page<UserReadDTO>> findAll(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                     @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        List<UserReadDTO> list = userService.findAll(page, size)
                .stream()
                .map(DTOMapper::toDto)
                .collect(toList());

        Page<UserReadDTO> userReadDtoPage = new PageImpl<>(list);
        return ResponseEntity.ok().body(userReadDtoPage);
    }

    @GetMapping("/search")
    public Page<UserReadDTO> search(@RequestParam("searchTerm") String searchTerm,
                                    @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        List<UserReadDTO> userPage = userService.search(searchTerm, page, size).stream().map(DTOMapper::toDto).collect(Collectors.toList());
        return new PageImpl<>(userPage);
    }


    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok().body((userService.findById(id)));
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") Long id) {
        userService.delete(id);
    }

    @ResponseBody
    @PostMapping("/artists")
    public ResponseEntity<User> artistRegistration(@RequestBody @Valid Artist artistForm) throws MessagingException, UnsupportedEncodingException, StandardError {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(artistForm.getId()).toUri();
        checkEmailAndPhoneToCreate(artistForm);
        artistForm.setRoles(Arrays.asList(roleService.findByRoleName("ROLE_ARTIST")));
        Artist artist = (Artist) userService.create(artistForm);
        mailSender.sendVerificationEmail(artistForm);
        return ResponseEntity.created(uri).body(artist);
    }

    @ResponseBody
    @PostMapping("/customers")
    public ResponseEntity<User> customerRegistration(@RequestBody @Valid Customer customerForm) throws MessagingException, UnsupportedEncodingException, StandardError {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(customerForm.getId()).toUri();
        checkEmailAndPhoneToCreate(customerForm);
        customerForm.setRoles(Arrays.asList(roleService.findByRoleName("ROLE_CUSTOMER")));
        Customer artist = (Customer) userService.create(customerForm);
        mailSender.sendVerificationEmail(customerForm);
        return ResponseEntity.created(uri).body(artist);
    }

    @ResponseBody
    @PatchMapping("/artists/{id}")
    public ResponseEntity<?> updateArtist(@PathVariable(value = "id") Long userId, @RequestBody Map<String, Object> fieldsToUpdate) {
        Artist artistToUpdate = (Artist) userService.findById(userId);

        mergeUser(fieldsToUpdate, artistToUpdate);

        return ResponseEntity.ok().body(userService.update(artistToUpdate));
    }


    @ResponseBody
    @PatchMapping("/customers/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable(value = "id") Long userId, @RequestBody Map<String, Object> fieldsToUpdate) {
        Customer artistToUpdate = (Customer) userService.findById(userId);
        if (artistToUpdate == null) {
            return ResponseEntity.notFound().build();
        }

        mergeUser(fieldsToUpdate, artistToUpdate);
        return ResponseEntity.ok().body(userService.update(artistToUpdate));
    }

    private void mergeUser(Map<String, Object> fieldsToUpdate, Object userToUpdate) {
        ObjectMapper mapper = new ObjectMapper();
        Object newUser = null;
        if (userToUpdate instanceof Artist) {
            newUser = mapper.convertValue(fieldsToUpdate, Artist.class);
        }

        if (userToUpdate instanceof Customer) {
            newUser = mapper.convertValue(fieldsToUpdate, Customer.class);
        }

        Object finalNewUser = newUser;
        fieldsToUpdate.forEach((fieldName, fieldValue) -> {
            Field field = ReflectionUtils.findField(Artist.class, fieldName);
            field.setAccessible(true);

            Object newValue = ReflectionUtils.getField(field, finalNewUser);
            ReflectionUtils.setField(field, userToUpdate, newValue);
        });
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@Param("code") String code) throws JsonProcessingException, StandardError {
        if (userService.verify(code)) {
            return ResponseEntity.ok().body(("Usuário verificado com Sucesso!"));
        }
        return ResponseEntity.status(500).body(new StandardErrorMessage("Erro na verificação do usuário.", "Tente novamente"));
    }

    public void checkEmailAndPhoneToCreate(User user) throws StandardError {
        if (userService.findByEmail(user.getEmail()) != null) {
            throw new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro na criação do usuário", "Já existe um usuário com este e-mail");
        }
        if (userService.findByTelefone(user.getPhone()) != null) {
            throw new StandardError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro na criação do usuário", "Já existe um usuário com este telefone");
        }
    }
}