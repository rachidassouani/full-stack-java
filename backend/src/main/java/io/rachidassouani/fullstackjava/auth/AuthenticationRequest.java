package io.rachidassouani.fullstackjava.auth;

public record AuthenticationRequest(
        String email,
        String password) {
}
