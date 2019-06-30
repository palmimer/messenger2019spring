/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.messenger2019spring.config;

import com.progmatic.messenger2019spring.domain.User;
import java.time.LocalDate;
import java.time.Month;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 *
 * @author Varga János
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .formLogin()
                .loginPage("/login")
                //.permitAll()
                .defaultSuccessUrl("/messages", true)
                .and()
                .logout()
                .and()
                .authorizeRequests()
                .antMatchers("/messages", "/register", "/login").permitAll()
                //.antMatchers("/messages/create").access("hasRole('ADMIN')")
                .antMatchers("/css/*", "/js/*", "/images/*", "/favicon.ico").permitAll()
                .anyRequest().authenticated();
                
    }
    
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("user").password("password").roles("USER").build());
//        manager.createUser(User.withUsername("admin").password("password").roles("ADMIN").build());
        manager.createUser(new User("user", "password", "test@email.com", LocalDate.of(1986, Month.MARCH,15), "ROLE_USER"));
        manager.createUser(new User("admin", "password", "admin@email.com", LocalDate.of(1986, Month.MARCH,15), "ROLE_ADMIN"));
        return manager;
    }
    
    @SuppressWarnings("deprecation")
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
