package com.progmatic.messenger2019spring.dto;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Varga János
 */
public class RegistrationDto {

    @NotNull
    @Size(min = 3, message = "Username must be at least {2} characters long!")
    private String username;
    @NotNull
    @Size(min = 8, message = "Password must be at least {2} characters long!")
    private String password1;
    
    @NotNull
    @Size(min = 8, message = "Password must be at least {2} characters long!")
    private String password2;
    
    @NotNull
    @NotBlank
    @Email
    private String email;
    
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
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
