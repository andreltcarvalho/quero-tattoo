package com.querotattoo.dao;

import com.querotattoo.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityDAO extends JpaRepository<City, Long> {
    City findByName(String name);
}