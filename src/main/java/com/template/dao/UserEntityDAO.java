package com.template.dao;

import com.template.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityDAO extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByVerificationCode(String code);

    UserEntity findByPhone(String phone);
}