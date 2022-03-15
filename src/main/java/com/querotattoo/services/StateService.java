package com.querotattoo.services;

import com.querotattoo.dao.StateDAO;
import com.querotattoo.entities.State;
import com.querotattoo.exceptions.DatabaseException;
import com.querotattoo.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StateService {

    @Autowired
    private StateDAO stateDAO;

    public List<State> findAll() {
        return stateDAO.findAll();
    }

    public State findByNomeState(String state) {
        return stateDAO.findByName(state);
    }

    public State findById(Long id) {
        Optional<State> object = stateDAO.findById(id);
        return object.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public void SaveAll(List<State> roles) {
        stateDAO.saveAll(roles);
    }

    public State insert(State obj) {
        return stateDAO.save(obj);
    }


    public void delete(Long id) {
        try {
            stateDAO.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());

        }
    }
}
