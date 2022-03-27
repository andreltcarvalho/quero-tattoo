package com.querotattoo.services;

import com.querotattoo.dao.ScheduleDAO;
import com.querotattoo.entities.Schedule;
import com.querotattoo.exceptions.DatabaseException;
import com.querotattoo.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    private static Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    private ScheduleDAO scheduleDAO;

    @Autowired
    private RoleService roleService;


    public Page<Schedule> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "eventDate");
        return new PageImpl<Schedule>(scheduleDAO.findAll(), pageRequest, size);
    }

    public Schedule findById(Long id) {
        if (id == null) {
            throw new IllegalStateException("O id do agendamento não pode ser nulo.");
        }
        Optional<Schedule> object = scheduleDAO.findById(id);
        return object.orElseThrow(() -> new ResourceNotFoundException("Agendamento nao encontrado com id: " + id));
    }

    public List<Schedule> saveAll(List<Schedule> schedules) {
        return scheduleDAO.saveAll(schedules);
    }

    public Schedule create(Schedule schedule) {
        return scheduleDAO.save(schedule);
    }

    public void delete(Long id) {
        try {
            scheduleDAO.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void merge(Schedule schedule) {
        Schedule scheduleToUpdate = scheduleDAO.findById(schedule.getId()).get();
        scheduleDAO.saveAndFlush(scheduleToUpdate);
    }
}

