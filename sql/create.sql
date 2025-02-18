-- ! CREATE
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100),
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50)
);
CREATE TABLE articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    subtitle VARCHAR(100),
    body TEXT,
    publish_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT,
    category_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
CREATE TABLE users_roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    role_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
create TABLE images(
    id BIGINT auto_increment PRIMARY KEY,
    path VARCHAR(255) NOT NULL,
    article_id BIGINT,
    foreign key(article_id) references articles(id)
);
CREATE TABLE career_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    body TEXT,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    role_id BIGINT,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    is_checked BOOLEAN
);
-- ! Insert
insert into users (username, email, password, created_at)
values (
        'admin',
        'admin@aulab.it',
        '$2a$10$oMiUOq5ToRfUI/Zprg5nE.qt8nT9KKJZoDBu1SIWuj.UGx8aRHwxS',
        '20240607'
    );
INSERT INTO roles (name)
VALUES ('ROLE_ADMIN');
INSERT INTO roles (name)
VALUES ('ROLE_REVISOR');
INSERT INTO roles (name)
VALUES ('ROLE_WRITER');
INSERT INTO roles (name)
VALUES ('ROLE_USER');
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1);
INSERT into categories (name)
values ('politica'),
    ('economia'),
    ('food&drink'),
    ('sport'),
    ('intrattenimento'),
    ('tech');