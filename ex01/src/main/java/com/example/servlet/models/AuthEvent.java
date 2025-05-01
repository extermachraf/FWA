package com.example.servlet.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuthEvent {

    private Long id;
    private Long userId;
    private LocalDateTime timestamp;
    private String ipAddress;

    // Constructors
    public AuthEvent() {
    }

    public AuthEvent(Long userId, LocalDateTime timestamp, String ipAddress) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    // Convenience methods for display
    public String getFormattedDate() {
        return timestamp.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
    }

    public String getFormattedTime() {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
