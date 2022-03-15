package com.querotattoo.entities;

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
public class Artist extends User implements Serializable, UserDetails {
    private static final long serialVersionUID = 1L;

    @NotNull
    @NotEmpty
    @ElementCollection
    private List<String> tattooStyles;

    @NotNull
    @NotEmpty
    @ElementCollection
    private List<String> workingCities;

    @Embedded
    private StudioAddress studioAddress;

    public Artist(Long id, String nome, String email, String password, String phone,
                  List<Role> roles, String userType, List<String> tattooStyles, List<String> workingCities) {
        super(id, nome, email, password, phone, roles);
        this.tattooStyles = tattooStyles;
        this.workingCities = workingCities;
    }

    public Artist() {

    }

    public List<String> getTattooStyles() {
        return tattooStyles;
    }

    public void setTattooStyles(List<String> tattooStyles) {
        this.tattooStyles = tattooStyles;
    }

    public List<String> getWorkingCities() {
        return workingCities;
    }

    public void setWorkingCities(List<String> workingCities) {
        this.workingCities = workingCities;
    }
}
