package io.rachidassouani.fullstackjava.customer;

import io.rachidassouani.fullstackjava.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final JWTUtil jwtUtil;

    public CustomerController(CustomerService customerService, JWTUtil jwtUtil) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<CustomerDTO> findAllCustomers() {
        return customerService.findAllCustomers();
    }

    @GetMapping("{customerId}")
    public CustomerDTO findCustomerById(@PathVariable("customerId") Long customerId) {
        return customerService.findCustomerById(customerId);
    }

    @PostMapping
    // we should add validation using Jakarta Bean Validation 
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerRegistrationRequest registrationRequest) {
        customerService.saveCustomer(registrationRequest);
        String jwtToken = jwtUtil.issueToken(registrationRequest.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Long customerId) {
        customerService.deleteCustomerById(customerId);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(@PathVariable("customerId") Long customerId,
                               @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        customerService.updateCustomer(customerId, customerUpdateRequest);
    }
}