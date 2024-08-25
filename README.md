# Club_Management_System
Using Core Java and SQL connectivity

# Case Study 1: 

People's Club:

This application will maintain the details of all club members who have registered for the membership. Based on membership type, club member will be able to avail various facilities in the club.

Membership type can be Regular Members, Premium members and Gold members. Validity of the membership will be based on the type of membership. A person can register for the membership and can update their details. On registration, a membership id will be generated for the person who have successfully registered for the club. Admin can manage types of memberships such as adding new membership schemes, modify existing schemes and remove any membership schemes.

A member can also view the expiry date of membership and can also check the number of days or month left.

Design the database schema for the above case study and integrate it with the application.

# Table schema

set lines 256 
set trimout on 
set tab off 
set pagesize 100 
set colsep " | " 


create sequence member_seq
start with 101
increment by 1;

create sequence membership_seq
start with 1
increment by 1;

create sequence admin_seq
start with 1
increment by 1;


create table membership_types (
    membership_id int primary key,
    membership_name varchar2(50) not null,
    validity_months int not null,
    benefits CLOB
);

create table admin (
    admin_id int primary key,
    username varchar2(50) unique not null,
    password varchar2(255) not null
);

create table members (
    member_id int auto_increment primary key,
    first_name varchar2(20) not null,
    last_name varchar2(20) not null,
    email varchar2(50) not null unique,
    phone varchar2(10),
    membership_id int not null,
    registration_date date not null,
    expiry_date date,
    password varchar2(255) not null,
    foreign key (membership_id) references membership_types(membership_id)
);

///////

SQL*Plus: Release 10.2.0.1.0 - Production on Sun Aug 25 00:02:19 2024

Copyright (c) 1982, 2005, Oracle.  All rights reserved.

SQL> conn system
Enter password:
Connected.
SQL> create table membership_types (
  2      membership_id int auto_increment primary key,
  3      membership_name varchar2(50) not null,
  4      validity_months int not null,
  5      benefits text
  6  );
    membership_id int auto_increment primary key,
                      *
ERROR at line 2:
ORA-00907: missing right parenthesis


SQL> create sequence member_seq
  2  start with 101
  3  increment by 1;

Sequence created.

SQL> create sequence membership_seq
  2  start with 1
  3  increment by 1;

Sequence created.

SQL> create table membership_types (
  2      membership_id int primary key,
  3      membership_name varchar2(50) not null,
  4      validity_months int not null,
  5      benefits text
  6  );
    benefits text
             *
ERROR at line 5:
ORA-00902: invalid datatype


SQL> create table membership_types (
  2      membership_id int primary key,
  3      membership_name varchar2(50) not null,
  4      validity_months int not null,
  5      benefits CLOB
  6  );

Table created.

SQL> create sequence admin_seq
  2  start with 1
  3  increment by 1;

Sequence created.

SQL> create table admin (
  2      admin_id int primary key,
  3      username varchar2(50) unique not null,
  4      password varchar2(255) not null
  5  );

Table created.

SQL> create table members (
  2      member_id int primary key,
  3      first_name varchar2(20) not null,
  4      last_name varchar2(20) not null,
  5      email varchar2(50) not null unique,
  6      phone varchar2(10),
  7      membership_id int not null,
  8      registration_date date not null,
  9      expiry_date date,
 10      password varchar2(255) not null,
 11      foreign key (membership_id) references membership_types(membership_id)
 12  );

Table created.

SQL>
SELECT m.MEMBER_ID, m.FIRST_NAME, m.LAST_NAME, m.EMAIL, m.PHONE, m.REGISTRATION_DATE, m.EXPIRY_DATE, 
       s.MEMBERSHIP_NAME, s.VALIDITY_MONTHS
FROM members m
JOIN memberships s ON m.MEMBERSHIP_ID = s.MEMBERSHIP_ID;


SELECT m.MEMBER_ID, m.FIRST_NAME, m.LAST_NAME, m.EMAIL, m.PHONE, m.REGISTRATION_DATE, 
       m.EXPIRY_DATE, s.MEMBERSHIP_NAME, s.VALIDITY_MONTHS
FROM members m
JOIN membership_types s ON m.MEMBERSHIP_ID = s.MEMBERSHIP_ID
WHERE m.MEMBER_ID = 139;


8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918


