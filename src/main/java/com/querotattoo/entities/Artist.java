package com.querotattoo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "artists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist extends User implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    @NotNull
    @NotEmpty
    @ElementCollection
    @Valid
    private List<String> tattooStyles;

    @NotNull
    @Embedded
    @ElementCollection
    @Valid
    private List<StudioAddress> addresses;
}
