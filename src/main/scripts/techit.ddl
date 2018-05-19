
    create table hibernate_sequence (
       next_val bigint
    ) engine=InnoDB;

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    create table ticket_technicians (
       ticket_id bigint not null,
        technician_id bigint not null
    ) engine=InnoDB;

    create table tickets (
       id bigint not null,
        created_for_department varchar(255),
        created_for_email varchar(255) not null,
        created_for_name varchar(255),
        created_for_phone varchar(255),
        date_assigned datetime,
        date_closed datetime,
        date_created datetime,
        date_updated datetime,
        details varchar(255),
        location varchar(255),
        priority varchar(255),
        status varchar(255),
        subject varchar(255) not null,
        created_by bigint not null,
        unit_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table unit_supervisors (
       unit_id bigint not null,
        supervisor_id bigint not null
    ) engine=InnoDB;

    create table unit_technicians (
       unit_id bigint not null,
        technician_id bigint not null
    ) engine=InnoDB;

    create table units (
       id bigint not null,
        description varchar(255),
        email varchar(255),
        location varchar(255),
        name varchar(255) not null,
        phone varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table updates (
       id bigint not null,
        date datetime,
        details varchar(255),
        technician_id bigint,
        ticket_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table users (
       id bigint not null,
        department varchar(255),
        email varchar(255) not null,
        enabled bit not null,
        first_name varchar(255),
        hash varchar(255) not null,
        last_name varchar(255),
        phone varchar(255),
        type varchar(255),
        username varchar(255) not null,
        unit_id bigint,
        primary key (id)
    ) engine=InnoDB;

    alter table units 
       add constraint UK_etw07nfppovq9p7ov8hcb38wy unique (name);

    alter table users 
       add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table users 
       add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);

    alter table ticket_technicians 
       add constraint FKkvckku7hvsd59vfejvy2rho2s 
       foreign key (technician_id) 
       references users (id);

    alter table ticket_technicians 
       add constraint FKawo4w3hcl79i748u1d9xn0ye7 
       foreign key (ticket_id) 
       references tickets (id);

    alter table tickets 
       add constraint FKsytxppnwol0ckyehli7bqqbcr 
       foreign key (created_by) 
       references users (id);

    alter table tickets 
       add constraint FKmj126vcy9uobxd6rfu269wjc2 
       foreign key (unit_id) 
       references units (id);

    alter table unit_supervisors 
       add constraint FKl7lw1muuolii27pcfnv5fvo81 
       foreign key (supervisor_id) 
       references users (id);

    alter table unit_supervisors 
       add constraint FKjpsao3qooc9oyqrlmy76u3h7i 
       foreign key (unit_id) 
       references units (id);

    alter table unit_technicians 
       add constraint FKsqktib8i4n9jmqcff7444f7jv 
       foreign key (technician_id) 
       references users (id);

    alter table unit_technicians 
       add constraint FK24sdr8oecffr1eluovip1vrr8 
       foreign key (unit_id) 
       references units (id);

    alter table updates 
       add constraint FKjy1na4sbsstci7ngavdiw6ats 
       foreign key (technician_id) 
       references users (id);

    alter table updates 
       add constraint FK3fnl74oyd1raon25v5lo3hyag 
       foreign key (ticket_id) 
       references tickets (id);

    alter table users 
       add constraint FKp2hfld4bhbwtakwrmt4xq6een 
       foreign key (unit_id) 
       references units (id);
