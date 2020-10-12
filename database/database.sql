create database javaee_platzi;
use javaee_platzi;

create table teachers(
id int primary key auto_increment,
name varchar(250),
avatar varchar(250)
);

create table courses(
id int primary key auto_increment,
teacher_id int, 
name varchar (250), 
temary text, 
project varchar(250),
foreign key (teacher_id) references teachers(id)
);

create table socialNetworks(
id int primary key auto_increment,
name varchar(250),
icon varchar(250)
);

create table teacher_has_socialNetwork(
id int primary key auto_increment,
teacher_id int,
socialNetwork_id int,
nickname varchar(250),
foreign key (teacher_id) references teachers(id),
foreign key (socialNetwork_id) references socialNetworks(id)
);



