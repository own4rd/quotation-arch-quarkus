CREATE TABLE proposal (
    id                      BIGSERIAL PRIMARY KEY,
    customer                VARCHAR(255) NOT NULL,
    price_tonne             NUMERIC(15, 2) NOT NULL,
    tonnes                  INTEGER NOT NULL,
    country                 VARCHAR(100) NOT NULL,
    proposal_validity_days  INTEGER NOT NULL,
    created                 TIMESTAMP NOT NULL
);
