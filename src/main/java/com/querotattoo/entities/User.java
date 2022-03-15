package com.querotattoo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Inheritance
@Entity
@Table(name = "users")
public class User implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @NotNull
    @Column(name = "phone", unique = true)
    private String phone;


    private boolean enabled;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @NotNull
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "nomeRole"))
    private List<Role> roles;

    public User() {

    }

    public User(Long id, String nome, String email, String password, String phone, List<Role> roles) {
        this.id = id;
        this.name = nome;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.roles = roles;
        this.enabled = false;

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return isEnabled() == that.isEnabled() && getId().equals(that.getId())
                && getName().equals(that.getName()) && getPassword().equals(that.getPassword())
                && getPhone().equals(that.getPhone()) && getVerificationCode().equals(that.getVerificationCode())
                && getEmail().equals(that.getEmail())
                && getRoles().equals(that.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPassword(), getPhone(), isEnabled(), getVerificationCode(), getEmail(), getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", enabled=" + enabled +
                ", verificationCode='" + verificationCode + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}
