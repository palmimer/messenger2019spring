/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.messenger2019spring.controller;

import com.progmatic.messenger2019spring.domain.User;
import com.progmatic.messenger2019spring.dto.RegistrationDto;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
    public String showRegister(@ModelAttribute("registration") RegistrationDto registration) {
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registration") RegistrationDto registration, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || registrationHasErrors(registration, bindingResult)) {
            return "register";
        }
        userDetailsService.createUser(new User(registration.getUsername(), registration.getPassword1(), 
                registration.getEmail(), registration.getBirthday(), "ROLE_USER"));
         
        return "redirect:/login?registered";
    }
    
    private boolean registrationHasErrors(RegistrationDto registration, BindingResult bindingResult) {
        boolean hasErrors = false;
        if(!registration.getPassword1().equals(registration.getPassword2())){
            bindingResult.rejectValue("password1", "registration.password1", "Passwords do not match!");
            bindingResult.rejectValue("password2", "registration.password2", "Passwords do not match!");
            hasErrors = true;
        }
        if(userDetailsService.userExists(registration.getUsername())){
            bindingResult.rejectValue("username", "registration.username", "User already exists!");
            hasErrors = true;
        }
        return hasErrors;
    }
}
