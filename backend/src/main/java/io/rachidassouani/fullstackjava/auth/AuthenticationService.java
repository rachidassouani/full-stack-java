package io.rachidassouani.fullstackjava.auth;

import io.rachidassouani.fullstackjava.customer.Customer;
import io.rachidassouani.fullstackjava.customer.CustomerDTO;
import io.rachidassouani.fullstackjava.customer.CustomerDTOMapper;
import io.rachidassouani.fullstackjava.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomerDTOMapper customerDTOMapper;
    private final JWTUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 CustomerDTOMapper customerDTOMapper,
                                 JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.customerDTOMapper = customerDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        // get the customer from the authenticate object
        Customer customer = (Customer) authenticate.getPrincipal();

        // cast the customer to customerDTO
        CustomerDTO customerDTO = customerDTOMapper.apply(customer);

        // issue a token
        String token = jwtUtil.issueToken(customerDTO.email(), customerDTO.roles());

        return new AuthenticationResponse(customerDTO, token);
    }
}
