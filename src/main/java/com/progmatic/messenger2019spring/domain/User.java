package com.progmatic.messenger2019spring.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author Varga János
 */
public class User extends org.springframework.security.core.userdetails.User{
    private String email;
    private LocalDate birthday;
    private LocalDateTime timeOfRegistration;
    
    public User(String username, String password, String email, LocalDate birthDay, String... authorities) {
        this(username, password, email, birthDay, new ArrayList(Arrays.stream(authorities).map(SimpleGrantedAuthority::new).collect(Collectors.toList())));
    }
    
    public User(String username, String password, String email, LocalDate birthDay, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        
        this.email = email;
        this.birthday = birthDay;
        this.timeOfRegistration = LocalDateTime.now();
    }

    public LocalDateTime getTimeOfRegistration() {
        return timeOfRegistration;
    }

    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
 
    
}
