package io.rachidassouani.fullstackjava.customer;

import io.rachidassouani.fullstackjava.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> findAllCustomers() {
        return customerDao.findAllCustomers();
    }

    public Customer findCustomerById(Long customerId) {
        return customerDao.findCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id [%s] not found".formatted(customerId)));
    }
}