package io.rachidassouani.fullstackjava.customer;

import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CustomerDTOMapper implements Function<Customer, CustomerDTO> {

    @Override
    public CustomerDTO apply(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAuthorities()
                        .stream()
                        .map(role -> role.getAuthority())
                        .collect(Collectors.toList()));
    }
}
