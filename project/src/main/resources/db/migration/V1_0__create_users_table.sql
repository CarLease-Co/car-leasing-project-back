CREATE TABLE users
(
    user_id  BIGINT AUTO_INCREMENT NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NULL,
    surname  VARCHAR(255) NULL,
    role     SMALLINT     NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

