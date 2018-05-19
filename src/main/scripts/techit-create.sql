create table hibernate_sequence (
    next_val bigint
);

insert into hibernate_sequence values ( 1000000 );

create table units (
    id          bigint primary key,
    name        varchar(255) not null unique,
    description varchar(8000),
    email       varchar(255),
    phone       varchar(255),
    location    varchar(255)
);

create table users (
    id          bigint primary key,
    type        varchar(255) default 'REGULAR',
    username    varchar(255) not null unique,
    hash        varchar(255) not null,
    enabled     boolean not null default true,
    first_name  varchar(255),
    last_name   varchar(255),
    email       varchar(255) not null unique,
    phone       varchar(255),
    department  varchar(255),
    unit_id     bigint references units(id)
);

create table unit_supervisors (
    unit_id         bigint not null references units(id),
    supervisor_id   bigint not null references users(id)
);

create table unit_technicians (
    unit_id         bigint not null references units(id),
    technician_id   bigint not null references users(id)
);

create table tickets (
    id                      bigint primary key,
    created_by              bigint not null references users(id),
    created_for_name        varchar(255),
    created_for_email       varchar(255) not null,
    created_for_phone       varchar(255),
    created_for_department  varchar(255),
    subject                 varchar(255) not null,
    details                 varchar(8000),
    location                varchar(255),
    unit_id                 bigint not null references units(id),
    priority                varchar(255) default 'MEDIUM',
    status                  varchar(255) default 'OPEN',
    date_created            datetime default now(),
    date_assigned           datetime,
    date_updated            datetime,
    date_closed             datetime
);

create table ticket_technicians (
    ticket_id       bigint not null references tickets(id),
    technician_id   bigint not null references users(id)
);

create table updates (
    id              bigint primary key,
    ticket_id       bigint references tickets(id),
    technician_id   bigint references users(id),
    details         varchar(8000),
    date            datetime
);