package com.template.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.template.entities.Role;

public interface RoleDAO extends JpaRepository<Role, Long> {

	Role findByNomeRole(String role);
}
