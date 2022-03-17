package com.querotattoo.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Customer extends User implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

}
