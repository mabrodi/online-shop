CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(20,2) NOT NULL,
    description TEXT NULL,
    creation_date TIMESTAMP NOT NULL
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE carts (
    id BIGSERIAL PRIMARY KEY,
    user_id int4 NOT NULL,
    product_id int4 NOT NULL,

    CONSTRAINT "user_id_foreign" FOREIGN KEY ("user_id") REFERENCES "public"."users"("id") ON DELETE CASCADE,
    CONSTRAINT "product_id_foreign" FOREIGN KEY ("product_id") REFERENCES "public"."products"("id") ON DELETE CASCADE
);

INSERT INTO "users" ("id", "email", "password", "name") VALUES
    (1, 'test@example.com', '-87-75-1950-1178655-38-120-3682104-110-37126-574-72-109-30-40499-1109120-44-4488-994120', 'Admin');

INSERT INTO "products" ("id", "name", "price", "creation_date", "description") VALUES
    (1, 'Apple', 1200.00, '2025-07-01 16:06:54.578422', 'Apple product');