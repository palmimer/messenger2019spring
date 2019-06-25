/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.messenger2019spring.service;

import com.progmatic.messenger2019spring.domain.Message;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Varga János
 */
@Service
public class MessageServiceImpl {

    private List<Message> messages;

    public MessageServiceImpl() {
        this.messages = new ArrayList<>();

        messages.add(new Message("Hello there", "Obi wan", LocalDateTime.of(2019, Month.MARCH, 12, 17, 13, 0)));
        messages.add(new Message("Programozni jó", "Progmatic"));
        messages.add(new Message("Ez egy teszt üzenet", "Test"));
        messages.add(new Message("Meg ez is", "Test"));
        messages.add(new Message("Meg még ez is", "Test"));
    }

    public List<Message> filterMessages(String author, String text, LocalDateTime startDate, LocalDateTime endDate, String sortBy, int messageCount, boolean ascending) {
        int numOfMessagesToShow = messageCount < 0 ? messages.size() : messageCount;
        
        Comparator<Message> messageComparator = getMesageComparator(sortBy);
            
        List<Message> messagesToShow = messages.stream()
                .filter(m -> author.isEmpty() ? true : m.getAuthor().equals(author))
                .filter(m -> m.getText().contains(text))
                .filter(m -> startDate == null ? true : m.getDateTime().isAfter(startDate))
                .filter(m -> endDate == null ? true : m.getDateTime().isBefore(endDate))
                .sorted(ascending ? messageComparator : messageComparator.reversed())
                .limit(Math.min(messages.size(), numOfMessagesToShow))
                .collect(Collectors.toList());

        return messagesToShow;
    }

    public Message getMessageById(int messageId) {
        Message message = messages.stream().filter(m -> m.getId() == messageId).findFirst().get();
        return message;
    }

    public void addNewMessage(Message message) {
        messages.add(message);
    }
    
    private Comparator<Message> getMesageComparator(String by) {
        switch (by) {
            case "author": return Comparator.comparing(Message::getAuthor);
            case "text": return Comparator.comparing(Message::getText);
            default: return Comparator.comparing(Message::getDateTime);
        }
    }


}