-- V1

CREATE SEQUENCE users_id_seq START 1;

create table users
(
    id       integer default nextval('users_id_seq'::regclass) not null primary key,
    full_name     varchar(255)                                      not null,
    email    varchar(50)                                       not null,
    status   varchar(20)                                       not null,
    password varchar(500)                                      not null
);

CREATE UNIQUE INDEX ix_users_username on users (email);

CREATE SEQUENCE roles_id_seq START 1;

create table roles
(
    id          integer default nextval('roles_id_seq'::regclass) not null primary key,
    name        varchar(50)                                       not null,
    description varchar(250)
);

CREATE SEQUENCE users_roles_id_seq START 1;

create table users_roles
(
    id      integer default nextval('users_roles_id_seq'::regclass) not null primary key,
    user_id integer                                                 not null,
    rol_id  integer                                                 not null,
    constraint fk_user_roles_users foreign key (user_id) references users (id),
    constraint fk_user_roles_roles foreign key (rol_id) references roles (id)
);

CREATE INDEX ix_users_roles_user_id on users_roles (user_id);

insert into users(full_name, email, password, status)
values ('user', 'user', '$2a$10$DzJkAf225jy4f8s7xT3PHOKod/6mAlsbtx931iLN0KT8BuWtF/5Sy', 'ACTIVO');
insert into users(full_name, email, password, status)
values ('admin', 'admin', '$2a$10$dRd.ZlbgW5E8Qt8iomMXdePS1S/bvzTJoDzstgql8qieACI2z6zQG', 'ACTIVO');

insert into roles(name, description)
VALUES ('ROLE_ADMIN', 'ADMIN');
insert into roles(name, description)
VALUES ('ROLE_CONSULTOR', 'CONSULTOR');

insert into users_roles(user_id, rol_id)
VALUES ((select id from users where email = 'user'), (select id from roles where name = 'ROLE_CONSULTOR'));

insert into users_roles(user_id, rol_id)
VALUES ((select id from users where email = 'admin'), (select id from roles where name = 'ROLE_CONSULTOR'));
insert into users_roles(user_id, rol_id)
VALUES ((select id from users where email = 'admin'), (select id from roles where name = 'ROLE_ADMIN'));
