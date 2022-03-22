package com.querotattoo.dao;

import com.querotattoo.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleDAO extends JpaRepository<Role, Long> {

    Role findByRoleName(String role);

    @Query("FROM Role c " + "WHERE LOWER(c.roleName) like %:searchTerm%")
    Page<Role> search(@Param("searchTerm") String searchTerm, Pageable pageable);
}
