CREATE TABLE card_request (
    id         UUID        NOT NULL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    oib        VARCHAR(11) NOT NULL UNIQUE,
    status     VARCHAR(50) NOT NULL
);