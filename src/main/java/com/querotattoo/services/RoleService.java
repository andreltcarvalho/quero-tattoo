package com.querotattoo.services;

import com.querotattoo.dao.RoleDAO;
import com.querotattoo.entities.Role;
import com.querotattoo.exceptions.DatabaseException;
import com.querotattoo.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleDAO roleDAO;

    public List<Role> findAll() {
        return roleDAO.findAll();
    }

    public Role findByNomeRole(String role) {
        return roleDAO.findByNomeRole(role);
    }

    public void SaveAll(List<Role> roles) {
        roleDAO.saveAll(roles);
    }

    public Role insert(Role obj) {
        return roleDAO.save(obj);
    }

    public Role merge(Role role) {
        return roleDAO.findByNomeRole(role.getNomeRole());
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
//
//	public Role update(Long id, Role obj) {
//		try {
//			Csidade entity = roleDAO.getOne(id);
//			updateData(entity, obj);
//			return roleDAO.save(entity);
//		} catch (EntityNotFoundException e) {
//			throw new ResourceNotFoundException(id);
//		}
//	}

//	private void updateData(Role entity, Role obj) {
//		entity.setNome(obj.getName());
//		entity.setEstado(obj.getEmail());
//		entity.setPhone(obj.getPhone());
//
//	}
}