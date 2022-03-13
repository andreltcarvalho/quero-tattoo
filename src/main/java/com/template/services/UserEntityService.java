package com.template.services;

import com.template.dao.UserEntityDAO;
import com.template.entities.UserEntity;
import com.template.exceptions.DatabaseException;
import com.template.exceptions.ResourceNotFoundException;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserEntityService {

    private static Logger logger = LoggerFactory.getLogger(UserEntityService.class);

    @Autowired
    private UserEntityDAO userEntityDAO;

    @Autowired
    private RoleService roleService;

    public List<UserEntity> findAll() {
        return userEntityDAO.findAll();
    }

    public UserEntity findById(Long id) {
        Optional<UserEntity> object = userEntityDAO.findById(id);
        return object.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<UserEntity> saveAll(List<UserEntity> users) {
        return userEntityDAO.saveAll(users);
    }

    public UserEntity create(UserEntity userForm) {
        userForm.setPassword(new BCryptPasswordEncoder().encode(userForm.getPassword()));
        String randomCode = RandomString.make(64);
        userForm.setVerificationCode(randomCode);
        userForm.setRoles(Arrays.asList(roleService.findByNomeRole("ROLE_USER")));
        UserEntity user = userEntityDAO.save(userForm);
        logger.info("Novo usuario cadastrado: " + user.toString());
        return user;
    }

    public UserEntity update(UserEntity userForm) {
        if (findByEmail(userForm.getEmail()) != null) {
            logger.info("Usuario atualizado: " + userForm.getEmail());
            return userEntityDAO.save(userForm);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public void delete(Long id) {
        try {
            userEntityDAO.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public boolean verify(String verificationCode) {
        UserEntity user = userEntityDAO.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setEnabled(true);
            user.setVerificationCode(null);
            userEntityDAO.save(user);
            logger.info("Verificação feita com sucesso pelo usuario: " + user.getEmail());
            return true;
        }
    }

    public UserEntity findByEmail(String username) {
        return userEntityDAO.findByEmail(username);
    }

    public UserEntity findByVerificationCode(String code) {
        return userEntityDAO.findByVerificationCode(code);
    }

    public boolean checkValidPassword(String password, String param) {
        return new BCryptPasswordEncoder().matches(param, password);
    }

    public UserEntity findByTelefone(String telefone) {
        return userEntityDAO.findByPhone(telefone);
    }
}

//	public Usuario update(Long id, Usuario obj) {
//		try {
//			Cidade entity = usuarioDAO.getOne(id);
//			return usuarioDAO.save(entity);
//		} catch (EntityNotFoundException e) {
//			throw new ResourceNotFoundException(id);
//		}
//	}

