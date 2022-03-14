package com.querotattoo.dao;

import com.querotattoo.entities.IpAdress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IpDAO extends JpaRepository<IpAdress, Long> {
    IpAdress findByIp(String ip);
}