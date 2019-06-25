/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.messenger2019spring;

import com.progmatic.messenger2019spring.domain.Message;
import com.progmatic.messenger2019spring.service.MessageServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Varga János
 */
@Component
@Scope(
        scopeName = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserStatistics {
    private String lastAuthor;
    
    private List<Message> messagesInSession = new ArrayList<>();
    
    private MessageServiceImpl messageService;

    @Autowired
    public UserStatistics(MessageServiceImpl messageService) {
        this.messageService = messageService;
    }

    public void addNewMessage(Message message) {
        messagesInSession.add(message);
    }
    
    public int getMessageCountInSesson() {
        return messagesInSession.size();
    }
    
    public List<String> getAuthorsInSession() {
        //return messagesInSession.stream().map(m -> m.getAuthor()).distinct().collect(Collectors.toList());
        return messagesInSession.stream().map(Message::getAuthor).distinct().collect(Collectors.toList());
    }
    
    public int getMessageCountForAllAuthorsInSession() {
        List<String> authors = getAuthorsInSession();
        
        return (int) messageService.getAllMessages().stream().filter(m -> authors.contains(m.getAuthor())).count();
    }

    public String getLastAuthor() {
        return lastAuthor;
    }

    public void setLastAuthor(String lastAuthor) {
        this.lastAuthor = lastAuthor;
    }
}
