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

    public List<Message> listMessages(int messageCount, boolean ascending) {
        int numOfMessagesToShow = messageCount < 0 ? messages.size() : messageCount;
        List<Message> messagesToShow = messages.subList(0, Math.min(messages.size(), numOfMessagesToShow));

//        Comparator<Message> comparator = ascending ? (m1, m2) -> m1.getAuthor().compareTo(m2.getAuthor()) :
//                (m1, m2) -> m2.getAuthor().compareTo(m1.getAuthor());
        Comparator<Message> comparator = Comparator.comparing(Message::getAuthor);
        if (!ascending) {
            comparator = comparator.reversed();
        }

        messagesToShow.sort(comparator);

        return messagesToShow;
    }

    public Message getMessageById(int messageId) {
        Message message = messages.stream().filter(m -> m.getId() == messageId).findFirst().get();
        return message;
    }

    public void addNewMessage(Message message) {
        messages.add(message);
    }
}
