CREATE TABLE accumulated_points
(
    transaction_id           TEXT                        NOT NULL UNIQUE,
    transaction_timestamp    TIMESTAMP WITH TIME ZONE NOT NULL,
    customer_id              TEXT                        NOT NULL,
    points                   NUMERIC                     NOT NULL,
    tier_validity_date       TIMESTAMP WITH TIME ZONE NOT NULL,
    redemption_validity_date TIMESTAMP WITH TIME ZONE NOT NULL,
    description              TEXT,
    PRIMARY KEY (transaction_id)
);
