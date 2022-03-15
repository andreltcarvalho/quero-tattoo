package com.querotattoo.services;

import com.querotattoo.dao.CityDAO;
import com.querotattoo.entities.City;
import com.querotattoo.exceptions.DatabaseException;
import com.querotattoo.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    @Autowired
    private CityDAO cityDAO;

    public List<City> findAll() {
        return cityDAO.findAll();
    }

    public City findByNomeCity(String city) {
        return cityDAO.findByName(city);
    }

    public City findById(Long id) {
        Optional<City> object = cityDAO.findById(id);
        return object.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public void SaveAll(List<City> roles) {
        cityDAO.saveAll(roles);
    }

    public City insert(City obj) {
        return cityDAO.save(obj);
    }


    public void delete(Long id) {
        try {
            cityDAO.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());

        }
    }
}
