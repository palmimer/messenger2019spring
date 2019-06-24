/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progmatic.messenger2019spring.filters;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Varga János
 */
@Component
public class RequestLoggerFilter implements Filter {
    
    private Logger logger = LoggerFactory.getLogger(RequestLoggerFilter.class);
    
    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        sr.getParameterMap().forEach((k, v) -> logger.debug("{}: {}", k, v));
        fc.doFilter(sr, sr1);
    }
    
}
