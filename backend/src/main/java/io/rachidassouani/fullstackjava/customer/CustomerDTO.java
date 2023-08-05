package io.rachidassouani.fullstackjava.customer;

import java.util.List;

public record CustomerDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        List<String> roles) { }
