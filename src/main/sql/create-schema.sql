create user expressions with password 'expressions';
create schema expressions;
grant all privileges on all tables in schema expressions to expressions;

set search_path to expressions;

create table functions (
    id varchar(100) not null,
    version varchar(10),
    primary key (id)
);

create table functions_versions (
    id varchar(100) not null,
    version varchar(10) not null,
    definition text,
    primary key (id, version),
    constraint functions_versions_fk1 foreign key (id) references functions(id),
    constraint functions_versions_uc unique (id, version)
);

alter table functions
    add constraint functions_fk1 foreign key (id, version) references functions_versions(id, version);

create table evaluations (
    function varchar(100) not null,
    version varchar(10) not null,
    id int,
    parameters json,
    primary key (function, id),
    constraint evaluations_fk1 foreign key (function, version) references functions_versions(id, version)
);

create or replace view evaluations_ids as
    select function, max(id) +1 as next_value from evaluations group by function