/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.messenger2019spring.controller;

import com.progmatic.messenger2019spring.UserStatistics;
import com.progmatic.messenger2019spring.domain.Message;
import com.progmatic.messenger2019spring.service.MessageServiceImpl;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Varga János
 */
@Controller
public class MessageController {
    
    private MessageServiceImpl messageService;
    private UserStatistics userStatistics;
    

    @Autowired
    public MessageController(MessageServiceImpl messageService, UserStatistics userStatistics) {
        this.messageService = messageService;
        this.userStatistics = userStatistics;
    }
    
    

    @RequestMapping(value = {"/", "/messages"}, method = RequestMethod.GET)
    public String listMessages(@RequestParam(value = "messageCount", defaultValue = "-1") int messageCount,
            @RequestParam(value = "ascending", defaultValue = "true") boolean ascending,
            Model model) {
        
        List<Message> messagesToShow = messageService.listMessages(messageCount, ascending);
        model.addAttribute("messages", messagesToShow);

        return "messages";
    }

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.GET)
    public String oneMessage(@PathVariable("messageId") int messageId, Model model) {

        Message message = messageService.getMessageById(messageId);
        model.addAttribute("message", message);
        return "oneMessage";
    }
    
    @RequestMapping(value = "/messages/create", method = RequestMethod.GET)
    public String showCreateMessage(Model model) {
        
        model.addAttribute("message", new Message("", userStatistics.getAuthor()));
        return "newMessage";
    }
    
    @RequestMapping(value = "/messages/create", method = RequestMethod.POST)
    public String createMessage(@Valid @ModelAttribute("message") Message message, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return "newMessage";
        }
         
        userStatistics.setAuthor(message.getAuthor());
        messageService.addNewMessage(message);
        return "redirect:/messages";
    }

}
