package com.example.servlet.listener;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.servlet.config.AppConfig;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class SpringContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        // Initialize Spring Context with our configuration
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        // Store Spring context in the ServletContext
        servletContext.setAttribute("springContext", context);

        System.out.println("Spring context initialized successfully!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        AnnotationConfigApplicationContext context
                = (AnnotationConfigApplicationContext) servletContext.getAttribute("springContext");

        if (context != null) {
            context.close();
            System.out.println("Spring context closed successfully!");
        }
    }
}
