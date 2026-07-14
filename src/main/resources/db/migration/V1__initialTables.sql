CREATE TABLE card_request (
    id         UUID        NOT NULL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    oib        VARCHAR(13) NOT NULL,
    status     VARCHAR(50) NOT NULL
);

CREATE INDEX idx_card_request_oib ON card_request (oib);