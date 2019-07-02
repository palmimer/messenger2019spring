/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.messenger2019spring.service;

import com.progmatic.messenger2019spring.domain.Message;
import com.progmatic.messenger2019spring.domain.User;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 *
 * @author Varga János
 */
@Service
public class MessageServiceImpl {

    @PersistenceContext
    private EntityManager em;

    @PostFilter("hasRole('ADMIN') or filterObject.deleted == false")
    public List<Message> filterMessages(String author, String text, LocalDateTime startDate, LocalDateTime endDate, String sortBy, int messageCount, boolean ascending, boolean showDeleted) {

        List<Message> messages = getAllMessages();

        int numOfMessagesToShow = messageCount < 0 ? messages.size() : messageCount;
        Comparator<Message> messageComparator = getMesageComparator(sortBy);

        List<Message> messagesToShow = messages.stream()
                .filter(m -> author.isEmpty() ? true : m.getAuthor().equalsIgnoreCase(author))
                .filter(m -> m.getText().contains(text))
                .filter(m -> startDate == null ? true : m.getDateTime().isAfter(startDate))
                .filter(m -> endDate == null ? true : m.getDateTime().isBefore(endDate))
                .filter(m -> showDeleted ? true : !m.isDeleted())
                .sorted(ascending ? messageComparator : messageComparator.reversed())
                .limit(Math.min(messages.size(), numOfMessagesToShow))
                .collect(Collectors.toList());

        return messagesToShow;
    }

    @PostAuthorize("hasRole('ADMIN') or !returnObject.deleted")
    public Message getMessageById(int messageId) {
        Message message = em.find(Message.class, messageId);
        return message;
    }

    @Transactional
    public void addNewMessage(Message message) {
        em.persist(message);
    }

    private Comparator<Message> getMesageComparator(String by) {
        switch (by) {
            case "author":
                return Comparator.comparing(Message::getAuthor);
            case "text":
                return Comparator.comparing(Message::getText);
            default:
                return Comparator.comparing(Message::getDateTime);
        }
    }

    public List<Message> getAllMessages() {
        List<Message> messages = em.createQuery("SELECT m FROM Message m", Message.class).getResultList();

        return messages;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteMessage(int id) {
        Message messageToDelete = getMessageById(id);

        messageToDelete.setDeleted(true);
    }
}
