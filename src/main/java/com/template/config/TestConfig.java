package com.template.config;

import com.template.entities.Role;
import com.template.entities.UserEntity;
import com.template.services.RoleService;
import com.template.services.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserEntityService userEntityService;

    @Override
    public void run(String... args) throws Exception {
        final Role r1 = new Role("ROLE_ADMIN", null);
        final Role r2 = new Role("ROLE_USER", null);


        if (roleService.findByNomeRole(r1.getNomeRole()) == null && roleService.findByNomeRole(r2.getNomeRole()) == null) {
            roleService.SaveAll(Arrays.asList(r1, r2));
        }

        final UserEntity u3 = new UserEntity(null, "admin", "admin",
                new BCryptPasswordEncoder().encode("admin"), "35992258023", Arrays.asList(r1),null);
        u3.setEnabled(true);

        if (userEntityService.findByEmail(u3.getEmail()) == null) {
            userEntityService.saveAll(Arrays.asList(u3));
        }
    }
}