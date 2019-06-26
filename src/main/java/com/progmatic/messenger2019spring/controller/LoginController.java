/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.messenger2019spring.controller;

import com.progmatic.messenger2019spring.dto.RegistrationDto;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Varga János
 */
@Controller
public class LoginController {
    private UserDetailsManager userDetailsService;

    @Autowired
    public LoginController(UserDetailsService userDetailsService) {
        this.userDetailsService = (UserDetailsManager) userDetailsService;
    }
    
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
    
    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registration") RegistrationDto registration) {
        List<GrantedAuthority> userAuthorities = new ArrayList<>();
        userAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        userDetailsService.createUser(new SajatUser(registration.getUsername()
                , "password", "email", userAuthorities));
        return "register";
    }
}
