package io.rachidassouani.fullstackjava.customer;

import io.rachidassouani.fullstackjava.TestContainersTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest extends TestContainersTest {

    private CustomerJPADataAccessService underTest;

    @Mock
    private CustomerRepository customerRepository;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void findAllCustomers() {
        underTest.findAllCustomers();
        verify(customerRepository).findAll();
    }

    @Test
    void findCustomerById() {
        Long id = 1l;
        underTest.findCustomerById(id);
        verify(customerRepository).findById(id);
    }

    @Test
    void saveCustomer() {
        Customer customer = new Customer(
                "randomFirstName",
                "randomLastName",
                "randomEmail");

        underTest.saveCustomer(customer);
        verify(customerRepository).save(customer);
    }

    @Test
    void isCustomerExistsWithEmail() {
        String email = "randomEmail@test.com";
        underTest.isCustomerExistsWithEmail(email);
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void isCustomerExistsWithId() {
        Long id = 1l;
        underTest.isCustomerExistsWithId(id);
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        Long id = 1l;
        underTest.deleteCustomerById(id);
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        Customer customer = new Customer(
                "randomFirstName",
                "randomLastName",
                "randomEmail");

        underTest.updateCustomer(customer);
        verify(customerRepository).save(customer);
    }
}