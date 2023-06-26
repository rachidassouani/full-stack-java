package io.rachidassouani.fullstackjava.customer;

import com.github.javafaker.Faker;
import io.rachidassouani.fullstackjava.TestContainersTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

class CustomerJDBCDataAccessServiceTest extends TestContainersTest {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                new JdbcTemplate(TestContainersTest.getDataSource()),
                customerRowMapper);
    }

    @Test
    void findAllCustomers() {

        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();

        Customer customer = new Customer(firstName, lastName, email);

        underTest.saveCustomer(customer);

        List<Customer> customers = underTest.findAllCustomers();

        Assertions.assertThat(customers).isNotEmpty();
    }

    @Test
    void findCustomerById() {

        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();

        Customer customer = new Customer(firstName, lastName, email);

        underTest.saveCustomer(customer);

        Long savedCustomerId = underTest.findAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        Optional<Customer> expectedCustomer = underTest.findCustomerById(savedCustomerId);

        Assertions.assertThat(expectedCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
                    Assertions.assertThat(c.getId()).isEqualTo(savedCustomerId);
                    Assertions.assertThat(c.getEmail()).isEqualTo(email);
                    Assertions.assertThat(c.getFirstName()).isEqualTo(firstName);
                    Assertions.assertThat(c.getLastName()).isEqualTo(lastName);
                });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        Long id = -1l;
        var customer = underTest.findCustomerById(id);
        Assertions.assertThat(customer).isEmpty();
    }

    @Test
    void saveCustomer() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();

        Customer customer = new Customer(firstName, lastName, email);

        underTest.saveCustomer(customer);

        Customer customerFromDB = underTest.findAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElseThrow();

        Assertions.assertThat(customerFromDB).isNotNull();


    }

    @Test
    void isCustomerExistsWithEmail() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();

        Customer customer = new Customer(firstName, lastName, email);

        underTest.saveCustomer(customer);

        boolean isCustomerExists = underTest.isCustomerExistsWithEmail(email);

        Assertions.assertThat(isCustomerExists).isTrue();
    }

    @Test
    void existsCustomerWithEmailShouldReturnsFalseWhenDoesNotExists() {
        Faker faker = new Faker();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();

        boolean isCustomerExists = underTest.isCustomerExistsWithEmail(email);

        Assertions.assertThat(isCustomerExists).isFalse();
    }

    @Test
    void isCustomerExistsWithId() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();

        Customer customer = new Customer(firstName, lastName, email);

        underTest.saveCustomer(customer);

        Long id = underTest.findAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        boolean isCustomerExists = underTest.isCustomerExistsWithId(id);

        Assertions.assertThat(isCustomerExists).isTrue();
    }

    @Test
    void existsCustomerWithIdShouldReturnsFalseWhenIdDoesNotExists() {
        Long id = -1l;

        boolean isCustomerExists = underTest.isCustomerExistsWithId(id);

        Assertions.assertThat(isCustomerExists).isFalse();
    }


    @Test
    void deleteCustomerById() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();

        Customer customer = new Customer(firstName, lastName, email);

        underTest.saveCustomer(customer);

        Long id = underTest.findAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        underTest.deleteCustomerById(id);

        boolean isCustomerExists = underTest.isCustomerExistsWithId(id);

        Assertions.assertThat(isCustomerExists).isFalse();

    }

    @Test
    void updateCustomer() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();

        Customer customer = new Customer(firstName, lastName, email);

        underTest.saveCustomer(customer);

        Customer customerToUpdate = underTest.findAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElseThrow();

        customerToUpdate.setFirstName("updatedFirstName");
        customerToUpdate.setLastName("updatedLastName");
        String newEmail = UUID.randomUUID() + "updatedEmail@gmail.com";
        customerToUpdate.setEmail(newEmail);

        underTest.updateCustomer(customerToUpdate);

        Customer updatedCustomer = underTest.findAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(newEmail))
                .findFirst()
                .orElseThrow();

        Assertions.assertThat(updatedCustomer.getFirstName()).isEqualTo("updatedFirstName");
        Assertions.assertThat(updatedCustomer.getLastName()).isEqualTo("updatedLastName");
        Assertions.assertThat(updatedCustomer.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();

        Customer customer = new Customer(firstName, lastName, email);

        underTest.saveCustomer(customer);

        Long id = underTest.findAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        customer.setId(id);

        underTest.updateCustomer(customer);

        Optional<Customer> customerFromDB = underTest.findCustomerById(id);

        Assertions.assertThat(customerFromDB)
                .isPresent()
                .hasValueSatisfying(c -> {
                    Assertions.assertThat(c.getId()).isEqualTo(id);
                    Assertions.assertThat(c.getFirstName()).isEqualTo(firstName);
                    Assertions.assertThat(c.getLastName()).isEqualTo(lastName);
                    Assertions.assertThat(c.getEmail()).isEqualTo(email);
                });
    }
}