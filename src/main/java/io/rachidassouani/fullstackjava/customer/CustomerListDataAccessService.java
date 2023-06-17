package io.rachidassouani.fullstackjava.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {

    @Override
    public List<Customer> findAllCustomers() {
        return null;
    }

    @Override
    public Optional<Customer> findCustomerById(Long id) {
        return Optional.empty();
    }

    @Override
    public void saveCustomer(Customer customer) {

    }

    @Override
    public boolean isCustomerExistsWithEmail(String email) {
        return false;
    }

    @Override
    public boolean isCustomerExistsWithId(Long customerId) {
        return false;
    }

    @Override
    public void deleteCustomerById(Long customerId) {

    }
}
