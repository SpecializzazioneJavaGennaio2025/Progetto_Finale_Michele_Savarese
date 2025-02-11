insert into users (username, email, password, created_at)
values (
        'admin',
        'admin@aulab.it',
        '$2a$10$oMiUOq5ToRfUI/Zprg5nE.qt8nT9KKJZoDBu1SIWuj.UGx8aRHwxS',
        '20240607'
    );

    INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
    INSERT INTO roles (name) VALUES ('ROLE_REVISOR');
    INSERT INTO roles (name) VALUES ('ROLE_WRITER');
    INSERT INTO roles (name) VALUES ('ROLE_USER');

    INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);

    INSERT into categories (name) values ('politica'),('economia'),('food&drink'),('sport'),('intrattenimento'),('tech');