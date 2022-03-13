package com.template.security;

import com.template.entities.UserEntity;
import com.template.services.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserEntityService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userService.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario não encontrado");

        } else {
            return user;
        }
    }

}
