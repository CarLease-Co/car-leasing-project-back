CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT NOT NULL,
                       name VARCHAR(255) NULL,
                       surname VARCHAR(255) NULL,
                       role SMALLINT NOT NULL,
                       CONSTRAINT pk_user PRIMARY KEY (user_id)
);

