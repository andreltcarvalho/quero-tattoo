package com.querotattoo.dao;

import com.querotattoo.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface ScheduleDAO extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByEventDate(Instant eventDate);
}