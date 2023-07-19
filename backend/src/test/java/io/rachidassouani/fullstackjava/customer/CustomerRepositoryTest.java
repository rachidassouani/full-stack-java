package io.rachidassouani.fullstackjava.customer;

import com.github.javafaker.Faker;
import io.rachidassouani.fullstackjava.TestContainersTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends TestContainersTest {

    @Autowired
    private CustomerRepository underTest;


    @BeforeEach
    void setUp() {

    }

    @Test
    void existsCustomerByEmail() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();

        Customer customer = new Customer(firstName, lastName, email);

        underTest.save(customer);

        boolean isCustomerExistsByEmail = underTest.existsCustomerByEmail(email);

        Assertions.assertThat(isCustomerExistsByEmail).isTrue();
    }

    @Test
    void existsCustomerByEmailFailsWhenEmailNotExists() {
        Faker faker = new Faker();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();

        boolean isCustomerExistsByEmail = underTest.existsCustomerByEmail(email);

        Assertions.assertThat(isCustomerExistsByEmail).isFalse();
    }

    @Test
    void existsCustomerById() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();

        Customer customer = new Customer(firstName, lastName, email);

        Customer savedCustomer = underTest.save(customer);

        boolean isCustomerExistsById = underTest.existsCustomerById(savedCustomer.getId());

        Assertions.assertThat(isCustomerExistsById).isTrue();
    }

    @Test
    void existsCustomerByIdFailsWhenIdNotExists() {
        Long id = -1l;

        boolean isCustomerExistsById = underTest.existsCustomerById(id);

        Assertions.assertThat(isCustomerExistsById).isFalse();
    }
}