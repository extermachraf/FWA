package com.example.servlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.context.ApplicationContext;

import com.example.servlet.models.User;
import com.example.servlet.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/signin")
public class SigninServlet extends HttpServlet {

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
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            User user = userService.signin(email, password);

            HttpSession session = req.getSession();
            session.setAttribute("user", user);

            resp.sendRedirect(req.getContextPath() + "/profile");

        } catch (Exception e) {
            req.getSession().setAttribute("error", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/signin");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");

        HttpSession session = req.getSession();
        String error = (String) session.getAttribute("error");
        session.removeAttribute("error");

        PrintWriter out = resp.getWriter();
        out.println("""
            <html>
            <body>
                <h2>Sign In</h2>
                %s
                <form action="%s/signin" method="post">
                    <div><input type="email" name="email" placeholder="Email" required></div>
                    <div><input type="password" name="password" placeholder="Password" required></div>
                    <button type="submit">Sign In</button>
                </form>
                <p>Don't have an account? <a href="%s/signup">Sign Up</a></p>
            </body>
            </html>
            """.formatted(
                error != null ? "<p style='color: red'>" + error + "</p>" : "",
                req.getContextPath(),
                req.getContextPath()
        ));
    }
}
