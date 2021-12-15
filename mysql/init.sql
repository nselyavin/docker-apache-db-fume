CREATE DATABASE IF NOT EXISTS meetsapp;
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY '1324';
GRANT ALL ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;

USE meetsapp;
create table if not exists user (
    id bigint not null auto_increment,
    birth_day varchar(255),
    created_date datetime(6),
    email varchar(255) not null,
    image LONGBLOB,
    lastname varchar(255) not null,
    likes INTEGER default 0,
    name varchar(255) not null,
    password varchar(3000) not null,
    username varchar(255),
    primary key (id)
) engine = InnoDB;

INSERT INTO meetsapp.user (birth_day, created_date, email, lastname, name, password, username) 
VALUES ('2001-11-11', '2021-12-12 16:50:21.000000', 'nselyavin@inbox.ru', 'Sel', 'Ni', '$2a$10$PVkt.srJewgI/Lf0fjsQ6OZFPQythiXriJYFtj7hL7Vw.MrnVZiZ.', 'Fume');