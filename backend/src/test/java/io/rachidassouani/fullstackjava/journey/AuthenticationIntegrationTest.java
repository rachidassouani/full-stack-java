package io.rachidassouani.fullstackjava.journey;

import com.github.javafaker.Faker;
import io.rachidassouani.fullstackjava.auth.AuthenticationRequest;
import io.rachidassouani.fullstackjava.auth.AuthenticationResponse;
import io.rachidassouani.fullstackjava.customer.CustomerDTO;
import io.rachidassouani.fullstackjava.customer.CustomerRegistrationRequest;
import io.rachidassouani.fullstackjava.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JWTUtil jwtUtil;

    private static final String AUTHENTICATION_PATH = "api/v1/auth/login";
    private static final String CUSTOMER_PATH = "api/v1/customers";

    @Test
    public void customerLoginWithCorrectCredentialsTest() {
        // create registration request
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = "loginTest-" + UUID.randomUUID() + faker.internet().safeEmailAddress();
        String password = faker.internet().password();

        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(firstName, lastName, email, password);

        // send post request
        webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();


        // creating login request
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);

        // sending login request
        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(AUTHENTICATION_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {})
                .returnResult();


        // get the Authorization value from the response
        String expectedToken = result.getResponseHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

        // get the customerDTO from the response
        CustomerDTO expectedCustomerDTO = result.getResponseBody().customerDTO();

        // check if the token and the customerDTO is valid
        assertThat(expectedToken).isNotNull();
        assertThat(expectedCustomerDTO).isNotNull();

        assertThat(jwtUtil.isTokenValid(expectedToken, expectedCustomerDTO.email()));
        assertThat(expectedCustomerDTO.firstName()).isEqualTo(firstName);
        assertThat(expectedCustomerDTO.lastName()).isEqualTo(lastName);
        assertThat(expectedCustomerDTO.email()).isEqualTo(email);
        assertThat(expectedCustomerDTO.roles()).isEqualTo(List.of("ROLE_USER"));
    }


    @Test
    public void customerLoginWithWrongCredentialsTest() {
        Faker faker = new Faker();
        String email = "loginFailedTest-" + UUID.randomUUID() + faker.internet().safeEmailAddress();
        String password = faker.internet().password();

        // creating login request
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);

        // sending login request
        webTestClient.post()
                .uri(AUTHENTICATION_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
