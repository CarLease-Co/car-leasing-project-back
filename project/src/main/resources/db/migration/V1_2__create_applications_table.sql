CREATE TABLE applications
(
    application_id        BIGINT AUTO_INCREMENT NOT NULL,
    user_id               BIGINT NULL,
    monthly_income        DECIMAL      NOT NULL,
    financial_obligations DECIMAL      NOT NULL,
    car_id                BIGINT NULL,
    free_text_explanation VARCHAR(255) NULL,
    is_submitted          BOOLEAN      NOT NULL,
    status                VARCHAR(255) NOT NULL,
    start_date            date NULL,
    end_date              date NULL,
    CONSTRAINT pk_applications PRIMARY KEY (application_id)
);

ALTER TABLE applications
    ADD CONSTRAINT FK_APPLICATIONS_ON_CAR FOREIGN KEY (car_id) REFERENCES cars (car_id);

ALTER TABLE applications
    ADD CONSTRAINT FK_APPLICATIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);