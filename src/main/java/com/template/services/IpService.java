package com.template.services;

import com.template.dao.IpDAO;
import com.template.entities.IpAdress;
import com.template.exceptions.DatabaseException;
import com.template.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IpService {

    private static Logger logger = LoggerFactory.getLogger(IpService.class);

    @Autowired
    private IpDAO ipDAO;

    public List<IpAdress> findAll() {
        return ipDAO.findAll();
    }

    public IpAdress findById(Long id) {
        final Optional<IpAdress> object = ipDAO.findById(id);
        return object.orElseThrow(() -> new ResourceNotFoundException(id));
    }
    public IpAdress findByIp(String ip){
       IpAdress object = ipDAO.findByIp(ip);
        return object;
    }

    public IpAdress insert(IpAdress ipAdress) {
        if (ipDAO.findByIp(ipAdress.getIp()) == null) {
            logger.info("Novo Ip conectado: " + ipAdress.toString());
            return ipDAO.save(ipAdress);
        }
        logger.info("Ip conectado: " + ipAdress.toString());
        return new IpAdress();
    }

    public IpAdress merge(IpAdress ip) {
        final Optional<IpAdress> object = ipDAO.findById(ip.getId());
        return object.orElseThrow(() -> new ResourceNotFoundException(ip.getId()));
    }

    public void saveAll(List<IpAdress> viagens) {
        ipDAO.saveAll(viagens);
    }

    public void delete(Long id) {
        try {
            ipDAO.deleteById(id);
        } catch (final EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (final DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
