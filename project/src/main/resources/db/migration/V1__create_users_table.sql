CREATE TABLE users (
  user_id BIGINT AUTO_INCREMENT NOT NULL,
   user_name VARCHAR(255) NULL,
   role SMALLINT NOT NULL,
   CONSTRAINT pk_user PRIMARY KEY (user_id)
);

INSERT INTO users (user_name, role) VALUES
 ('Tomas', 0),
 ('Edgaras',1),
 ('Mantas', 2),
 ('Ruta', 3),
 ('Vaiva', 4);
