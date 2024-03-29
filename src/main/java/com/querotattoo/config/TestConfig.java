package com.querotattoo.config;

import com.querotattoo.entities.City;
import com.querotattoo.entities.Role;
import com.querotattoo.entities.State;
import com.querotattoo.services.CityService;
import com.querotattoo.services.RoleService;
import com.querotattoo.services.StateService;
import com.querotattoo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private StateService stateService;

    @Autowired
    CityService cityService;

    @Override
    public void run(String... args) throws Exception {
        final Role r1 = new Role("ROLE_ADMIN", null);
        final Role r2 = new Role("ROLE_CUSTOMER", null);
        final Role r3 = new Role("ROLE_ARTIST", null);


        if (roleService.findByRoleName(r1.getRoleName()) == null && roleService.findByRoleName(r2.getRoleName()) == null) {
            roleService.SaveAll(Arrays.asList(r1, r2, r3));
        }


        State state1 = new State(null, "São Paulo", null);
        State state2 = new State(null, "Minas Gerais", null);

        if (stateService.findByStateName(state1.getName()) == null && stateService.findByStateName(state2.getName()) == null) {
            stateService.SaveAll(Arrays.asList(state1, state2));
        }


        City city1 = new City(null, "Campinas", state1);
        City city2 = new City(null, "Belo Horizonte", state2);

        if (cityService.findByNomeCity(city1.getName()) == null && cityService.findByNomeCity(city2.getName()) == null) {
            cityService.SaveAll(Arrays.asList(city1, city2));
        }
    }
}