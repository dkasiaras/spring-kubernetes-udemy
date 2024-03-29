drop table if exists customers;
create table if not exists customers (
    customer_id int auto_increment primary key,
    name varchar(100) not null,
    email varchar(100) not null,
    mobile_number varchar(100) not null,
    created_at date not null,
    created_by varchar(100) not null,
    updated_at date default null,
    updated_by varchar(100) default null
);

drop table if exists accounts;
create table if not exists accounts (
    customer_id int not null,
    account_number int auto_increment primary key,
    communication_sw boolean,
    account_type text not null,
    branch_address text not null,
    created_at date not null,
    created_by text not null,
    updated_at text default null,
    updated_by text default null
)