package com.querotattoo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_roles")
@NoArgsConstructor
public class Role implements GrantedAuthority {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(unique = true)
    private String nomeRole;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private List<User> users;


    public Role(String nomeRole, List<User> users) {
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
