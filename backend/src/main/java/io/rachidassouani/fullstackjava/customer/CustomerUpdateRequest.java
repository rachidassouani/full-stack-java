package io.rachidassouani.fullstackjava.customer;

public record CustomerUpdateRequest(
        String firstName,
        String lastName,
        String email) {
}
