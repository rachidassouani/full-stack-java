package io.rachidassouani.fullstackjava.customer;

import io.rachidassouani.fullstackjava.exception.DuplicateResourceException;
import io.rachidassouani.fullstackjava.exception.RequestValidationException;
import io.rachidassouani.fullstackjava.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    private final CustomerDao customerDao;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDTOMapper customerDTOMapper;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao, PasswordEncoder passwordEncoder, CustomerDTOMapper customerDTOMapper) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
        this.customerDTOMapper = customerDTOMapper;
    }

    public List<CustomerDTO> findAllCustomers() {
        return customerDao.findAllCustomers()
                .stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());
    }

    public CustomerDTO findCustomerById(Long customerId) {
        return customerDao.findCustomerById(customerId)
                .map(customerDTOMapper)
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
                        customerRegistrationRequest.email(),
                        passwordEncoder.encode(customerRegistrationRequest.password()))
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

        Customer customer = customerDao.findCustomerById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id [%s] not found".formatted(customerId)));;

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