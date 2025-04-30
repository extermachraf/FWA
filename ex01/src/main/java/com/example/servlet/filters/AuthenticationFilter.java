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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final String[] PUBLIC_URLS = {"/signIn", "/signUp"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        logger.info("this si the path = {} ", 
                path);

        chain.doFilter(request, response);

        logger.info("Outgoing response: Status={}", httpResponse.getStatus());
        // if(isPublicPath(path)){
            
        // }
    }


    public boolean isPublicPath(String path) {
        for(String url : PUBLIC_URLS) {
            if(url.equals(path))
                return true;
        }
        return false;
    }
}
