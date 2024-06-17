package com.project.gui.user;

public record UserRegistrationRequest(
        String requestType,
        String email,
        String password
) {
    public UserRegistrationRequest(String requestType, String email) {
        this(requestType, email, null);
    }
}
