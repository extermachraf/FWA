package com.example.servlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.example.servlet.models.User;
import com.example.servlet.services.AuthEventService;
import com.example.servlet.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/signin")
public class SigninServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(SigninServlet.class);
    private UserService userService;
    private AuthEventService authEventService;

    @Override
    public void init() throws ServletException {
        ApplicationContext springContext = (ApplicationContext) getServletContext().getAttribute("springContext");
        this.userService = springContext.getBean(UserService.class);
        this.authEventService = springContext.getBean(AuthEventService.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            User user = userService.signin(email, password);
            // Record the authentication event
            String ipAddress = getClientIpAddress(req);
            authEventService.recordLogin(user, ipAddress);
            logger.info("Authentication event recorded for user ID: {} from IP: {}", user.getId(), ipAddress);

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

    private String getClientIpAddress(HttpServletRequest request) {

        String ipAddress = request.getHeader("X-Forwarded-For");
        // Try standard headers used by proxies first
        logger.info("X-Forwarded-For: {}", ipAddress);

        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
            logger.info("Proxy-Client-IP: {}", ipAddress);
        }

        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
            logger.info("WL-Proxy-Client-IP: {}", ipAddress);
        }

        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
            logger.info("HTTP_X_FORWARDED_FOR: {}", ipAddress);
        }

        // Last resort - get the direct remote address
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            logger.info("RemoteAddr: {}", ipAddress);
        }

        return ipAddress;
    }
}
