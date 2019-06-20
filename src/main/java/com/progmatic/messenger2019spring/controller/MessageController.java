/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.messenger2019spring.controller;

import com.progmatic.messenger2019spring.model.Message;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Varga János
 */
@Controller
public class MessageController {
    
    private List<Message> messages;

    public MessageController() {
        this.messages = new ArrayList<>();
        
        messages.add(new Message("Hello there", "Obi wan", LocalDateTime.of(2019, Month.MARCH, 12, 17, 13, 0)));
        messages.add(new Message("Programozni jó", "Progmatic"));
        messages.add(new Message("Ez egy teszt üzenet", "Test"));
        messages.add(new Message("Meg ez is", "Test"));
        messages.add(new Message("Meg még ez is", "Test"));
    }
    
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String listMessages(@RequestParam(value = "messageCount", defaultValue = "-1") int messageCount, Model model) {
        int numOfMessagesToShow = messageCount == -1 ? messages.size() : messageCount;
        List<Message> messagesToShow = messages.subList(0, Math.min(messages.size(), numOfMessagesToShow));
        model.addAttribute("messages", messagesToShow);
        
        return "messages";
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
