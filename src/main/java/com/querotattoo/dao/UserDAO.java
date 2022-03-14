package com.querotattoo.dao;

import com.querotattoo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByVerificationCode(String code);

    User findByPhone(String phone);
}