package com.example.servlet.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.example.servlet.models.AuthEvent;
import com.example.servlet.models.User;
import com.example.servlet.services.AuthEventService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
// private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ProfileServlet.class);
    private AuthEventService authEventService;

    @Override
    public void init() throws ServletException {
        ApplicationContext springContext = (ApplicationContext) getServletContext().getAttribute("springContext");
        if (springContext != null) {
            try {
                this.authEventService = springContext.getBean(AuthEventService.class);
                logger.info("AuthEventService successfully initialized");
            } catch (Exception e) {
                logger.error("Failed to get AuthEventService bean: {}", e.getMessage());
            }
        } else {
            logger.error("Spring context not found in servlet context");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/signup");
            return;
        }

        User user = (User) session.getAttribute("user");
        req.setAttribute("user", user);

        // Load authentication history
        List<AuthEvent> authHistory = Collections.emptyList();
        if (authEventService != null) {
            try {
                authHistory = authEventService.getUserAuthHistory(user.getId());
                logger.info("Loaded {} authentication events for user ID: {}",
                        authHistory.size(), user.getId());
            } catch (Exception e) {
                logger.error("Error loading authentication history: {}", e.getMessage());
            }
        } else {
            logger.warn("AuthEventService is null, cannot load authentication history");
        }
        req.setAttribute("authHistory", authHistory);

        logger.info("Loaded {} authentication events for user ID: {}", authHistory.size(), user.getId());
        logger.info("Forwarding to profile.jsp");

        logger.info("Forwarding to profile.jsp");
        req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
    }
}
