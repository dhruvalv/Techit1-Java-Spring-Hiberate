insert into units (id, name) values (1, 'TechOps');
insert into units (id, name) values (2, 'ITC');

-- All hash are bcrypt('abcd')
insert into users (id, type, username, hash, first_name, last_name, email) values
    (1, 'ADMIN', 'techit', '$2a$10$v2/oF1tdBlXxejoMszKW3eNp/j6x8CxSBURUnVj006PYjYq3isJjO',
        'System', 'Admin', 'techit@localhost.localdomain');
insert into users (id, type, username, hash, first_name, last_name, email, unit_id) values
    (2, 'SUPERVISOR', 'jsmith', '$2a$10$9PJIPq9PMYHd9L8kb66/Nuu7DDQqq29eOsVF1F8SnPZ2UfD6KC/ly',
        'John', 'Smith', 'jsmith@localhost.localdomain', 1);
insert into users (id, type, username, hash, first_name, last_name, email, unit_id) values
    (3, 'SUPERVISOR', 'stiger', '$2a$10$9PJIPq9PMYHd9L8kb66/Nuu7DDQqq29eOsVF1F8SnPZ2UfD6KC/ly',
        'Scott', 'Tiger', 'tscott@gmail.com', 2);
insert into users (id, type, username, hash, first_name, last_name, email, unit_id) values
    (4, 'TECHNICIAN', 'jjim', '$2a$10$Q8G5BtMC.C5oonZzvBS.0usxJ2fpccf.I46pw3IGi.zorntvTSYbK',
        'Jimmy', 'Jim', 'jjim@localhost.localdomain', 1);
insert into users (id, type, username, hash, first_name, last_name, email, unit_id) values
    (5, 'TECHNICIAN', 'blee', '$2a$10$d8lhouzPhxZ.nLCaqjh5gevTyA3tZDUMwuy2WAsWAm.i/ag/btcxe',
        'Bob', 'Lee', 'blee@localhost.localdomain', 1);
insert into users (id, type, username, hash, first_name, last_name, email, unit_id) values
    (6, 'TECHNICIAN', 'tshrey', '$2a$10$Q8G5BtMC.C5oonZzvBS.0usxJ2fpccf.I46pw3IGi.zorntvTSYbK',
        'Shrey', 'Tarsar', 'shreyt@gmail.com', 2);
insert into users (id, type, username, hash, first_name, last_name, email, unit_id) values
    (7, 'TECHNICIAN', 'trutu', '$2a$10$d8lhouzPhxZ.nLCaqjh5gevTyA3tZDUMwuy2WAsWAm.i/ag/btcxe',
        'Rutu', 'Tarsar', 'rutut@gmail.com', 2);
insert into users (id, type, username, hash, first_name, last_name, email, unit_id) values
    (8, 'REGULAR', 'dhruvalv', '$2a$10$d8lhouzPhxZ.nLCaqjh5gevTyA3tZDUMwuy2WAsWAm.i/ag/btcxe',
        'Dhruval', 'Variya', 'dhruvalv@gmai;.com', null);
insert into users (id, type, username, hash, first_name, last_name, email, unit_id) values
    (9, 'REGULAR', 'jayv', '$2a$10$d8lhouzPhxZ.nLCaqjh5gevTyA3tZDUMwuy2WAsWAm.i/ag/btcxe',
        'Jay', 'Variya', 'jayvariya@gmail.com', null);

insert into unit_supervisors (unit_id, supervisor_id) values (1, 2);
insert into unit_supervisors (unit_id, supervisor_id) values (2, 3);
insert into unit_technicians (unit_id, technician_id) values (1, 4);
insert into unit_technicians (unit_id, technician_id) values (1, 5);
insert into unit_technicians (unit_id, technician_id) values (2, 6);
insert into unit_technicians (unit_id, technician_id) values (2, 7);

insert into tickets (id, created_by, created_for_name, created_for_email, subject, details, unit_id) values
    (1, 8, 'Dhruval Variya', 'dhruvalv@gmai;.com', 'Projector Malfunction',
        'The projector is broken in room A220.', 1);
insert into tickets (id, created_by, created_for_name, created_for_email, subject, details, unit_id) values
    (2, 8, 'Dhruval Variya', 'dhruvalv@gmai;.com', 'Equipment for EE Senior Design Project',
        'One of the EE senior design projects needs some equipment.', 1);
insert into tickets (id, created_by, created_for_name, created_for_email, subject, details, unit_id) values
    (3, 9, 'Jay Variya', 'jayvariya@gmail.com', 'Cant install Java',
        'Asking for permission while installing java', 2);
insert into tickets (id, created_by, created_for_name, created_for_email, subject, details, unit_id) values
    (4, 9, 'Jay Variya', 'jayvariya@gmail.com', 'PC restarts intermittently',
        'PC restarts after Installing Anti Virus.', 2);		

insert into ticket_technicians (ticket_id, technician_id) values (1, 4);
insert into ticket_technicians (ticket_id, technician_id) values (2, 5);
insert into ticket_technicians (ticket_id, technician_id) values (3, 6);
insert into ticket_technicians (ticket_id, technician_id) values (4, 7);

