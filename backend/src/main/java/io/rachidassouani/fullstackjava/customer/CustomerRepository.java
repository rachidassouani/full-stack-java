package io.rachidassouani.fullstackjava.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Long customerId);
}
