package com.template.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/postRegister")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/about")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/verify/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/home/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/register/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/register/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/forgotPassword/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/forgotPassword/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/profile/**")
                .authenticated()
                .antMatchers(HttpMethod.GET, "/admin/**")
                .hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

        http.exceptionHandling().accessDeniedPage("/accessDenied");
    }

    @Override
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/images/**");
    }
}
