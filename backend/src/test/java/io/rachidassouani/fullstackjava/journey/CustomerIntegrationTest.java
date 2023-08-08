package io.rachidassouani.fullstackjava.journey;

import com.github.javafaker.Faker;
import io.rachidassouani.fullstackjava.customer.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
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

    private static final String CUSTOMER_PATH = "api/v1/customers";

    private final CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();

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
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomerDTOs = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange() // sending a request
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        // get the id
        var id = allCustomerDTOs.stream()
                .filter(c -> c.email().equals(email))
                .map(c -> c.id())
                .findFirst()
                .orElseThrow();

        // make sure that the customer is present
        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                firstName,
                lastName,
                email,
                List.of("ROLE_USER"));

        assertThat(allCustomerDTOs)
                .contains(expectedCustomer);

        // get customer by id
        CustomerDTO foundedCustomer = webTestClient.get()
                .uri(CUSTOMER_PATH + "/{id}",  id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        assertThat(foundedCustomer.id()).isEqualTo(expectedCustomer.id());
        assertThat(foundedCustomer.firstName()).isEqualTo(expectedCustomer.firstName());
        assertThat(foundedCustomer.lastName()).isEqualTo(expectedCustomer.lastName());
        assertThat(foundedCustomer.email()).isEqualTo(expectedCustomer.email());
    }

    @Test
    void deleteCustomerTest() {
        // create registration request
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = "integrationTest-" + UUID.randomUUID() + faker.internet().safeEmailAddress();
        String password = faker.internet().password();

        String email2 = "integrationTest-" + UUID.randomUUID() + faker.internet().safeEmailAddress();

        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(firstName, lastName, email, password);

        CustomerRegistrationRequest request2 =
                new CustomerRegistrationRequest(firstName, lastName, email2, password);

        // send post request for customer 1
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // send post request for customer 2
        String jwtToken2 = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request2), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        // get the id
        var id = allCustomers.stream()
                .filter(c -> c.email().equals(email2))
                .map(c -> c.id())
                .findFirst()
                .orElseThrow();

        // delete customer by its id
        webTestClient.delete()
                .uri(CUSTOMER_PATH + "/{id}",  id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri(CUSTOMER_PATH + "/{id}",  id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isNotFound();
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
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        // get the id
        var id = allCustomers.stream()
                .filter(c -> c.email().equals(email))
                .map(c -> c.id())
                .findFirst()
                .orElseThrow();

        String newFirstName = "updatedFirstName";
        String newLastName = "updatedLastName";
        //String newEmail = "updatedEmail@updatedEmail.com" + UUID.randomUUID() + faker.internet().safeEmailAddress();

        CustomerUpdateRequest updateRequest =
                new CustomerUpdateRequest(newFirstName, newLastName, null);

        // update customer by its id
        webTestClient.put()
                .uri(CUSTOMER_PATH + "/{id}",  id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk();

        CustomerDTO updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .returnResult()
                .getResponseBody();

        CustomerDTO expectedCustomer = new CustomerDTO(id, newFirstName, newLastName, email, List.of("ROLE_USER"));

        assertThat(updatedCustomer).isEqualToComparingFieldByField(expectedCustomer);
    }
}
