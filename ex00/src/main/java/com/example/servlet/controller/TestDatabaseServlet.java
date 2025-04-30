package com.example.servlet.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/test-db")
public class TestDatabaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        try {
            ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute("springContext");

            NamedParameterJdbcTemplate jdbcTemplate
                    = context.getBean(NamedParameterJdbcTemplate.class);

            // Simple test query
            int result = jdbcTemplate.queryForObject(
                    "SELECT 1",
                    Collections.emptyMap(),
                    Integer.class
            );

            out.println("Database connection test successful!");
            out.println("Test query result: " + result);

        } catch (Exception e) {
            out.println("Database connection test failed!");
            out.println("Error: " + e.getMessage());
            e.printStackTrace(out);
        }
    }
}
