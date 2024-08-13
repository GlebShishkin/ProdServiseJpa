create table users (
    id int8 not null,
    username varchar(255) not null,
    primary key (id)
);

create table products (
    id int8 not null,
    userid int8,
    account varchar(255),
    saldo numeric,
    typ varchar(255), 
    primary key (id)
);

alter table if exists products
    add constraint fkey_user
    foreign key (userid) references users;
