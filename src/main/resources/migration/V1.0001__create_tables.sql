
CREATE TABLE currencies (
                            currency_id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            code VARCHAR(10) NOT NULL,
                            symbol VARCHAR(10)
);

CREATE TABLE product_specifications (
                                        product_specification_id BIGSERIAL PRIMARY KEY,
                                        color VARCHAR(255),
                                        size VARCHAR(255),
                                        material VARCHAR(255),
                                        crt_at TIMESTAMP NOT NULL,
                                        upd_at TIMESTAMP
);

CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          price DECIMAL(19,2),
                          currency_id BIGINT,
                          product_specification_id BIGINT,
                          crt_at TIMESTAMP NOT NULL,
                          upd_at TIMESTAMP,
                          CONSTRAINT fk_currency FOREIGN KEY (currency_id) REFERENCES currencies (currency_id),
                          CONSTRAINT fk_product_specification FOREIGN KEY (product_specification_id) REFERENCES product_specifications (product_specification_id)
);
