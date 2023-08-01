package io.rachidassouani.fullstackjava.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> findAllCustomers() {

        String query = """
                SELECT id, first_name, last_name, email, password
                FROM customer
                """;

        List<Customer> customers =  jdbcTemplate.query(query, customerRowMapper);

        return customers;
    }

    @Override
    public Optional<Customer> findCustomerById(Long id) {

        String query = """
                SELECT id, first_name, last_name, email, password
                FROM customer
                WHERE id = ? 
                """;
        return jdbcTemplate
                .query(query, customerRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void saveCustomer(Customer customer) {
        var query = """
                INSERT INTO customer (first_name, last_name, email, password)
                VALUES(?, ?, ?, ?)                
                """;
        jdbcTemplate.update(query,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPassword());
    }

    @Override
    public boolean isCustomerExistsWithEmail(String email) {
        String query = """
                SELECT COUNT(id)
                FROM customer
                WHERE email = ?
                """;

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, email);

        return count != null && count != 0;
    }

    @Override
    public boolean isCustomerExistsWithId(Long customerId) {
        String query = """
                SELECT COUNT(id)
                FROM customer
                WHERE id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, customerId);

        return count != null && count != 0;
    }

    @Override
    public void deleteCustomerById(Long customerId) {
        String query = """
                DELETE
                FROM customer
                WHERE id = ? 
                """;
        jdbcTemplate.update(query, customerId);
    }

    @Override
    public void updateCustomer(Customer customer) {
        if (customer.getFirstName() != null) {
            String query = """
                    UPDATE customer
                    SET first_name = ? 
                    WHERE id = ?
                    """;
            jdbcTemplate.update(query, customer.getFirstName(), customer.getId());
        }

        if (customer.getLastName() != null) {
            String query = """
                    UPDATE customer
                    SET last_name = ? 
                    WHERE id = ?
                    """;
            jdbcTemplate.update(query, customer.getLastName(), customer.getId());
        }

        if (customer.getEmail() != null) {
            String query = """
                    UPDATE customer
                    SET email = ?
                    WHERE id = ?
                    """;
            jdbcTemplate.update(query, customer.getEmail(), customer.getId());
        }
    }

    @Override
    public Optional<Customer> findCustomerByEmail(String email) {
        String query = """
                SELECT id, first_name, last_name, email, password
                FROM customer
                WHERE email = ?
                """;
        return jdbcTemplate
                .query(query, customerRowMapper, email)
                .stream()
                .findFirst();
    }
}