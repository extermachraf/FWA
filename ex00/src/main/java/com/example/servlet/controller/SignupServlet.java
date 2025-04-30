package com.example.servlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.example.servlet.models.User;
import com.example.servlet.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/signup")
@Component
public class SignupServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        ApplicationContext springContext = (ApplicationContext) getServletContext().getAttribute("springContext");
        this.userService = springContext.getBean(UserService.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            User user = new User();
            user.setFirst_name(req.getParameter("first_name"));
            user.setLast_name(req.getParameter("last_name"));
            user.setPhone_number(req.getParameter("phone_number"));
            user.setEmail(req.getParameter("email"));
            user.setPassword(req.getParameter("password"));

            userService.signup(user);

            // Create session and store user
            HttpSession session = req.getSession();
            session.setAttribute("user", user);

            // Redirect to profile page
            resp.sendRedirect(req.getContextPath() + "/profile");

        } catch (Exception e) {
            // Store error message in session
            req.getSession().setAttribute("error", e.getMessage());
            // Redirect back to signup page
            resp.sendRedirect(req.getContextPath() + "/signup");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");

        // Get error message if exists
        HttpSession session = req.getSession();
        String error = (String) session.getAttribute("error");
        // Remove error from session after getting it
        session.removeAttribute("error");

        PrintWriter out = resp.getWriter();
        out.println("""
            <html>
            <body>
                <h2>Sign Up</h2>
                %s
                <form action="%s/signup" method="post">
                    <div><input type="text" name="first_name" placeholder="First Name" required></div>
                    <div><input type="text" name="last_name" placeholder="Last Name" required></div>
                    <div><input type="tel" name="phone_number" placeholder="Phone Number" required></div>
                    <div><input type="email" name="email" placeholder="Email" required></div>
                    <div><input type="password" name="password" placeholder="Password" required></div>
                    <button type="submit">Sign Up</button>
                </form>
            </body>
            </html>
            """.formatted(
                error != null ? "<p style='color: red'>" + error + "</p>" : "",
                req.getContextPath()
        ));
    }
}
