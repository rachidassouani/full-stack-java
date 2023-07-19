create table customer (
    id BIGSERIAL,
    email VARCHAR(255) not null,
    first_name VARCHAR(255) not null,
    last_name VARCHAR(255) not null,
    primary key (id)
    );