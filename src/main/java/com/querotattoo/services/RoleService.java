package com.querotattoo.services;

import com.querotattoo.dao.RoleDAO;
import com.querotattoo.entities.Role;
import com.querotattoo.exceptions.DatabaseException;
import com.querotattoo.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleDAO roleDAO;

    public List<Role> findAll() {
        return roleDAO.findAll();
    }

    public Role findByRoleName(String role) {
        return roleDAO.findByRoleName(role);
    }

    public void SaveAll(List<Role> roles) {
        roleDAO.saveAll(roles);
    }

    public Role insert(Role obj) {
        return roleDAO.save(obj);
    }

    public Role merge(Role role) {
        return roleDAO.findByRoleName(role.getRoleName());
    }

    public void delete(Long id) {
        try {
            roleDAO.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());

        }
    }

    public Page<Role> search(String searchTerm, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "roleName");
        return roleDAO.search(searchTerm.toLowerCase(), pageRequest);
    }
}
