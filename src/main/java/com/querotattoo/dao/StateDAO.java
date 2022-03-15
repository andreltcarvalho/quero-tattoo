package com.querotattoo.dao;

import com.querotattoo.entities.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateDAO extends JpaRepository<State, Long> {
    State findByName(String name);
}