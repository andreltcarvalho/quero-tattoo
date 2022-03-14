package com.querotattoo.dao;

import com.querotattoo.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role, Long> {

	Role findByNomeRole(String role);
}
