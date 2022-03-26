package com.querotattoo.services;

import com.querotattoo.dao.TattooEstimateDAO;
import com.querotattoo.entities.TattooEstimate;
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
public class TattooEstimateService {

    private static Logger logger = LoggerFactory.getLogger(TattooEstimateService.class);

    @Autowired
    private TattooEstimateDAO tattooEstimateDAO;

    @Autowired
    private RoleService roleService;


    public Page<TattooEstimate> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "artist");
        return new PageImpl<TattooEstimate>(tattooEstimateDAO.findAll(), pageRequest, size);
    }

    public TattooEstimate findById(Long id) {
        if (id == null) {
            throw new IllegalStateException("O id do agendamento não pode ser nulo.");
        }
        Optional<TattooEstimate> object = tattooEstimateDAO.findById(id);
        return object.orElseThrow(() -> new ResourceNotFoundException("Agendamento nao encontrado com id: " + id));
    }

    public List<TattooEstimate> saveAll(List<TattooEstimate> estimates) {
        return tattooEstimateDAO.saveAll(estimates);
    }

    public TattooEstimate save(TattooEstimate estimate) {
        return tattooEstimateDAO.save(estimate);
    }

    public void delete(Long id) {
        try {
            tattooEstimateDAO.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public void merge(TattooEstimate tattooEstimate) {
        TattooEstimate estimateToUpdate = tattooEstimateDAO.findById(tattooEstimate.getId()).get();
        tattooEstimateDAO.saveAndFlush(estimateToUpdate);
    }
}

