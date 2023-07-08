create database mpss;
use mpss;

create table invoice
(
    part_id int           null,
    d1      int default 0 not null,
    d2      int default 0 null,
    d3      int default 0 null,
    d4      int default 0 null,
    d5      int default 0 null,
    d6      int default 0 null,
    d7      int default 0 null
);

create table item
(
    part_id         int auto_increment,
    type            varchar(50) null,
    manufacturer_id int         null,
    vehicle_type    varchar(50) null,
    price           int         null,
    quantity        int         null,
    rack_name       varchar(50) null,
    constraint `inventory_item id_uindex`
        unique (part_id)
);

create table manufacturer
(
    manufacturer_id int auto_increment,
    name            varchar(50)  not null,
    address         varchar(400) null,
    phone_no        varchar(50)  null,
    constraint manufacturer_manufacturer_id_uindex
        unique (manufacturer_id)
);

create table owner
(
    username varchar(50) not null,
    password varchar(50) null
);

create table revenue
(
    date          date not null,
    total_revenue int  null
);

create table temp
(
    ctr int null
);


