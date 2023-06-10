package io.rachidassouani.fullstackjava.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> findAllCustomers();
    Optional<Customer> findCustomerById(Long id);
}
