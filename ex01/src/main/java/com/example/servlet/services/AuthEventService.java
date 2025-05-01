package com.example.servlet.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.servlet.models.AuthEvent;
import com.example.servlet.models.User;
import com.example.servlet.repositories.AuthEventRepository;

@Service
public class AuthEventService {

    private final AuthEventRepository authEventRepository;

    @Autowired
    public AuthEventService(AuthEventRepository authEventRepository) {
        this.authEventRepository = authEventRepository;
    }

    public void recordLogin(User user, String ipAddress) {
        AuthEvent authEvent = new AuthEvent();
        authEvent.setUserId(user.getId());
        authEvent.setTimestamp(LocalDateTime.now());
        authEvent.setIpAddress(ipAddress);

        authEventRepository.save(authEvent);
    }

    public List<AuthEvent> getUserAuthHistory(Long userId) {
        return authEventRepository.findByUserId(userId);
    }
}
