package com.querotattoo.entities.dto;

import com.querotattoo.entities.User;
import lombok.Data;

import javax.persistence.ElementCollection;
import java.util.List;

@Data
public class UserReadDTO {
    private String name;
    private String password;
    private String email;
    private String phone;
    private boolean enabled;

    @ElementCollection
    private List<String> roles;

    public UserReadDTO() {

    }

    public UserReadDTO(User user, List<String> roles) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.password = user.getPassword();
        this.phone = user.getPhone();
        this.roles = roles;
        this.enabled = user.isEnabled();

    }
}
