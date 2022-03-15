package com.querotattoo.entities;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "artists")
@Data
public class Artist extends User implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    @NotNull
    @NotEmpty
    @ElementCollection
    private List<String> tattooStyles;

    @NotNull
    @Embedded
    @ElementCollection
    private List<StudioAddress> addresses;
}
