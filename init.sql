CREATE DATABASE IF NOT EXISTS db_curso_api;
GRANT ALL ON db_curso_api.* TO 'dev'@'%';

USE auth_db;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (name, email, password, role)
VALUES (
    'Juan Fernández',
    'admin@gmail.com',
    '$2a$10$OR6jnNehxOH/elIaDFHwOuCXGpMIL7Y8uxJKRbq9.2/i5eP2pRg6q',
    'ADMIN'
);