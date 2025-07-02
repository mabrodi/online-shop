CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    description TEXT NULL,
    creation_date TIMESTAMP NOT NULL
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

INSERT INTO "users" ("id", "email", "password", "name") VALUES
    (1, 'test@example.com', '12341234', 'Admin');

INSERT INTO "products" ("id", "name", "price", "creation_date", "description") VALUES
    (1, 'Apple', 1200.00, '2025-07-01 16:06:54.578422', 'Apple product');