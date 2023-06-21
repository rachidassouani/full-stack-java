package io.rachidassouani.fullstackjava;

import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestContainersTest {

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(
                POSTGRE_SQL_CONTAINER.getJdbcUrl(),
                POSTGRE_SQL_CONTAINER.getUsername(),
                POSTGRE_SQL_CONTAINER.getPassword()).load();
        flyway.migrate();
    }

    @Container
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("dao_unit_test")
                    .withUsername("rachid")
                    .withPassword("password");

    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {

        dynamicPropertyRegistry.add(
                "spring.datasource.url", () -> POSTGRE_SQL_CONTAINER.getJdbcUrl());

        dynamicPropertyRegistry.add(
                "spring.datasource.username", () -> POSTGRE_SQL_CONTAINER.getUsername());

        dynamicPropertyRegistry.add(
                "spring.datasource.password", () -> POSTGRE_SQL_CONTAINER.getPassword());
    }

    @Test
    void canStartPostgresDB() {
        Assertions.assertThat(POSTGRE_SQL_CONTAINER.isCreated()).isTrue();
        Assertions.assertThat(POSTGRE_SQL_CONTAINER.isRunning()).isTrue();
    }

}