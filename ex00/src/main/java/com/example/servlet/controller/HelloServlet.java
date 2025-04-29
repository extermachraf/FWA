package com.example.servlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.context.ApplicationContext;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "HelloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        // Get Spring context from servlet context
        ServletContext servletContext = getServletContext();
        ApplicationContext springContext = (ApplicationContext) servletContext.getAttribute("springContext");

        // Get the welcome message from Spring context
        String welcomeMessage = "Default Message";
        if (springContext != null) {
            welcomeMessage = springContext.getBean("welcomeMessage", String.class);
        }

        out.println("<html><body>");
        out.println("<h1>" + welcomeMessage + "</h1>");
        out.println("<p>Servlet is running on port 4000</p>");
        out.println("</body></html>");
    }
}
