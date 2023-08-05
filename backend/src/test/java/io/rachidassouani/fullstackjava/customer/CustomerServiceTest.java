package io.rachidassouani.fullstackjava.customer;

import io.rachidassouani.fullstackjava.exception.DuplicateResourceException;
import io.rachidassouani.fullstackjava.exception.RequestValidationException;
import io.rachidassouani.fullstackjava.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;

    @Mock
    private CustomerDao customerDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao, passwordEncoder, customerDTOMapper);
    }

    @Test
    void findAllCustomers() {
        underTest.findAllCustomers();
        verify(customerDao).findAllCustomers();
    }

    @Test
    void findCustomerById() {
        Long id = 1l;

        Customer customer = new Customer(
                "firsName",
                "lastName",
                "email",
                "password");
        CustomerDTO expectedCustomer = customerDTOMapper.apply(customer);

        when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerDTO returnedCustomer = underTest.findCustomerById(id);

        assertThat(returnedCustomer).isEqualTo(expectedCustomer);
    }

    @Test
    void throwWhenFindCustomerByIdReturnEmptyOptional() {
        Long id = -1l;

        when(customerDao.findCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.findCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] not found".formatted(id));
    }

    @Test
    void saveCustomer() {
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "email";
        String password = "password";

        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(firstName, lastName, email, password);

        when(customerDao.isCustomerExistsWithEmail(email)).thenReturn(false);

        String encodedPassword = "FDKLHGKLDFHGVFLDJVMLD";
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        underTest.saveCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).saveCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getFirstName()).isEqualTo(request.firstName());
        assertThat(capturedCustomer.getLastName()).isEqualTo(request.lastName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    void saveCustomerWillThrowExceptionWhenEmailExists() {

        String firstName = "firstName";
        String lastName = "lastName";
        String email = "email";
        String password = "password";

        CustomerRegistrationRequest request =
                new CustomerRegistrationRequest(firstName, lastName, email, password);

        when(customerDao.isCustomerExistsWithEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> underTest.saveCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        verify(customerDao, never()).saveCustomer(any());
    }


    @Test
    void deleteCustomerById() {
        Long id = 1l;

        when(customerDao.isCustomerExistsWithId(id)).thenReturn(true);

        underTest.deleteCustomerById(id);

        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void deleteCustomerByIdWillThrowExceptionWhenCustomerIsNotExists() {
        Long id = 1l;

        when(customerDao.isCustomerExistsWithId(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] not found".formatted(id));

        verify(customerDao, never()).deleteCustomerById(id);
    }

    @Test
    void updateAllCustomerProperties() {
        Long id = 1l;
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        String newEmail = "newEmail";

        CustomerUpdateRequest customerUpdateRequest =
                new CustomerUpdateRequest(newFirstName, newLastName, newEmail);

        Customer customerFromDB = new Customer("Rachid", "Assouani", "rachid@assouani", "password");

        when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customerFromDB));
        when(customerDao.isCustomerExistsWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id, customerUpdateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getFirstName()).isEqualTo(customerUpdateRequest.firstName());
        assertThat(capturedCustomer.getLastName()).isEqualTo(customerUpdateRequest.lastName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerUpdateRequest.email());
    }

    @Test
    void updateOnlyCustomerFirstName() {
        Long id = 1l;
        String newFirstName = "newFirstName";

        CustomerUpdateRequest customerUpdateRequest =
                new CustomerUpdateRequest(newFirstName, null, null);

        Customer customerFromDB = new Customer("Rachid", "Assouani", "rachid@assouani", "password");

        when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customerFromDB));

        underTest.updateCustomer(id, customerUpdateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getFirstName()).isEqualTo(customerUpdateRequest.firstName());
        assertThat(capturedCustomer.getLastName()).isEqualTo(customerFromDB.getLastName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerFromDB.getEmail());
    }

    @Test
    void updateOnlyCustomerLastName() {
        Long id = 1l;
        String newLastName = "newLastName";

        CustomerUpdateRequest customerUpdateRequest =
                new CustomerUpdateRequest(null, newLastName, null);

        Customer customerFromDB = new Customer("Rachid", "Assouani", "rachid@assouani", "password");

        when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customerFromDB));

        underTest.updateCustomer(id, customerUpdateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getFirstName()).isEqualTo(customerFromDB.getFirstName());
        assertThat(capturedCustomer.getLastName()).isEqualTo(customerUpdateRequest.lastName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerFromDB.getEmail());
    }

    @Test
    void updateOnlyCustomerEmail() {
        Long id = 1l;
        String newEmail = "newEmail";

        CustomerUpdateRequest customerUpdateRequest =
                new CustomerUpdateRequest(null, null, newEmail);

        Customer customerFromDB = new Customer("Rachid", "Assouani", "rachid@assouani", "password");

        when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customerFromDB));

        underTest.updateCustomer(id, customerUpdateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getFirstName()).isEqualTo(customerFromDB.getFirstName());
        assertThat(capturedCustomer.getLastName()).isEqualTo(customerFromDB.getLastName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerUpdateRequest.email());
    }

    @Test
    void willThrowExceptionWhenTryingToUpdateCustomerEmailWhenAlreadyExists() {
        Long id = 1l;
        String existingEmail = "existingemail@email.com";

        CustomerUpdateRequest customerUpdateRequest =
                new CustomerUpdateRequest("firstName", "lastName", existingEmail);

        Customer customerFromDB = new Customer("Rachid", "Assouani", "dbemail@mail.com", "password");

        when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customerFromDB));
        when(customerDao.isCustomerExistsWithEmail(existingEmail)).thenReturn(true);

        assertThatThrownBy(() -> underTest.updateCustomer(id, customerUpdateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void throwExceptionWhenNoChangesDetectedWhenTryingToUpdateCustomerProperties() {
        Long id = 1l;
        String firstName = "firstName";
        String lastName = "lastName";
        String email = "email@email.com";

        CustomerUpdateRequest customerUpdateRequest =
                new CustomerUpdateRequest(firstName, lastName, email);

        Customer customerFromDB = new Customer(firstName, lastName, email, "password");

        when(customerDao.findCustomerById(id)).thenReturn(Optional.of(customerFromDB));

        assertThatThrownBy(() -> underTest.updateCustomer(id, customerUpdateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No data changes found");

        verify(customerDao, never()).updateCustomer(any());
    }
}