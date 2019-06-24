/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.messenger2019spring.controller;

import com.progmatic.messenger2019spring.domain.Message;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.validation.Valid;
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

    private List<Message> messages;

    public MessageController() {
        this.messages = new ArrayList<>();

        messages.add(new Message("Hello there", "Obi wan", LocalDateTime.of(2019, Month.MARCH, 12, 17, 13, 0)));
        messages.add(new Message("Programozni jó", "Progmatic"));
        messages.add(new Message("Ez egy teszt üzenet", "Test"));
        messages.add(new Message("Meg ez is", "Test"));
        messages.add(new Message("Meg még ez is", "Test"));
    }

    @RequestMapping(value = {"/", "/messages"}, method = RequestMethod.GET)
    public String listMessages(@RequestParam(value = "messageCount", defaultValue = "-1") int messageCount,
            @RequestParam(value = "ascending", defaultValue = "true") boolean ascending,
            Model model) {
        int numOfMessagesToShow = messageCount < 0 ? messages.size() : messageCount;
        List<Message> messagesToShow = messages.subList(0, Math.min(messages.size(), numOfMessagesToShow));

//        Comparator<Message> comparator = ascending ? (m1, m2) -> m1.getAuthor().compareTo(m2.getAuthor()) :
//                (m1, m2) -> m2.getAuthor().compareTo(m1.getAuthor());

        Comparator<Message> comparator = Comparator.comparing(Message::getAuthor);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        
        messagesToShow.sort(comparator);
        model.addAttribute("messages", messagesToShow);

        return "messages";
    }

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.GET)
    public String oneMessage(@PathVariable("messageId") int messageId, Model model) {

        Message message = messages.stream().filter(m -> m.getId() == messageId).findFirst().get();
        model.addAttribute("message", message);
        return "oneMessage";
    }
    
    @RequestMapping(value = "/messages/create", method = RequestMethod.GET)
    public String showCreateMessage(@ModelAttribute("message") Message message) {
        return "newMessage";
    }
    
    @RequestMapping(value = "/messages/create", method = RequestMethod.POST)
    public String createMessage(@Valid @ModelAttribute("message") Message message, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return "newMessage";
        }
        
        messages.add(message);
        return "redirect:/messages";
    }

}
