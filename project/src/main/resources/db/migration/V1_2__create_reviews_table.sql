CREATE TABLE reviews
(
    review_id      BIGINT AUTO_INCREMENT NOT NULL,
    application_id BIGINT       NOT NULL,
    reviewer_id    BIGINT       NULL,
    review_status  VARCHAR(255) NOT NULL,
    review_date    DATE NULL,
    CONSTRAINT pk_reviews PRIMARY KEY (review_id),
    CONSTRAINT fk_reviews_application FOREIGN KEY (application_id) REFERENCES applications (application_id),
    CONSTRAINT fk_reviews_user FOREIGN KEY (reviewer_id) REFERENCES users (user_id)
);



