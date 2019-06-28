/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.messenger2019spring.controller;

import com.progmatic.messenger2019spring.UserStatistics;
import com.progmatic.messenger2019spring.domain.Message;
import com.progmatic.messenger2019spring.dto.MessageDto;
import com.progmatic.messenger2019spring.service.MessageServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
    public String listMessages(
            @RequestParam(value = "author", defaultValue = "") String author,
            @RequestParam(value = "text", defaultValue = "") String text,
            @RequestParam(value = "startDate", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startDate,
            @RequestParam(value = "endDate", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime endDate,
            @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
            @RequestParam(value = "messageCount", defaultValue = "-1") int messageCount,
            @RequestParam(value = "ascending", defaultValue = "true") boolean ascending,
            @RequestParam(value = "showDeleted", defaultValue = "false") boolean showDeleted,
            Model model, HttpServletRequest servletRequest) {
        
        List<Message> messagesToShow = messageService.filterMessages(author, text, startDate, endDate, sortBy, messageCount, ascending, showDeleted);
        messagesToShow = messagesToShow.stream().filter(m -> servletRequest.isUserInRole("ADMIN") || servletRequest.isUserInRole("USER") && !m.isDeleted()).collect(Collectors.toList());

        model.addAttribute("messages", messagesToShow);

        return "messages";
    }

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.GET)
    public String oneMessage(@PathVariable("messageId") int messageId, Model model) {

        Message message = messageService.getMessageById(messageId);
        model.addAttribute("message", message);
        return "oneMessage";
    }
    
    @RequestMapping(value = "/messages/delete/{messageId}", method = RequestMethod.POST)
    public String deleteMessage(@PathVariable("messageId") int messageId) {

        messageService.deleteMessage(messageId);
        return "redirect:/messages";
    }

    @RequestMapping(value = "/messages/create", method = RequestMethod.GET)
    public String showCreateMessage(Model model) {
        model.addAttribute("message", new MessageDto(""));
        return "newMessage";
    }

    @RequestMapping(value = "/messages/create", method = RequestMethod.POST)
    public String createMessage(@Valid @ModelAttribute("message") MessageDto message, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "newMessage";
        }
        
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Message newMessage = new Message(message.getText(), user.getUsername());
        
        messageService.addNewMessage(newMessage);
        userStatistics.setLastAuthor(newMessage.getAuthor());
        userStatistics.addNewMessage(newMessage);
        return "redirect:/messages";
    }

    
    @RequestMapping(value = "/userstats", method = RequestMethod.GET)
    public String showUserStatistics(Model model) {
        
        model.addAttribute("userstats", userStatistics);
        return "userstats";
    }
}
