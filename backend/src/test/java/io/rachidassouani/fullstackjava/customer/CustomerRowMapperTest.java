package io.rachidassouani.fullstackjava.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {

        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getLong("id")).thenReturn(1l);
        when(resultSet.getString("first_name")).thenReturn("mockedFirstName");
        when(resultSet.getString("last_name")).thenReturn("mockedLastName");
        when(resultSet.getString("email")).thenReturn("mockedEmail@mockedEmail.com");

        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        Customer customer = customerRowMapper.mapRow(resultSet, 1);

        Customer expectedCustomer =
                new Customer(1l, "mockedFirstName", "mockedLastName", "mockedEmail@mockedEmail.com");

        assertThat(customer.getId()).isEqualTo(expectedCustomer.getId());
        assertThat(customer.getFirstName()).isEqualTo(expectedCustomer.getFirstName());
        assertThat(customer.getLastName()).isEqualTo(expectedCustomer.getLastName());
        assertThat(customer.getEmail()).isEqualTo(expectedCustomer.getEmail());
    }
}