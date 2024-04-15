CREATE TABLE applications
(
    application_id        BIGINT AUTO_INCREMENT NOT NULL,
    user_id               BIGINT NULL,
    monthly_income        DECIMAL(19, 4) NOT NULL,
    financial_obligations DECIMAL(19, 4) NOT NULL,
    car_id                INT            NOT NULL,
    loan_amount           DECIMAL(19, 4) NOT NULL,
    loan_duration         INT            NOT NULL CHECK (loan_duration BETWEEN 1 AND 120),
    free_text_explanation VARCHAR(255),
    is_submitted          BOOLEAN        NOT NULL,
    status                VARCHAR(255)   NOT NULL,
    start_date            date NULL,
    end_date              date NULL,
    CONSTRAINT pk_applications PRIMARY KEY (application_id)
);

ALTER TABLE applications
    ADD CONSTRAINT FK_APPLICATIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);
