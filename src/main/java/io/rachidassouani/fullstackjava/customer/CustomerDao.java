package io.rachidassouani.fullstackjava.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> findAllCustomers();
    Optional<Customer> findCustomerById(Long id);
    void saveCustomer(Customer customer);
    boolean isCustomerExistsWithEmail(String email);
    boolean isCustomerExistsWithId(Long customerId);
    void deleteCustomerById(Long customerId);
}
