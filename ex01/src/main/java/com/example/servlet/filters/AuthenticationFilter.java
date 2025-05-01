package com.example.servlet.filters;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest; 
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    // Fix the paths to match your actual servlet mappings (lowercase)
    private static final String[] PUBLIC_URLS = {"/signin", "/signup", "/hello", ""};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String relativePath = path.substring(contextPath.length());
        
        logger.info("Request path: {}", relativePath);

        // Check if this is a protected path that requires authentication
        if (!isPublicPath(relativePath)) {
            HttpSession session = httpRequest.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                logger.info("Unauthorized access attempt to {}, redirecting to signin", relativePath);
                // Return 403 Forbidden response
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\": \"Access denied\"}");
                return;
            }
            logger.info("Authenticated user accessing {}", relativePath);
        } else {
            logger.info("Public path access: {}", relativePath);
        }

        chain.doFilter(request, response);
        logger.info("Outgoing response: Status={}", httpResponse.getStatus());
    }

    public boolean isPublicPath(String path) {
        // Handle root path
        if (path.equals("/")) {
            return true;
        }
        
        for(String url : PUBLIC_URLS) {
            if(path.equals(url)) {
                return true;
            }
        }
        return false;
    }
}