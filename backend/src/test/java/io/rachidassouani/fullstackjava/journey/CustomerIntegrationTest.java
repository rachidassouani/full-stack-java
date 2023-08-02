package io.rachidassouani.fullstackjava.journey;

import com.github.javafaker.Faker;
import io.rachidassouani.fullstackjava.customer.Customer;
import io.rachidassouani.fullstackjava.customer.CustomerRegistrationRequest;
import io.rachidassouani.fullstackjava.customer.CustomerUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final String CUSTOMER_URI = "api/v1/customers";


    @Test
    void registerCustomerTest() {
        // create registration request
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = "integrationTest-" + UUID.randomUUID() + faker.internet().safeEmailAddress();
        String password = faker.internet().password();

        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(firstName, lastName, email, password);

        // send post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        // make sure that the customer is present
        Customer expectedCustomer = new Customer(firstName, lastName, email, password);

        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        // get the id
        var id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        expectedCustomer.setId(id);

        // get customer by id
        Customer foundedCustomer = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}",  id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        assertThat(foundedCustomer.getId()).isEqualTo(expectedCustomer.getId());
        assertThat(foundedCustomer.getFirstName()).isEqualTo(expectedCustomer.getFirstName());
        assertThat(foundedCustomer.getLastName()).isEqualTo(expectedCustomer.getLastName());
        assertThat(foundedCustomer.getEmail()).isEqualTo(expectedCustomer.getEmail());
    }

    @Test
    void deleteCustomerTest() {
        // create registration request
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = "integrationTest-" + UUID.randomUUID() + faker.internet().safeEmailAddress();
        String password = faker.internet().password();

        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(firstName, lastName, email, password);

        // send post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        // get the id
        var id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        // delete customer by its id
        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}",  id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();


        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}",  id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateCustomerTest() {
        // create registration request
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = "integrationTest-" + UUID.randomUUID() + faker.internet().safeEmailAddress();
        String password = faker.internet().password();

        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(firstName, lastName, email, password);

        // send post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        // get the id
        var id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        String newFirstName = "updatedFirstName";
        String newLastName = "updatedLastName";
        String newEmail = "updatedEmail@updatedEmail.com" + UUID.randomUUID() + faker.internet().safeEmailAddress();

        CustomerUpdateRequest updateRequest =
                new CustomerUpdateRequest(newFirstName, newLastName, newEmail);

        // update customer by its id
        webTestClient.put()
                .uri(CUSTOMER_URI + "/{id}",  id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus().isOk();

        Customer updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer customer = new Customer(id, newFirstName, newLastName, newEmail, "password");

        assertThat(updatedCustomer).isEqualToComparingFieldByField(customer);
    }
}
