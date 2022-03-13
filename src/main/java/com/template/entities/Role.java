package com.template.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(unique = true)
    private String nomeRole;

    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users;

    public Role() {

    }

    public Role(String nomeRole, List<UserEntity> users) {
        this.nomeRole = nomeRole;
        this.users = users;
    }

    public String getNomeRole() {
        return nomeRole;
    }

    public void setNomeRole(String nomeRole) {
        this.nomeRole = nomeRole;
    }

    @Override
    public String getAuthority() {
        return this.nomeRole;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

}
