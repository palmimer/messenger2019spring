/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.messenger2019spring.domain;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Varga János
 */
public class Message {
    private static int prevId;
    private int id;
    @NotNull
    @NotBlank
    @Size(min = 10, max = 100)
    private String text;
    
//    @NotNull
//    @NotBlank
//    @Size(min = 3, message = "Minimum {2} karakter hosszúnak kell lennie!")
    private String author;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    public Message() {
        dateTime = LocalDateTime.now();
        id = prevId++;
    }

    public Message(String text, String author) {
        this();
        this.text = text;
        this.author = author;
    }

    public Message(String text, String author, LocalDateTime dateTime) {
        this();
        
        this.text = text;
        this.author = author;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
