DELETE FROM comments;
ALTER TABLE comments ALTER COLUMN id RESTART with 1;
DELETE FROM bookings;
ALTER TABLE bookings ALTER COLUMN id RESTART with 1;
DELETE FROM requests;
DELETE FROM items;
ALTER TABLE items ALTER COLUMN id RESTART with 1;
DELETE FROM users;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;

CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    email VARCHAR(30) NOT NULL,
    name VARCHAR(30) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    description VARCHAR(50) NOT NULL,
    requestor_id BIGINT NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requests_requestor_id FOREIGN KEY (requestor_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS items
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(300) NOT NULL,
    is_available BOOLEAN NOT NULL,
    owner_id BIGINT,
    request_id BIGINT,
    CONSTRAINT pk_items PRIMARY KEY (id),
    CONSTRAINT fk_items_owner_id FOREIGN KEY (owner_id) REFERENCES users (id),
    CONSTRAINT fk_items_request_id FOREIGN KEY (request_id) REFERENCES requests (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    item_id BIGINT NOT NULL,
    booker_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT pk_bookings PRIMARY KEY (id),
    CONSTRAINT fk_bookings_item_id FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_bookings_booker_id FOREIGN KEY (booker_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    text VARCHAR(1000) NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT fk_comments_item_id FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_comments_booker_id FOREIGN KEY (author_id) REFERENCES users (id)
);