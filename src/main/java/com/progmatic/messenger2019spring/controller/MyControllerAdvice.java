/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.messenger2019spring.controller;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.apache.tomcat.util.ExceptionUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author Varga János
 */
@ControllerAdvice
public class MyControllerAdvice {

//    @ExceptionHandler(Exception.class)
//    public String handleErrors(Exception ex, Model model) {
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        PrintStream ps = new PrintStream(os);
//        ex.printStackTrace(ps);
//        String output = os.toString();
//        model.addAttribute("exceptionMessage", output);
//        return "error";
//    }
}
