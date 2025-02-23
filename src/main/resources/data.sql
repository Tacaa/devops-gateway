-- Lozinke su hesovane pomocu BCrypt algoritma https://www.dailycred.com/article/bcrypt-calculator
-- Lozinka za oba user-a je 123

-- USERS
INSERT INTO users (first_name, last_name, username, password, email, enabled, last_password_reset_date) VALUES
('John', 'Doe', 'johndoe', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'johndoe@example.com', true, '2017-10-01 21:58:58.508-07'),
('Jane', 'Smith', 'janesmith', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'janesmith@example.com', true, '2017-10-01 21:58:58.508-07'),
('Robert', 'Johnson', 'robertjohnson', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'robertjohnson@example.com', true, '2017-10-01 21:58:58.508-07'),
('Emily', 'Davis', 'emilydavis', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'emilydavis@example.com', true, '2017-10-01 21:58:58.508-07'),
('Michael', 'Brown', 'michaelbrown', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'michaelbrown@example.com', true, '2017-10-01 21:58:58.508-07'),
('Sarah', 'Miller', 'sarahmiller', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'sarahmiller@example.com', true, '2017-10-01 21:58:58.508-07'),
('David', 'Wilson', 'davidwilson', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'davidwilson@example.com', true, '2017-10-01 21:58:58.508-07'),
('Sophia', 'Moore', 'sophiamoore', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'sophiamoore@example.com', true, '2017-10-01 21:58:58.508-07'),
('Daniel', 'Taylor', 'danieltaylor', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'danieltaylor@example.com', true, '2017-10-01 21:58:58.508-07'),
('Olivia', 'Anderson', 'oliviaanderson', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'oliviaanderson@example.com', true, '2017-10-01 21:58:58.508-07'),
('James', 'Thomas', 'jamesthomas', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'jamesthomas@example.com', true, '2017-10-01 21:58:58.508-07'),
('Ella', 'Jackson', 'ellajackson', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'ellajackson@example.com', true, '2017-10-01 21:58:58.508-07'),
('William', 'White', 'williamwhite', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'williamwhite@example.com', true, '2017-10-01 21:58:58.508-07'),
('Ava', 'Harris', 'avaharris', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'avaharris@example.com', true, '2017-10-01 21:58:58.508-07'),
('Christopher', 'Martin', 'christophermartin', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'christophermartin@example.com', true, '2017-10-01 21:58:58.508-07'),
('Isabella', 'Thompson', 'isabellathompson', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'isabellathompson@example.com', true, '2017-10-01 21:58:58.508-07'),
('Joseph', 'Garcia', 'josephgarcia', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'josephgarcia@example.com',  true, '2017-10-01 21:58:58.508-07'),
('Mia', 'Martinez', 'miamartinez', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'miamartinez@example.com', true, '2017-10-01 21:58:58.508-07'),
('Alexander', 'Robinson', 'alexanderrobinson', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'alexanderrobinson@example.com', true, '2017-10-01 21:58:58.508-07'),
('Amelia', 'Clark', 'ameliaclarck', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'ameliaclarck@example.com', true, '2017-10-01 21:58:58.508-07'),
('Ethan', 'Rodriguez', 'ethanrodriguez', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'ethanrodriguez@example.com', true, '2017-10-01 21:58:58.508-07'),
('Charlotte', 'Lewis', 'charlottelewis', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'charlottelewis@example.com', true, '2017-10-01 21:58:58.508-07'),
('Matthew', 'Lee', 'matthewlee', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'matthewlee@example.com', true, '2017-10-01 21:58:58.508-07'),
('Harper', 'Walker', 'harperwalker', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'harperwalker@example.com', true, '2017-10-01 21:58:58.508-07'),
('Andrew', 'Hall', 'andrewhall', '$2a$10$X7F3hUST4aTxWHikNRY7gek7xsjW352cF51QmbRehvE9rGJooAMhW', 'andrewhall@example.com', true, '2017-10-01 21:58:58.508-07');


-- ROLES
INSERT INTO ROLE (name) VALUES ('ROLE_GUEST');
INSERT INTO ROLE (name) VALUES ('ROLE_HOST');


-- USERS_ROLES
INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (3, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (4, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (5, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (6, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (7, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (8, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (9, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (10, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (11, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (12, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (13, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (14, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (15, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (16, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (17, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (18, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (19, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (20, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (21, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (22, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (23, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (24, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (25, 2);