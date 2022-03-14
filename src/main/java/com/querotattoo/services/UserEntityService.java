package com.querotattoo.services;

import com.querotattoo.dao.UserDAO;
import com.querotattoo.entities.User;
import com.querotattoo.exceptions.DatabaseException;
import com.querotattoo.exceptions.ResourceNotFoundException;
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
    private UserDAO userDAO;

    @Autowired
    private RoleService roleService;

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public User findById(Long id) {
        Optional<User> object = userDAO.findById(id);
        return object.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<User> saveAll(List<User> users) {
        return userDAO.saveAll(users);
    }

    public User create(User userForm) {
        userForm.setPassword(new BCryptPasswordEncoder().encode(userForm.getPassword()));
        String randomCode = RandomString.make(64);
        userForm.setVerificationCode(randomCode);
        userForm.setRoles(Arrays.asList(roleService.findByNomeRole("ROLE_USER")));
        logger.info("Novo usuario cadastrado: " + userForm.toString());
        return userDAO.save(userForm);
    }

    public User update(User userForm) {
        if (findByEmail(userForm.getEmail()) != null) {
            logger.info("Usuario atualizado: " + userForm.getEmail());
            return userDAO.save(userForm);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public void delete(Long id) {
        try {
            userDAO.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public boolean verify(String verificationCode) {
        User user = userDAO.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setEnabled(true);
            user.setVerificationCode(null);
            userDAO.save(user);
            logger.info("Verificação feita com sucesso pelo usuario: " + user.getEmail());
            return true;
        }
    }

    public User findByEmail(String username) {
        return userDAO.findByEmail(username);
    }

    public User findByVerificationCode(String code) {
        return userDAO.findByVerificationCode(code);
    }

    public boolean checkValidPassword(String password, String param) {
        return new BCryptPasswordEncoder().matches(param, password);
    }

    public User findByTelefone(String telefone) {
        return userDAO.findByPhone(telefone);
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

