package com.querotattoo.dao;

import com.querotattoo.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDAO extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByVerificationCode(String code);

    User findByPhone(String phone);

    @Query("FROM User c " + "WHERE LOWER(c.name) like %:searchTerm% " + "OR LOWER(c.email) like %:searchTerm%")
    Page<User> search(@Param("searchTerm") String searchTerm, Pageable pageable);
}