
-- Inserarea în tabelul 'role'
INSERT INTO role (name) VALUES ('ADMIN');
INSERT INTO role (name) VALUES ('USER');

-- Inserarea utilizatorului în tabelul 'user'
INSERT INTO user(username, email, password, age, role_id) 
VALUES ('Alex', 'alexandru.com', '$2a$12$ChDeMO7MgsJwzDPVKTKhMeVaV35y3qrVZQhns2VgIzRaisDInizlq', 17, 2);
