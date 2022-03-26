package com.querotattoo.dao;

import com.querotattoo.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleDAO extends JpaRepository<Schedule, Long> {
}