package io.rachidassouani.fullstackjava;

import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class TestContainersTest {

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(
                POSTGRESQL_CONTAINER.getJdbcUrl(),
                POSTGRESQL_CONTAINER.getUsername(),
                POSTGRESQL_CONTAINER.getPassword()).load();
        flyway.migrate();
    }

    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("dao_unit_test")
                    .withUsername("rachid")
                    .withPassword("password");

    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {

        dynamicPropertyRegistry.add(
                "spring.datasource.url", () -> POSTGRESQL_CONTAINER.getJdbcUrl());

        dynamicPropertyRegistry.add(
                "spring.datasource.username", () -> POSTGRESQL_CONTAINER.getUsername());

        dynamicPropertyRegistry.add(
                "spring.datasource.password", () -> POSTGRESQL_CONTAINER.getPassword());
    }

    @Test
    void canStartPostgresDB() {
        Assertions.assertThat(POSTGRESQL_CONTAINER.isCreated()).isTrue();
        Assertions.assertThat(POSTGRESQL_CONTAINER.isRunning()).isTrue();
    }

    public static DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(POSTGRESQL_CONTAINER.getDriverClassName())
                .url(POSTGRESQL_CONTAINER.getJdbcUrl())
                .username(POSTGRESQL_CONTAINER.getUsername())
                .password(POSTGRESQL_CONTAINER.getPassword())
                .build();
    }
}