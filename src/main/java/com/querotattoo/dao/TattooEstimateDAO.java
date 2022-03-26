package com.querotattoo.dao;

import com.querotattoo.entities.TattooEstimate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TattooEstimateDAO extends JpaRepository<TattooEstimate, Long> {
}