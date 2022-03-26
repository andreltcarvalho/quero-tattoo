package com.querotattoo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tb_artists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist extends User implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "Os estilos de tatuagem não podem ser nulos")
    @NotEmpty
    @ElementCollection
    @Valid
    private List<String> tattooStyles;

    @NotNull(message = "Os endereços não podem ser nulos")
    @Embedded
    @ElementCollection
    @Valid
    private List<StudioAddress> addresses;

    @OneToMany
    @ElementCollection
    @JsonIgnore
    private List<Schedule> schedules;
}
