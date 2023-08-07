package io.rachidassouani.fullstackjava.auth;

import io.rachidassouani.fullstackjava.customer.CustomerDTO;

public record AuthenticationResponse(
        CustomerDTO customerDTO,
        String token) {
}
