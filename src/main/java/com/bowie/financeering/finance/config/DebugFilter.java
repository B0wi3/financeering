package com.bowie.financeering.finance.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DebugFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        System.out.println("=== FILTER CHAIN START ===");
        System.out.println("URI: " + httpRequest.getRequestURI());
        System.out.println("Auth Header: " + httpRequest.getHeader("Authorization"));
        System.out.println("==========================");

        chain.doFilter(request, response);

        System.out.println("=== FILTER CHAIN END ===");
    }
}