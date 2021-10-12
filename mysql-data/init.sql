CREATE DATABASE IF NOT EXISTS appDB;
CREATE PRODUCT IF NOT EXISTS 'products'@'%' IDENTIFIED BY 'password';
GRANT SELECT,UPDATE,INSERT ON appDB.* TO 'products'@'%';
FLUSH PRIVILEGES;

USE appDB;
CREATE TABLE IF NOT EXISTS products (
  ID INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  cost INTEGER(40) NOT NULL,
  PRIMARY KEY (ID)
);

INSERT INTO products (name, cost)
SELECT * FROM (SELECT 'Mishka', 1200) AS tmp
WHERE NOT EXISTS (
    SELECT name FROM products WHERE name = 'Mishka' AND cost = '1200'
) LIMIT 1;

INSERT INTO products (name, cost)
SELECT * FROM (SELECT 'Pokryshka', 2050) AS tmp
WHERE NOT EXISTS (
    SELECT name FROM products WHERE name = 'Pokryshka' AND cost = 2050
) LIMIT 1;
