package com.example.servlet.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.servlet.models.User;

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
        logger.info("Forwarding to profile.jsp");
        req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
    }
}
