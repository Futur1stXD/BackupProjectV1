create sequence users_seq;

create table users
(
    id         bigint                                          not null,
    email      varchar(255)                                    not null
        constraint users_pk
            primary key,
    first_name varchar(100)                                    not null,
    last_name  varchar(100)                                    not null,
    password   varchar(255)                                    not null,
    role       varchar(25)                                     not null,
    status     varchar(25) default 'ACTIVE'::character varying not null
);

create table file_store
(
    id        varchar not null,
    file_name varchar not null,
    file_type varchar not null,
    data      bigint  not null,
    email     varchar not null
);

create table response_file
(
    id           varchar not null,
    email        varchar not null,
    file_name    varchar not null,
    download_url varchar not null,
    file_type    varchar not null,
    file_size    bigint  not null
);
