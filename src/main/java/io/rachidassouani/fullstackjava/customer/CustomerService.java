package io.rachidassouani.fullstackjava.customer;

import io.rachidassouani.fullstackjava.exception.DuplicateResourceException;
import io.rachidassouani.fullstackjava.exception.RequestValidationException;
import io.rachidassouani.fullstackjava.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
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

    public void saveCustomer(CustomerRegistrationRequest customerRegistrationRequest) {

        // check if customer's email is already exist
        if (customerDao.isCustomerExistsWithEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException("Email already taken");
        }

        // saving customer
        customerDao.saveCustomer(
                new Customer(
                        customerRegistrationRequest.firstName(),
                        customerRegistrationRequest.lastName(),
                        customerRegistrationRequest.email())
        );
    }

    public void deleteCustomerById(Long customerId) {

        // throw exception in case customer not exists
        if (!customerDao.isCustomerExistsWithId(customerId)) {
            throw new ResourceNotFoundException("Customer with id [%s] not found".formatted(customerId));
        }
        // delete customer by id
        customerDao.deleteCustomerById(customerId);
    }

    public void updateCustomer(Long customerId, CustomerUpdateRequest customerUpdateRequest) {

        Customer customer = findCustomerById(customerId);

        /*
         update the customer only if the request body that accompanies the request contains fields
         that are neither null nor empty and that are not equal to the existing field.
         */

        boolean changes = false;

        if (customerUpdateRequest.firstName() != null && !customerUpdateRequest.firstName().isEmpty()
                && !customerUpdateRequest.firstName().equals(customer.getFirstName())) {

            customer.setFirstName(customerUpdateRequest.firstName());
            changes = true;
        }

        if (customerUpdateRequest.lastName() != null && !customerUpdateRequest.lastName().isEmpty()
                && !customerUpdateRequest.lastName().equals(customer.getLastName())) {

            customer.setLastName(customerUpdateRequest.lastName());
            changes = true;
        }

        if (customerUpdateRequest.email() != null && !customerUpdateRequest.email().isEmpty()
                && !customerUpdateRequest.email().equals(customer.getEmail())) {

            if (customerDao.isCustomerExistsWithEmail(customerUpdateRequest.email())) {
                throw new DuplicateResourceException("email already taken");
            }
            customer.setEmail(customerUpdateRequest.email());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No data changes found");
        }
        customerDao.updateCustomer(customer);
    }
}