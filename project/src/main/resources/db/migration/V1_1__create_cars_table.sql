CREATE TABLE cars
(
    car_id           BIGINT AUTO_INCREMENT NOT NULL,
    make             VARCHAR(255) NULL,
    model            VARCHAR(255) NULL,
    manufacture_date INT NOT NULL,
    price_from       DECIMAL NULL,
    price_to         DECIMAL NULL,
    CONSTRAINT pk_cars PRIMARY KEY (car_id)
);