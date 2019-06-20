/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.messenger2019spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author Varga János
 */

@SpringBootApplication
@ComponentScan("com.progmatic.messenger2019spring")
public class MessengerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessengerApplication.class, args);
    }
}
