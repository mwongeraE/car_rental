-- fireArt Database Schema
-- Version 2.0

-- ABTO3BIT Copyright (c) 2015-2017, MySQL version
-- All rights reserved.
-- Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS fireart_247;
CREATE SCHEMA fireart_247;
USE fireart_247;

-- [table objects]
-- /////////////////////////////////////////////////////////////////////////////////////////////////////////////

-- manufacture

CREATE TABLE manufacture (
  id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(32) NOT NULL,
  description VARCHAR(128) DEFAULT '',
  picture MEDIUMBLOB DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FULLTEXT KEY idx_name_description (name,description),
  KEY idx_name (name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- product

CREATE TABLE product (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  manufacture_id SMALLINT UNSIGNED NOT NULL,
  discount_code VARCHAR(8) DEFAULT NULL,
  name VARCHAR(32) NOT NULL,
  description VARCHAR(128) DEFAULT '',
  code VARCHAR(32) UNIQUE NOT NULL,
  price DECIMAL(6,2) NOT NULL DEFAULT 0.00,
  picture MEDIUMBLOB DEFAULT NULL,
  review_count INTEGER UNSIGNED DEFAULT 0,
  review_value INTEGER UNSIGNED DEFAULT 0,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY idx_unique_code (code),
  FULLTEXT KEY idx_name_description (name,description),
  KEY idx_name (name),
  KEY idx_description (description),
  KEY idx_price (price),
  -- not review indexes(in sql query calculate the final value)
  KEY idx_fk_manufacture_id (manufacture_id),
  CONSTRAINT `fk_product_manufacture` FOREIGN KEY (manufacture_id) REFERENCES manufacture (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_discount_code (discount_code),
  CONSTRAINT `fk_product_discount_code` FOREIGN KEY (discount_code) REFERENCES discount (code) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- product_type

CREATE TABLE product_type (
  id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  parent_id SMALLINT UNSIGNED DEFAULT NULL,
  name VARCHAR(32) NOT NULL,
  description VARCHAR(128) DEFAULT '',
  language VARCHAR(32) DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FULLTEXT KEY idx_name_description (name,description),
  KEY idx_name (name),
  KEY idx_language (language),
  KEY idx_fk_parent_id (parent_id),
  CONSTRAINT `fk_product_type_parent` FOREIGN KEY (parent_id) REFERENCES product_type (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- product_param

CREATE TABLE product_param (
  id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  parent_id SMALLINT UNSIGNED DEFAULT NULL,
  name VARCHAR(32) NOT NULL,
  value VARCHAR(64) DEFAULT NULL,
  picture MEDIUMBLOB DEFAULT NULL,
  language VARCHAR(32) DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FULLTEXT KEY idx_name_value (name,value),
  KEY idx_name (name),
  KEY idx_language (language),
  KEY idx_fk_parent_id (parent_id),
  CONSTRAINT `fk_product_param_parent` FOREIGN KEY (parent_id) REFERENCES product_param (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- product_type_part

CREATE TABLE product_type_part (
  product_id INTEGER UNSIGNED NOT NULL,
  product_type_id SMALLINT UNSIGNED NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (product_id,product_type_id),
  KEY idx_fk_product_id (product_id),
  CONSTRAINT `fk_product_type_part_product` FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_product_type_id (product_type_id),
  CONSTRAINT `fk_product_type_part_product_type` FOREIGN KEY (product_type_id) REFERENCES product_type (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- product_param_part

CREATE TABLE product_param_part (
  product_id INTEGER UNSIGNED NOT NULL,
  product_param_id SMALLINT UNSIGNED NOT NULL,
  value VARCHAR(64) DEFAULT NULL,
  price DECIMAL(6,2) NOT NULL DEFAULT 0.00,
  picture MEDIUMBLOB DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (product_id,product_param_id),
  KEY idx_value (value),
  KEY idx_fk_product_id (product_id),
  CONSTRAINT `fk_product_param_part_product` FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_product_param_id (product_param_id),
  CONSTRAINT `fk_product_param_part_product_param` FOREIGN KEY (product_param_id) REFERENCES product_param (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- product_review

CREATE TABLE product_review (
  user_id INTEGER UNSIGNED NOT NULL,
  product_id INTEGER UNSIGNED NOT NULL,
  description VARCHAR(128) DEFAULT '',
  value TINYINT NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_fk_user_id (user_id),
  CONSTRAINT `fk_product_review_user` FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_product_id (product_id),
  CONSTRAINT `fk_product_review_product` FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- product_circle

CREATE TABLE product_circle (
  productA_id INTEGER UNSIGNED NOT NULL,
  productB_id INTEGER UNSIGNED NOT NULL,
  create_date DATETIME DEFAULT NULL,
  PRIMARY KEY (productA_id,productB_id),
  KEY idx_fk_productA_id (productA_id),
  CONSTRAINT `fk_product_circle_productA` FOREIGN KEY (productA_id) REFERENCES product (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_productB_id (productB_id),
  CONSTRAINT `fk_product_circle_productB` FOREIGN KEY (productB_id) REFERENCES product (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- delivery (shipping location for user by transport in purchase, stock_invoice)

CREATE TABLE delivery (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INTEGER UNSIGNED NOT NULL,
  transport_id MEDIUMINT UNSIGNED DEFAULT NULL,
  latitude DOUBLE DEFAULT NULL,
  longitude DOUBLE DEFAULT NULL,
  address VARCHAR(128) DEFAULT NULL,
  create_date DATETIME DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_address (address),
  KEY idx_create_date (create_date),
  KEY idx_fk_user_id (user_id),
  CONSTRAINT `fk_delivery_user` FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_transport_id (transport_id),
  CONSTRAINT `fk_delivery_transport` FOREIGN KEY (transport_id) REFERENCES transport (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- delivery_type

CREATE TABLE delivery_type (
  id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(32) NOT NULL,
  language VARCHAR(32) DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_language (language)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- transport (for delivery)

CREATE TABLE transport (
  id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  sensor_id MEDIUMINT UNSIGNED DEFAULT NULL,
  name VARCHAR(128) NOT NULL,
  color VARCHAR(32) DEFAULT NULL,
  license_plate VARCHAR(12) DEFAULT NULL,
  picture MEDIUMBLOB DEFAULT NULL,
  review_count INTEGER UNSIGNED DEFAULT 0,
  review_value INTEGER UNSIGNED DEFAULT 0,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY idx_sensor_id (sensor_id), -- WARNING! ONE TRANSPORT FOR ONE SENSOR
  KEY idx_name (name),
  KEY idx_license_plate (license_plate),
  -- not review indexes(in sql query calculate the final value)
  KEY idx_fk_sensor_id (sensor_id),
  CONSTRAINT `fk_transport_sensor` FOREIGN KEY (sensor_id) REFERENCES sensor (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- transport_type

CREATE TABLE transport_type (
  id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  parent_id SMALLINT UNSIGNED DEFAULT NULL,
  name VARCHAR(32) NOT NULL,
  description VARCHAR(128) DEFAULT '',
  language VARCHAR(32) DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FULLTEXT KEY idx_name_description (name,description),
  KEY idx_name (name),
  KEY idx_language (language),
  KEY idx_fk_parent_id (parent_id),
  CONSTRAINT `fk_transport_type_parent` FOREIGN KEY (parent_id) REFERENCES transport_type (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- transport_type_part

CREATE TABLE transport_type_part (
  transport_id MEDIUMINT UNSIGNED NOT NULL,
  transport_type_id SMALLINT UNSIGNED NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (transport_id,transport_type_id),
  KEY idx_fk_transport_id (transport_id),
  CONSTRAINT `fk_transport_type_part_transport` FOREIGN KEY (transport_id) REFERENCES transport (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_transport_type_id (transport_type_id),
  CONSTRAINT `fk_transport_type_part_transport_type` FOREIGN KEY (transport_type_id) REFERENCES transport_type (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- transport_product_part

CREATE TABLE transport_product_part (
  transport_id MEDIUMINT UNSIGNED NOT NULL,
  product_id INTEGER UNSIGNED NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (transport_id,product_id),
  KEY idx_fk_transport_id (transport_id),
  CONSTRAINT `fk_transport_product_part_transport` FOREIGN KEY (transport_id) REFERENCES transport (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_product_id (product_id),
  CONSTRAINT `fk_transport_product_part_product` FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- transport_review

CREATE TABLE transport_review (
  order_id INTEGER UNSIGNED NOT NULL,
  user_id INTEGER UNSIGNED NOT NULL,
  transport_id MEDIUMINT UNSIGNED NOT NULL,
  description VARCHAR(128) DEFAULT '',
  value TINYINT NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_fk_order_id (order_id),
  CONSTRAINT `fk_transport_review_order` FOREIGN KEY (order_id) REFERENCES order_AB (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_user_id (user_id),
  CONSTRAINT `fk_transport_review_user` FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_transport_id (transport_id),
  CONSTRAINT `fk_transport_review_transport` FOREIGN KEY (transport_id) REFERENCES transport (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- order_status [>0 order,shipping|<0 cancellation]

CREATE TABLE order_status (
  id SMALLINT NOT NULL,
  name VARCHAR(32) NOT NULL,
  language VARCHAR(32) DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_language (language)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- order_X (with deliveries from/to X points)

-- pickup_status [>0 pickuping|<0 cancellation]

CREATE TABLE pickup_status (
  id SMALLINT NOT NULL,
  name VARCHAR(32) NOT NULL,
  language VARCHAR(32) DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_language (language)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- order_AB (with delivery from A to B)

CREATE TABLE order_AB (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INTEGER UNSIGNED NOT NULL,
  transport_id MEDIUMINT UNSIGNED DEFAULT NULL,
  order_status_id SMALLINT NOT NULL DEFAULT 1,
  total_price DECIMAL(6,2) NOT NULL DEFAULT 0.00,
  route_distance INTEGER UNSIGNED DEFAULT NULL,
  route_duration INTEGER UNSIGNED DEFAULT NULL,
  route_data BLOB DEFAULT NULL,
  order_lat DOUBLE DEFAULT NULL,
  order_lon DOUBLE DEFAULT NULL,
  order_address VARCHAR(128) DEFAULT NULL,
  delivery_lat DOUBLE DEFAULT NULL,
  delivery_lon DOUBLE DEFAULT NULL,
  delivery_address VARCHAR(128) DEFAULT NULL,
  delivery_type_id SMALLINT UNSIGNED DEFAULT NULL,
  reserved_date DATETIME DEFAULT NULL,
  reserved_hours SMALLINT UNSIGNED DEFAULT NULL,
  create_date DATETIME DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_order_address (order_address),
  KEY idx_delivery_address (delivery_address),
  KEY idx_create_date (create_date),
  KEY idx_fk_user_id (user_id),
  CONSTRAINT `fk_order_AB_user` FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_transport_id (transport_id),
  CONSTRAINT `fk_order_AB_transport` FOREIGN KEY (transport_id) REFERENCES transport (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_order_status_id (order_status_id),
  CONSTRAINT `fk_order_AB_order_status` FOREIGN KEY (order_status_id) REFERENCES order_status (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_delivery_type_id (delivery_type_id),
  CONSTRAINT `fk_order_AB_delivery_type` FOREIGN KEY (delivery_type_id) REFERENCES delivery_type (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- order_AB_product_part (product part of order_AB)

CREATE TABLE order_AB_product_part (
  order_id INTEGER UNSIGNED NOT NULL,
  product_id INTEGER UNSIGNED NOT NULL,
  count INTEGER UNSIGNED NOT NULL,
  price DECIMAL(6,2) NOT NULL DEFAULT 0.00,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (order_id,product_id),
  KEY idx_fk_order_id (order_id),
  CONSTRAINT `fk_order_AB_product_part_order` FOREIGN KEY (order_id) REFERENCES order_AB (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_product_id (product_id),
  CONSTRAINT `fk_order_AB_product_part_product` FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- order_AB_product_param_part

CREATE TABLE order_AB_product_param_part (
  order_id INTEGER UNSIGNED NOT NULL,
  product_id INTEGER UNSIGNED NOT NULL,
  product_param_id SMALLINT UNSIGNED NOT NULL,
  count INTEGER UNSIGNED NOT NULL,
  price DECIMAL(6,2) NOT NULL DEFAULT 0.00,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (order_id,product_id,product_param_id),
  KEY idx_fk_order_id (order_id),
  CONSTRAINT `fk_order_AB_product_param_part_order` FOREIGN KEY (order_id) REFERENCES order_AB (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_product_id (product_id),
  CONSTRAINT `fk_order_AB_product_param_part_product` FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_product_param_id (product_param_id),
  CONSTRAINT `fk_order_AB_product_param_part_product_param` FOREIGN KEY (product_param_id) REFERENCES product_param (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- order_AB_user (user part of order_AB)

CREATE TABLE order_AB_user_part (
  order_id INTEGER UNSIGNED NOT NULL,
  user_id INTEGER UNSIGNED NOT NULL,
  product_param_id_1 INTEGER DEFAULT NULL, /*can be product_param_id as 1 occupied from order_AB_product_param_part*/
  product_param_id_2 INTEGER DEFAULT NULL, /*can be product_param_id as 2 occupied from order_AB_product_param_part*/
  pickup_status_id SMALLINT NOT NULL DEFAULT 1,
  pickup_lat DOUBLE DEFAULT NULL,
  pickup_lon DOUBLE DEFAULT NULL,
  pickup_address VARCHAR(128) DEFAULT NULL,
  pickup_date DATETIME DEFAULT NULL,
  pickup_info VARCHAR(128) DEFAULT '',
  price DECIMAL(6,2) NOT NULL DEFAULT 0.00,
  create_date DATETIME DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (order_id,user_id),
  KEY idx_fk_order_id (order_id),
  CONSTRAINT `fk_order_AB_user_part_order` FOREIGN KEY (order_id) REFERENCES order_AB (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_user_id (user_id),
  CONSTRAINT `fk_order_AB_user_part_user` FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE
  /*not indexed product_param_N*/
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- order_job (job from customer_user to worker_user)

CREATE TABLE order_job (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  customer_user_id INTEGER UNSIGNED NOT NULL,
  worker_user_id INTEGER UNSIGNED DEFAULT NULL,
  order_status_id SMALLINT NOT NULL DEFAULT 1,
  total_price DECIMAL(6,2) NOT NULL DEFAULT 0.00,  
  order_lat DOUBLE DEFAULT NULL,
  order_lon DOUBLE DEFAULT NULL,
  order_address VARCHAR(128) DEFAULT NULL,
  product_id INTEGER UNSIGNED DEFAULT NULL,
  reserved_date DATETIME DEFAULT NULL,
  reserved_hours SMALLINT UNSIGNED DEFAULT NULL,
  create_date DATETIME DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_job_address (order_address),
  KEY idx_create_date (create_date),
  KEY idx_fk_customer_user_id (customer_user_id),
  CONSTRAINT `fk_order_job_customer_user` FOREIGN KEY (customer_user_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_worker_user_id (worker_user_id),
  CONSTRAINT `fk_order_job_worker_user` FOREIGN KEY (worker_user_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_order_status_id (order_status_id),
  CONSTRAINT `fk_order_job_order_status` FOREIGN KEY (order_status_id) REFERENCES order_status (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_product_id (product_id),
  CONSTRAINT `fk_order_job_product` FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- store

CREATE TABLE store (
  id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL,
  description VARCHAR(128) DEFAULT '',
  picture MEDIUMBLOB DEFAULT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- store_part

CREATE TABLE store_part (
  id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  store_id SMALLINT UNSIGNED NOT NULL,
  name VARCHAR(32) NOT NULL,
  description VARCHAR(128) DEFAULT '',
  picture MEDIUMBLOB DEFAULT NULL,
  latitude DOUBLE DEFAULT NULL,
  longitude DOUBLE DEFAULT NULL,
  email VARCHAR(64) DEFAULT NULL,
  phone1 VARCHAR(20) DEFAULT NULL,
  phone2 VARCHAR(20) DEFAULT NULL,
  address VARCHAR(128) DEFAULT NULL,
  city VARCHAR(64) DEFAULT NULL,
  postcode VARCHAR(12) DEFAULT NULL,
  language VARCHAR(32) DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_name (name),
  KEY idx_language (language),
  KEY idx_fk_store_id (store_id),
  CONSTRAINT `fk_store_part_manufacture` FOREIGN KEY (store_id) REFERENCES store (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- stock

CREATE TABLE stock (
  product_id INTEGER UNSIGNED NOT NULL,
  store_id SMALLINT UNSIGNED DEFAULT NULL,
  count INTEGER UNSIGNED NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (product_id,store_id),
  KEY idx_fk_product_id (product_id),
  CONSTRAINT `fk_stock_product` FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_store_id (store_id),
  CONSTRAINT `fk_stock_store` FOREIGN KEY (store_id) REFERENCES store (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- stock_invoice

CREATE TABLE stock_invoice (
  id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  invoice_code VARCHAR(32) DEFAULT NULL,
  invoice_date DATETIME DEFAULT NULL,
  total_price DECIMAL(6,2) NOT NULL DEFAULT 0.00,
  total_tax DECIMAL(6,2) NOT NULL DEFAULT 0.00,
  supplier VARCHAR(32) DEFAULT NULL,
  phone VARCHAR(20) DEFAULT NULL,
  delivery_id INTEGER UNSIGNED DEFAULT NULL,
  delivery_code VARCHAR(32) DEFAULT NULL,
  delivery_type_id SMALLINT UNSIGNED DEFAULT NULL,
  delivery_date DATETIME DEFAULT NULL,
  delivery_price DECIMAL(6,2) DEFAULT NULL,
  payment_date DATETIME DEFAULT NULL,
  payment_info VARCHAR(32) DEFAULT '',
  payment_amount DECIMAL(6,2) DEFAULT NULL,
  create_date DATETIME DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_invoice_code (invoice_code),
  KEY idx_invoice_date (invoice_date),
  KEY idx_supplier (supplier),
  KEY idx_phone (phone),
  KEY idx_delivery_id (delivery_id),
  KEY idx_delivery_date (delivery_date),
  KEY idx_create_date (create_date),
  -- KEY idx_fk_delivery_id (delivery_id),
  -- CONSTRAINT `fk_stock_invoice_delivery` FOREIGN KEY (delivery_id) REFERENCES delivery (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_delivery_type_id (delivery_type_id),
  CONSTRAINT `fk_stock_invoice_delivery_type` FOREIGN KEY (delivery_type_id) REFERENCES delivery_type (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- stock_invoice_part

CREATE TABLE stock_invoice_part (
  stock_invoice_id MEDIUMINT UNSIGNED NOT NULL,
  product_id INTEGER UNSIGNED NOT NULL,
  tax_id SMALLINT UNSIGNED DEFAULT NULL,
  count INTEGER UNSIGNED NOT NULL,
  price DECIMAL(6,2) NOT NULL DEFAULT 0.00,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (stock_invoice_id,product_id),
  KEY idx_fk_stock_invoice_id (stock_invoice_id),
  CONSTRAINT `fk_stock_invoice_part_stock_invoice` FOREIGN KEY (stock_invoice_id) REFERENCES stock_invoice (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_product_id (product_id),
  CONSTRAINT `fk_stock_invoice_part_product` FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- currency

CREATE TABLE currency (
  id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(3) NOT NULL,
  description VARCHAR(128) DEFAULT '',
  value DECIMAL(6,2) NOT NULL DEFAULT 1.00,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  language VARCHAR(32) DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_name(name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- language

CREATE TABLE language (
  id SMALLINT UNSIGNED NOT NULL,
  name VARCHAR(32) NOT NULL,
  value VARCHAR(32) NOT NULL,
  code VARCHAR(5) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  UNIQUE KEY idx_unique_name (name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- color

CREATE TABLE color (
  name VARCHAR(32) NOT NULL,
  UNIQUE KEY idx_unique_name (name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- attr

CREATE TABLE attr (
  name VARCHAR(32) NOT NULL,
  UNIQUE KEY idx_unique_name (name)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- attr_part

CREATE TABLE attr_part (
  id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  object_id INTEGER NOT NULL,
  object_name VARCHAR(64) NOT NULL,
  name VARCHAR(32) NOT NULL,
  value VARCHAR(128) DEFAULT NULL,
  language VARCHAR(32) DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_object_id (object_id),
  KEY idx_object_name (object_name),
  KEY idx_name (name),
  KEY idx_language (language)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- discount (discount_type: [1-discount in percent, 2-discount in value add, 3-increase in percent 4-increase in value])

CREATE TABLE discount (
  id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  product_type_id SMALLINT UNSIGNED DEFAULT NULL,
  type TINYINT UNSIGNED NOT NULL DEFAULT 1,
  code VARCHAR(8) NOT NULL,
  name VARCHAR(32) NOT NULL,
  value SMALLINT UNSIGNED NOT NULL,
  language VARCHAR(32) DEFAULT NULL,
  start_date DATETIME DEFAULT NULL,
  finish_date DATETIME DEFAULT NULL,
  create_date DATETIME DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_code (code),
  KEY idx_name (name),
  KEY idx_language (language),
  KEY idx_fk_product_type_id (product_type_id),
  CONSTRAINT `fk_discount_product_type` FOREIGN KEY (product_type_id) REFERENCES product_type (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- tax

CREATE TABLE tax (
  id SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  code VARCHAR(32) NOT NULL,
  name VARCHAR(32) NOT NULL,
  value DECIMAL(6,2) NOT NULL,
  language VARCHAR(32) DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY idx_unique_code (code),
  KEY idx_name (name),
  KEY idx_language (language)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- purchase (order_id,delivery_id nullable after order,delivery completed and removed)
-- (order_id is a order_AB)

CREATE TABLE purchase (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INTEGER UNSIGNED NOT NULL,
  invoice_code VARCHAR(32) DEFAULT NULL,
  invoice_date DATETIME DEFAULT NULL,
  total_price DECIMAL(6,2) NOT NULL DEFAULT 0.00,
  total_tax DECIMAL(6,2) NOT NULL DEFAULT 0.00,
  order_id INTEGER UNSIGNED DEFAULT NULL,
  order_date DATETIME DEFAULT NULL,
  order_info VARCHAR(128) DEFAULT '',
  delivery_id INTEGER UNSIGNED DEFAULT NULL,
  delivery_code VARCHAR(32) DEFAULT NULL,
  delivery_type_id SMALLINT UNSIGNED DEFAULT NULL,
  delivery_date DATETIME DEFAULT NULL,
  delivery_price DECIMAL(6,2) DEFAULT NULL,
  payment_date DATETIME DEFAULT NULL,
  payment_info VARCHAR(128) DEFAULT '',
  payment_amount DECIMAL(6,2) DEFAULT NULL,
  create_date DATETIME DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_user_id (user_id),
  KEY idx_invoice_code (invoice_code),
  KEY idx_invoice_date (invoice_date),
  KEY idx_order_id (order_id),
  KEY idx_order_date (order_date),
  KEY idx_delivery_id (delivery_id),
  KEY idx_delivery_date (delivery_date),
  KEY idx_payment_date (payment_date),
  KEY idx_create_date (create_date),
  KEY idx_fk_user_id (user_id),
  CONSTRAINT `fk_purchase_user` FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  -- KEY idx_fk_order_id (order_id),
  -- CONSTRAINT `fk_purchase_order` FOREIGN KEY (order_id) REFERENCES order_AB (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  -- KEY idx_fk_delivery_id (delivery_id),
  -- CONSTRAINT `fk_purchase_delivery` FOREIGN KEY (delivery_id) REFERENCES delivery (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_delivery_type_id (delivery_type_id),
  CONSTRAINT `fk_purchase_delivery_type` FOREIGN KEY (delivery_type_id) REFERENCES delivery_type (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- payment (with credit card)

CREATE TABLE payment (
  purchase_id INTEGER UNSIGNED NOT NULL,
  amount DECIMAL(6,2) DEFAULT 0.00,
  currency VARCHAR(3) DEFAULT NULL,
  description VARCHAR(128) DEFAULT '',
  status VARCHAR(20) DEFAULT NULL,
  transaction_id VARCHAR(128) DEFAULT NULL,
  phone VARCHAR(20) DEFAULT NULL,
  create_date DATETIME DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_transaction_id (transaction_id),
  KEY idx_create_date (create_date),
  KEY idx_fk_purchase_id (purchase_id),
  CONSTRAINT `fk_payment_purchase` FOREIGN KEY (purchase_id) REFERENCES purchase (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- user (user_type: [1-admin, 2-worker(seller), 3-client(customer)])

CREATE TABLE user (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  discount_code VARCHAR(8) DEFAULT NULL,
  type TINYINT UNSIGNED NOT NULL,
  first_name VARCHAR(32) NOT NULL,
  last_name VARCHAR(32) NOT NULL,
  call_name VARCHAR(32) DEFAULT NULL,
  email VARCHAR(64) DEFAULT NULL,
  phone VARCHAR(20) DEFAULT NULL,
  username VARCHAR(64) UNIQUE NOT NULL,
  password VARCHAR(16) DEFAULT '',
  picture MEDIUMBLOB DEFAULT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  token VARCHAR(64) DEFAULT NULL,
  prepaid_amount DECIMAL(6,2) DEFAULT 0.00,
  review_count INTEGER UNSIGNED DEFAULT 0,
  review_value INTEGER UNSIGNED DEFAULT 0,  
  create_date DATETIME DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY idx_unique_username (username),
  UNIQUE KEY idx_unique_call_name (call_name),
  UNIQUE KEY idx_unique_token (token),
  FULLTEXT KEY idx_first_name_last_name (first_name,last_name),
  FULLTEXT KEY idx_first_name (first_name),
  FULLTEXT KEY idx_last_name (last_name),
  KEY idx_email (email),
  KEY idx_phone (phone),
  KEY idx_active (active),
  KEY idx_create_date (create_date),
  KEY idx_fk_discount_code (discount_code),
  CONSTRAINT `fk_user_discount_code` FOREIGN KEY (discount_code) REFERENCES discount (code) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- user_review

CREATE TABLE user_review (
  user_id INTEGER UNSIGNED NOT NULL,  
  review_user_id INTEGER UNSIGNED NOT NULL,
  description VARCHAR(128) DEFAULT '',
  value TINYINT NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_fk_user_id (user_id),
  CONSTRAINT `fk_user_review_user` FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_review_user_id (user_id),
  CONSTRAINT `fk_user_review_review_user` FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- prepaid_card

CREATE TABLE prepaid_card (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  serial_number VARCHAR(64) DEFAULT NULL,
  prepaid_code VARCHAR(16) DEFAULT NULL,
  amount DECIMAL(6,2) DEFAULT 0.00,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  create_date DATETIME DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY idx_unique_serial_number (serial_number),
  UNIQUE KEY idx_unique_prepaid_code (prepaid_code),
  KEY idx_create_date (create_date)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- message (message_type: [1-input, 2-output])

CREATE TABLE message (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  type TINYINT UNSIGNED NOT NULL,
  userA_id INTEGER UNSIGNED NOT NULL,
  userB_id INTEGER UNSIGNED NOT NULL,
  message VARCHAR(256) NOT NULL,
  create_date DATETIME DEFAULT NULL,
  PRIMARY KEY (id),
  KEY idx_type (type),
  FULLTEXT KEY idx_message (message),
  KEY idx_create_date (create_date),
  KEY idx_fk_userA_id (userA_id),
  CONSTRAINT `fk_message_userA` FOREIGN KEY (userA_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_userB_id (userB_id),
  CONSTRAINT `fk_message_userB` FOREIGN KEY (userB_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- sensor

CREATE TABLE sensor (
  id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INTEGER UNSIGNED NOT NULL,
  name VARCHAR(32) NOT NULL,
  serial_number VARCHAR(64) DEFAULT NULL,
  device_name VARCHAR(64) DEFAULT NULL,
  phone VARCHAR(20) DEFAULT NULL,
  active BOOLEAN NOT NULL DEFAULT FALSE,
  create_date DATETIME DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_name (name),
  KEY idx_device_name (device_name),
  KEY idx_phone (phone),
  KEY idx_active (active),
  KEY idx_create_date (create_date),
  KEY idx_fk_user_id (user_id),
  CONSTRAINT `fk_sensor_user` FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- sensor_circle

CREATE TABLE sensor_circle (
  sensorA_id MEDIUMINT UNSIGNED NOT NULL,
  sensorB_id MEDIUMINT UNSIGNED NOT NULL,
  create_date DATETIME DEFAULT NULL,
  PRIMARY KEY (sensorA_id,sensorB_id),
  KEY idx_fk_sensorA_id (sensorA_id),
  CONSTRAINT `fk_sensor_circle_sensorA` FOREIGN KEY (sensorA_id) REFERENCES sensor (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_sensorB_id (sensorB_id),
  CONSTRAINT `fk_sensor_circle_sensorB` FOREIGN KEY (sensorB_id) REFERENCES sensor (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- sensor_group

CREATE TABLE sensor_group (
  id MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,
  sensor_id MEDIUMINT UNSIGNED NOT NULL,
  name VARCHAR(32) NOT NULL,
  start_date DATETIME NOT NULL,
  finish_date DATETIME NOT NULL,
  create_date DATETIME DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_name (name),
  KEY idx_create_date (create_date),
  KEY idx_fk_sensor_id (sensor_id),
  CONSTRAINT `fk_sensor_group_sensor` FOREIGN KEY (sensor_id) REFERENCES sensor (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- sensor_group_part

CREATE TABLE sensor_group_part (
  sensor_group_id MEDIUMINT UNSIGNED NOT NULL,
  sensor_id MEDIUMINT UNSIGNED NOT NULL,
  PRIMARY KEY (sensor_group_id,sensor_id),
  KEY idx_fk_sensor_group_id (sensor_group_id),
  CONSTRAINT `fk_sensor_group_part_sensor_group` FOREIGN KEY (sensor_group_id) REFERENCES sensor_group (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_sensor_id (sensor_id),
  CONSTRAINT `fk_sensor_group_part_sensor` FOREIGN KEY (sensor_id) REFERENCES sensor (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- sensor_place (geographic point latlng with name)

CREATE TABLE sensor_place (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  sensor_id MEDIUMINT UNSIGNED NOT NULL,
  latitude DOUBLE NOT NULL,
  longitude DOUBLE NOT NULL,
  name VARCHAR(32) NOT NULL,
  description VARCHAR(128) DEFAULT '',
  language VARCHAR(32) DEFAULT NULL,
  picture MEDIUMBLOB DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_name (name),
  KEY idx_language (language),
  KEY idx_fk_sensor_id (sensor_id),
  CONSTRAINT `fk_sensor_place_sensor` FOREIGN KEY (sensor_id) REFERENCES sensor (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- sensor_area (polygon, octagon, rectangle)

-- track_type

CREATE TABLE track_type (
  id SMALLINT NOT NULL,
  name VARCHAR(32) NOT NULL,
  language VARCHAR(32) DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- track (timezone_offset value in minutes from UTC)

CREATE TABLE track (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  sensor_id MEDIUMINT UNSIGNED NOT NULL,
  track_type_id SMALLINT NOT NULL DEFAULT 1,
  latitude DOUBLE NOT NULL,
  longitude DOUBLE NOT NULL,
  time INTEGER DEFAULT NULL,
  altitude DOUBLE DEFAULT NULL,
  accuracy DOUBLE DEFAULT NULL,
  bearing DOUBLE DEFAULT NULL,
  speed DOUBLE DEFAULT NULL,
  satellites TINYINT DEFAULT NULL,
  battery TINYINT DEFAULT NULL,
  timezone_offset SMALLINT DEFAULT NULL,
  create_date DATETIME DEFAULT NULL,
  PRIMARY KEY (id),
  KEY idx_sensor_id (sensor_id),
  KEY idx_time (time),
  KEY idx_create_date (create_date),
  KEY idx_fk_sensor_id (sensor_id),
  CONSTRAINT `fk_track_sensor` FOREIGN KEY (sensor_id) REFERENCES sensor (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  KEY idx_fk_track_type_id (track_type_id),
  CONSTRAINT `fk_track_type` FOREIGN KEY (track_type_id) REFERENCES track_type (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- track_part

CREATE TABLE track_part (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  track_id INTEGER UNSIGNED NOT NULL,
  name VARCHAR(32) NOT NULL,
  description VARCHAR(128) DEFAULT '',
  language VARCHAR(32) DEFAULT NULL,
  picture MEDIUMBLOB DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY idx_name (name),
  KEY idx_language (language),
  KEY idx_fk_track_id (track_id),
  CONSTRAINT `fk_track_part_track` FOREIGN KEY (track_id) REFERENCES track (id) ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- system

CREATE TABLE audit ( -- save important events !
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(48) NOT NULL,
  description VARCHAR(128) DEFAULT '',
  username VARCHAR(64) NOT NULL,
  message VARCHAR(256) DEFAULT NULL,  
  create_date DATETIME DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),  
  KEY idx_name (name),
  KEY idx_description (description),
  KEY idx_username (username)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE preferences ( -- who is the master of schema ?
  code VARCHAR(32) DEFAULT NULL,
  name VARCHAR(32) DEFAULT 'fireArt',
  description VARCHAR(128) DEFAULT 'Open source CRM. Good Ideas for Bussiness!',
  copyrigth VARCHAR(32) DEFAULT 'ABTO3BIT',
  picture MEDIUMBLOB DEFAULT NULL,
  audit BOOLEAN DEFAULT TRUE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE settings ( -- api keys, mail server's username/password
  code VARCHAR(32) NOT NULL,
  name VARCHAR(64) NOT NULL,
  value VARCHAR(256) DEFAULT NULL,
  UNIQUE KEY idx_unique_code (code)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE geocode ( -- addresses from apps find
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  geocode_address VARCHAR(128) NOT NULL,
  formatted_address VARCHAR(128) NOT NULL,
  latitude DOUBLE NOT NULL,
  longitude DOUBLE NOT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY idx_unique_geocode_address (geocode_address),
  KEY idx_formatted_address (formatted_address),
  KEY idx_latitude (latitude),
  KEY idx_longitude (longitude)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE project ( -- project similar fireArt, project's api_key bind to schema_name
  user_id INTEGER UNSIGNED DEFAULT NULL,
  api_key VARCHAR(64) NOT NULL,
  schema_name VARCHAR(64) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT FALSE,
  amount_paid DECIMAL(6,2) DEFAULT 0.00,
  currency VARCHAR(3) DEFAULT NULL,
  name VARCHAR(64) NOT NULL,
  description VARCHAR(128) DEFAULT '',
  ip_address VARCHAR(15) NOT NULL,
  create_date DATETIME DEFAULT NULL,
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY idx_unique_api_key (api_key),
  UNIQUE KEY idx_unique_schema_name (schema_name),
  KEY idx_user_id (user_id),
  KEY idx_name (name),
  KEY idx_ip_address (ip_address),
  KEY idx_create_date (create_date)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- [other objects]
-- /////////////////////////////////////////////////////////////////////////////////////////////////////////////

-- -------------------------------------------------------------------------------------------------------------
-- HARDCORE there (schema copy functions and procedures)
-- WARNING! Need to review bottom functions and procedures (parameters int and not int creation)

-- WARNING! sys functions and procedures start with `sql_`, `clone_`, `copy_` (not named other as sys)

DELIMITER $$

CREATE FUNCTION `sql_create_trigger`(
  p_old_schema_name TINYTEXT,
  p_new_schema_name TINYTEXT,
  p_trigger_name TINYTEXT) RETURNS TINYTEXT
BEGIN
  DECLARE v_sql_query TINYTEXT DEFAULT '';

  SELECT CONCAT('CREATE TRIGGER `',p_new_schema_name,'`.`',TRIGGER_NAME,'` ',ACTION_TIMING,' ',EVENT_MANIPULATION,' ON `',p_new_schema_name,'`.`',EVENT_OBJECT_TABLE,'` FOR EACH ',ACTION_ORIENTATION,' ',ACTION_STATEMENT)
  INTO v_sql_query
  FROM INFORMATION_SCHEMA.TRIGGERS WHERE TRIGGER_SCHEMA=p_old_schema_name AND TRIGGER_NAME=p_trigger_name;

  RETURN v_sql_query;  
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `sql_create_trigger_list`(
  p_old_schema_name TINYTEXT,
  p_new_schema_name TINYTEXT) RETURNS TEXT
BEGIN
  DECLARE v_ret_val TEXT DEFAULT '';

  DECLARE v_finished INTEGER DEFAULT 0;
  DECLARE v_sql_query TEXT DEFAULT '';

  DECLARE c_sql_query CURSOR FOR SELECT sql_create_trigger(p_old_schema_name,p_new_schema_name,TRIGGER_NAME)
  FROM INFORMATION_SCHEMA.TRIGGERS WHERE TRIGGER_SCHEMA=p_old_schema_name;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_sql_query;

  get_data: LOOP
    FETCH c_sql_query INTO v_sql_query;

    IF v_finished THEN LEAVE get_data; END IF;

    SET v_ret_val=CONCAT(v_ret_val,v_sql_query,';');

  END LOOP;

  CLOSE c_sql_query;
  
  RETURN v_ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE PROCEDURE `clone_tables`(
  p_old_schema_name TINYTEXT,
  p_new_schema_name TINYTEXT)
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;
  DECLARE v_sql_query TINYTEXT DEFAULT '';

  DECLARE c_sql_query CURSOR FOR SELECT CONCAT('CREATE TABLE `',p_new_schema_name,'`.`',TABLE_NAME,'` LIKE `',p_old_schema_name,'`.`',TABLE_NAME,'`')
  FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA=p_old_schema_name;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_sql_query;

  get_data: LOOP
    FETCH c_sql_query INTO v_sql_query;

    IF v_finished THEN LEAVE get_data; END IF;

    SET @str=v_sql_query;

    PREPARE stmt FROM @str;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END LOOP;

  CLOSE c_sql_query;
  
  -- sys proc
  SET @str=CONCAT('CREATE TABLE `',p_new_schema_name,'`.`temp_proc` LIKE mysql.proc');

  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;
  
END $$

DELIMITER ;

-- (clone routines from temp table to sys table)

DELIMITER $$

CREATE PROCEDURE `clone_proc`(
  p_schema_name TINYTEXT)
BEGIN
  DECLARE v_db char(64);
  DECLARE v_name char(64);
  DECLARE v_type TINYTEXT;
  DECLARE v_specific_name char(64);
  DECLARE v_language TINYTEXT;
  DECLARE v_sql_data_access TINYTEXT;
  DECLARE v_is_deterministic TINYTEXT;
  DECLARE v_security_type TINYTEXT;
  DECLARE v_param_list blob;
  DECLARE v_returns longblob;
  DECLARE v_body longblob;
  DECLARE v_definer char(77);
  DECLARE v_created timestamp;
  DECLARE v_modified timestamp;
  DECLARE v_sql_mode TINYTEXT;
  DECLARE v_comment text;
  DECLARE v_character_set_client char(32);
  DECLARE v_collation_connection char(32);
  DECLARE v_db_collation char(32);
  DECLARE v_body_utf8 longblob;

  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE c_sql_query CURSOR FOR SELECT * FROM temp_view; -- temp_proc;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  DROP VIEW IF EXISTS temp_view;

  SET @sql_create_view=CONCAT('CREATE VIEW temp_view AS SELECT * FROM ',p_schema_name,'.temp_proc');
  -- SELECT @sql_create_view;
  PREPARE stmt FROM @sql_create_view;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  OPEN c_sql_query;

  get_data: LOOP
    FETCH c_sql_query INTO 
    v_db,
    v_name,
    v_type,
    v_specific_name,
    v_language,
    v_sql_data_access,
    v_is_deterministic,
    v_security_type,
    v_param_list,
    v_returns,
    v_body,
    v_definer,
    v_created,
    v_modified,
    v_sql_mode,
    v_comment,
    v_character_set_client,
    v_collation_connection,
    v_db_collation,
    v_body_utf8;

    IF v_finished THEN LEAVE get_data; END IF;

    INSERT INTO mysql.proc(
    db,
    name,
    type,
    specific_name,
    language,
    sql_data_access,
    is_deterministic,
    security_type,
    param_list,
    returns,
    body,
    definer,
    created,
    modified,
    sql_mode,
    comment,
    character_set_client,
    collation_connection,
    db_collation,
    body_utf8    
    ) 
    VALUES(
    v_db,
    v_name,
    v_type,
    v_specific_name,
    v_language,
    v_sql_data_access,
    v_is_deterministic,
    v_security_type,
    v_param_list,
    v_returns,
    v_body,
    v_definer,
    v_created,
    v_modified,
    v_sql_mode,
    v_comment,
    v_character_set_client,
    v_collation_connection,
    v_db_collation,
    v_body_utf8    
    );

  END LOOP;

  CLOSE c_sql_query;
  DROP VIEW IF EXISTS temp_view;
END $$

DELIMITER ;

-- (routine creation not work in DDL)
/*
DELIMITER $$

CREATE FUNCTION `sql_create_routine`(
  p_schema_name TINYTEXT,
  p_routine_name TINYTEXT) RETURNS TEXT
BEGIN
  DECLARE v_sql_query TEXT DEFAULT '';

  SELECT CONCAT('CREATE ',TYPE,' `',NAME,'` (',PARAM_LIST,' )',IF(TYPE='FUNCTION',CONCAT(' RETURNS ',RETURNS),''),'\n',BODY,';')
  INTO v_sql_query  
  FROM mysql.proc where db=(SELECT schema());

  RETURN v_sql_query;  
END $$

DELIMITER ;
*/
--

DELIMITER $$

CREATE PROCEDURE `clone_grants`(
  p_user_name TINYTEXT,
  p_old_schema_name TINYTEXT,
  p_new_schema_name TINYTEXT)
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;
  DECLARE v_sql_query TINYTEXT DEFAULT '';

  DECLARE c_sql_query CURSOR FOR SELECT CONCAT(PRIVILEGE_TYPE,IF(IS_GRANTABLE='YES',' WITH GRANT OPTION',''))
  FROM INFORMATION_SCHEMA.SCHEMA_PRIVILEGES WHERE GRANTEE LIKE CONCAT('%',p_user_name,'%') AND TABLE_SCHEMA=p_old_schema_name;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  SET @str='GRANT ';

  OPEN c_sql_query;

  get_data: LOOP
    FETCH c_sql_query INTO v_sql_query;

    IF v_finished THEN LEAVE get_data; END IF;

    SET @str=CONCAT(@str,v_sql_query,',');

  END LOOP;

  CLOSE c_sql_query;

  IF(SUBSTR(@str,-1)=',') THEN SET @str=SUBSTR(@str,1,CHAR_LENGTH(@str)-1); END IF;
  
  SET @str=CONCAT(@str,' ON ',p_new_schema_name,'.* TO \'',p_user_name,'\'@\'localhost\'');

  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str='FLUSH PRIVILEGES';

  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE PROCEDURE `copy_tables_data`(
  p_old_schema_name TINYTEXT,
  p_new_schema_name TINYTEXT)
BEGIN
  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.settings SELECT * FROM ',p_old_schema_name,'.settings');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.currency SELECT * FROM ',p_old_schema_name,'.currency');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.language SELECT * FROM ',p_old_schema_name,'.language');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.color SELECT * FROM ',p_old_schema_name,'.color');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.attr SELECT * FROM ',p_old_schema_name,'.attr');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.attr_part SELECT * FROM ',p_old_schema_name,'.attr_part');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.tax SELECT * FROM ',p_old_schema_name,'.tax');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.discount SELECT * FROM ',p_old_schema_name,'.discount');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  /*users need set one admin*/

  /*
  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.user SELECT * FROM ',p_old_schema_name,'.user');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;
  */
  /*admin user*/
  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.user(username,password,type,first_name,last_name,call_name,create_date) VALUES(\'admin\',\'admin\',1,\'your_admin_fn\',\'your_admin_ln\',\'admin\',Now())');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;
  /*demo user*/
  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.user(username,password,type,first_name,last_name,call_name,create_date) VALUES(\'Demouser\',\'demouser\',3,\'Anonymous\',\'User\',\'Demouser\',Now())');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;
  /*customer*/
  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.user(username,password,type,first_name,last_name,call_name,create_date) VALUES(\'customer\',\'\',3,\'\',\'\',\'customer\',Now())');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.sensor SELECT * FROM ',p_old_schema_name,'.sensor');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.sensor_group SELECT * FROM ',p_old_schema_name,'.sensor_group');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.track_type SELECT * FROM ',p_old_schema_name,'.track_type');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.order_status SELECT * FROM ',p_old_schema_name,'.order_status');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.manufacture SELECT * FROM ',p_old_schema_name,'.manufacture');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.product_type SELECT * FROM ',p_old_schema_name,'.product_type');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.store SELECT * FROM ',p_old_schema_name,'.store');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.store_part SELECT * FROM ',p_old_schema_name,'.store_part');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.transport SELECT * FROM ',p_old_schema_name,'.transport');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.transport_type SELECT * FROM ',p_old_schema_name,'.transport_type');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.delivery SELECT * FROM ',p_old_schema_name,'.delivery');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.delivery_type SELECT * FROM ',p_old_schema_name,'.delivery_type');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  -- one temp product
  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.product(id,manufacture_id,discount_code,name,description,code,price) VALUES (1,1,\'0001\',\'My product\',\'My product for sale\',\'01\',5.00)');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.product_type_part(product_id,product_type_id) VALUES (1,1)');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  -- for audit ON and schema name stamp
  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.preferences(code,name) VALUES(\'fA\',\'',p_new_schema_name,'\')');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  -- sys proc (not copy sys procedures and functions)
  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.temp_proc SELECT * FROM mysql.proc WHERE db=\'',p_old_schema_name,'\' AND name NOT LIKE \'sql_%\' AND name NOT LIKE \'clone_%\' AND name NOT LIKE \'copy_%\' AND name NOT LIKE \'%_schema\'');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('UPDATE ',p_new_schema_name,'.temp_proc SET db=\'',p_new_schema_name,'\'',' WHERE db=\'',p_old_schema_name,'\'');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

END $$

DELIMITER ;

--

DELIMITER $$

CREATE PROCEDURE `copy_user_data_as_admin`(
  p_old_schema_name TINYTEXT,
  p_new_schema_name TINYTEXT,
  p_username TINYTEXT)
BEGIN
  SET @str=CONCAT('INSERT INTO ',p_new_schema_name,'.user SELECT * FROM ',p_old_schema_name,'.user WHERE username=\'',p_username,'\'');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('UPDATE ',p_new_schema_name,'.user SET type=1, id=0 WHERE username=\'',p_username,'\'');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;
END $$

DELIMITER ;

-- (triggers can't clone by mysql stored routines)

DELIMITER $$

CREATE PROCEDURE `clone_schema`(
  p_old_schema_name TINYTEXT,
  p_new_schema_name TINYTEXT)
BEGIN
  DECLARE v_old_schema_name TINYTEXT;
  DECLARE v_new_schema_name TINYTEXT;
  SET v_old_schema_name=LOWER(p_old_schema_name);
  SET v_new_schema_name=LOWER(p_new_schema_name);

  SET @str=CONCAT('CREATE SCHEMA ',v_new_schema_name);
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  CALL clone_tables(v_old_schema_name,v_new_schema_name);
  CALL copy_tables_data(v_old_schema_name,v_new_schema_name);
  CALL clone_proc(v_new_schema_name);
  CALL clone_grants('fa_admin',v_old_schema_name,v_new_schema_name);
  CALL clone_grants('fa_client',v_old_schema_name,v_new_schema_name);
END $$

DELIMITER ;

--

DELIMITER $$

CREATE PROCEDURE `remove_schema`(
  p_schema_name TINYTEXT)
BEGIN
  SET @str=CONCAT('DROP SCHEMA ',p_schema_name);
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('REVOKE ALL ON ',p_schema_name,'.* FROM \'fa_client\'@\'localhost\'');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  SET @str=CONCAT('REVOKE ALL ON ',p_schema_name,'.* FROM \'fa_admin\'@\'localhost\'');
  PREPARE stmt FROM @str;
  EXECUTE stmt;
  DEALLOCATE PREPARE stmt;

  FLUSH PRIVILEGES;
END $$

DELIMITER ;

-- Wordpress | Woocommerce [sync]
/*

  Wordpress & fireArt allocated in one database schema

  Wordpress password example : $P$B/fLziz9Ahtqh9XloocggtRfgpc5C71 == 123456
  
  wp_users Wordpress users table
  wp_usermeta Wordpress users additional data (types, names, etc)
  
  Wordpress | Woocommerce new user
  password encoding with MD5 algorytm

  example:
  INSERT INTO `wp_users` (`ID`, `user_login`, `user_pass`, `user_nicename`, `user_email`, `user_url`, `user_registered`, `user_activation_key`, `user_status`, `display_name`)
  VALUES (0,'tester',MD5('123456'),'tester','tester@gmail.com','',Now(),'',0,'Super Tester');  
  
  Wordpress | Woocommerce user type:
  
  [Customer]
  wp_usermeta.user_level = 0
  wp_usermeta.wp_capabilities = a:1:{s:8:"customer";b:1;}
  
  [Shop manager | Driver | Deliveryman]
  wp_usermeta.user_level = 9
  wp_usermeta.wp_capabilities = a:1:{s:12:"shop_manager";b:1;}
  
  [Administrator]
  wp_usermeta.user_level = 10
  wp_usermeta.wp_capabilities = a:1:{s:13:"administrator";b:1;}
  
  example:
  SET @id=(SELECT MAX(`umeta_id`)+1 FROM `wp_usermeta`);
  
  INSERT INTO `wp_usermeta` (`umeta_id`,`user_id`,`meta_key`,`meta_value`)
  VALUES (@id,0,'wp_user_level','10'); -- admin level
  INSERT INTO `wp_usermeta` (`umeta_id`,`user_id`,`meta_key`,`meta_value`)
  VALUES (@id,0,'wp_capabilities','a:1:{s:13:"administrator";b:1;}'); -- admin capabalites
*/

-- copy new fa_users -> wp_users (to hang on trigger user.after_insert)

DELIMITER $$

CREATE PROCEDURE `sync_fa_users_to_wp_users`()
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;
  DECLARE v_sql_query TINYTEXT DEFAULT '';
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user_first_name TINYTEXT;
  DECLARE v_user_last_name TINYTEXT;
  DECLARE v_user_call_name TINYTEXT;
  DECLARE v_user_level INTEGER;
  DECLARE v_user_capabilities TINYTEXT;

  DECLARE c_sql_query CURSOR FOR SELECT CONCAT('INSERT INTO wp_users(user_login,user_pass,user_nicename,user_email,user_registered,user_status,display_name)',
  'VALUES (\'',username,'\',','\'',MD5(password),'\',','\'',call_name,'\',','\'',email,'\',',create_date,',',IF(active,0,-1),',\'',first_name,' ',last_name,'\''), type, first_name, last_name, call_name
  FROM user WHERE username NOT IN (SELECT user_login FROM wp_users);

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_sql_query;

  get_data: LOOP
    FETCH c_sql_query INTO v_sql_query,v_user_type,v_user_first_name,v_user_last_name,v_user_call_name;

    IF v_finished THEN LEAVE get_data; END IF;

    SET @str=v_sql_query;

    PREPARE stmt FROM @str;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET v_user_id=LAST_INSERT_ID();

    -- need to add 2 records to wp_usermeta
    CASE v_user_type
      WHEN 1 THEN BEGIN SET v_user_level=10; SET v_user_capabilities='a:1:{s:13:"administrator";b:1;}'; END;
      WHEN 2 THEN BEGIN SET v_user_level=9; SET v_user_capabilities='a:1:{s:12:"shop_manager";b:1;}'; END;
      WHEN 3 THEN BEGIN SET v_user_level=0; SET v_user_capabilities='a:1:{s:8:"customer";b:1;}'; END;
      ELSE BEGIN SET v_user_level=0; SET v_user_capabilities='a:1:{s:8:"customer";b:1;}'; END;
    END CASE;

	SET @str=CONCAT('INSERT INTO wp_usermeta(user_id,meta_key,meta_value) VALUES(',v_user_id,',','\'wp_user_level\',','\'',v_user_level,'\'');
    PREPARE stmt FROM @str;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

	SET @str=CONCAT('INSERT INTO wp_usermeta(user_id,meta_key,meta_value) VALUES(',v_user_id,',','\'wp_capabilities\',','\'',v_user_capabilities,'\'');
    PREPARE stmt FROM @str;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    -- first_name
    SET @str=CONCAT('INSERT INTO wp_usermeta(user_id,meta_key,meta_value) VALUES(',v_user_id,',','\'first_name\',','\'',v_user_first_name,'\'');
    PREPARE stmt FROM @str;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    -- last_name
    SET @str=CONCAT('INSERT INTO wp_usermeta(user_id,meta_key,meta_value) VALUES(',v_user_id,',','\'last_name\',','\'',v_user_last_name,'\'');
    PREPARE stmt FROM @str;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    -- nickname
    SET @str=CONCAT('INSERT INTO wp_usermeta(user_id,meta_key,meta_value) VALUES(',v_user_id,',','\'nickname\',','\'',v_user_call_name,'\'');
    PREPARE stmt FROM @str;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

  END LOOP;

  CLOSE c_sql_query;
END $$

DELIMITER ;

-- copy new wp_users -> fa_users (to hang on trigger wp_users.after_insert)

DELIMITER $$

CREATE PROCEDURE `sync_wp_users_to_fa_users`()
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;
  DECLARE v_sql_query TINYTEXT DEFAULT '';

  DECLARE c_sql_query CURSOR FOR SELECT CONCAT('INSERT INTO user(username,password,first_name,last_name,call_name,email,type,active,create_date)',
  'VALUES (\'',u.user_login,'\',','\'',u.user_pass,'\',',
           '\'',(SELECT meta_value FROM wp_usermeta WHERE user_id=u.ID AND meta_key='first_name'),'\',',
           '\'',(SELECT meta_value FROM wp_usermeta WHERE user_id=u.ID AND meta_key='last_name'),'\',',
           '\'',(SELECT meta_value FROM wp_usermeta WHERE user_id=u.ID AND meta_key='nickname'),'\',',
           '\'',u.user_email,'\',',
           (CASE(SELECT meta_value FROM wp_usermeta WHERE user_id=u.ID AND meta_key='wp_user_level') WHEN '10' THEN '1' WHEN '9' THEN '2' ELSE '3' END),',',
           IF(u.user_status=0,'1','0'),',',
           u.user_registered)
  FROM wp_users u WHERE u.user_login NOT IN (SELECT username FROM user);

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_sql_query;

  get_data: LOOP
    FETCH c_sql_query INTO v_sql_query,v_user_type;

    IF v_finished THEN LEAVE get_data; END IF;

    SET @str=v_sql_query;

    PREPARE stmt FROM @str;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

  END LOOP;

  CLOSE c_sql_query;
END $$

DELIMITER ;

-- update fa_user (to hang on trigger user.after_update)

DELIMITER $$

CREATE PROCEDURE `update_fa_user`(
  p_username VARCHAR(64),
  p_type INTEGER,
  p_active INTEGER,
  p_first_name VARCHAR(32),
  p_last_name VARCHAR(32),
  p_call_name VARCHAR(32),
  p_email VARCHAR(64),
  p_phone VARCHAR(20))
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_level INTEGER;
  DECLARE v_user_capabilities TINYTEXT;

  SELECT ID INTO v_user_id
  FROM wp_users
  WHERE user_login=p_username;

  IF v_user_id IS NULL THEN

    UPDATE wp_users SET user_nicename=p_call_name,
						user_email=p_email,
						user_status=IF(p_active=1,0,-1),
						display_name=CONCAT(p_first_name,' ',p_last_name)
    WHERE user_login=p_username;

    -- need to update 2 records to wp_usermeta
    CASE p_type
      WHEN 1 THEN BEGIN SET v_user_level=10; SET v_user_capabilities='a:1:{s:13:"administrator";b:1;}'; END;
      WHEN 2 THEN BEGIN SET v_user_level=9; SET v_user_capabilities='a:1:{s:12:"shop_manager";b:1;}'; END;
      WHEN 3 THEN BEGIN SET v_user_level=0; SET v_user_capabilities='a:1:{s:8:"customer";b:1;}'; END;
      ELSE BEGIN SET v_user_level=0; SET v_user_capabilities='a:1:{s:8:"customer";b:1;}'; END;
    END CASE;
    UPDATE wp_usermeta SET meta_value=v_user_level WHERE user_id=p_user_id AND meta_key='wp_user_level';
    UPDATE wp_usermeta SET meta_value=v_user_capabilities WHERE user_id=p_user_id AND meta_key='wp_capabilities';

    UPDATE wp_usermeta SET meta_value=p_first_name WHERE user_id=p_user_id AND meta_key='first_name';
    UPDATE wp_usermeta SET meta_value=p_last_name WHERE user_id=p_user_id AND meta_key='last_name';
    UPDATE wp_usermeta SET meta_value=p_call_name WHERE user_id=p_user_id AND meta_key='nickname';

  END IF;
END $$

DELIMITER ;

-- update fa_user_password (to hang on trigger user.before_update)

DELIMITER $$

CREATE PROCEDURE `update_fa_user_password`(
  p_username VARCHAR(64),
  p_password VARCHAR(16))
BEGIN
  DECLARE v_user_id INTEGER;

  SELECT ID INTO v_user_id
  FROM wp_users
  WHERE user_login=p_username;

  IF v_user_id IS NULL THEN

    UPDATE wp_users SET user_pass=MD5(p_password) WHERE user_login=p_username;

  END IF;
END $$

DELIMITER ;

-- update wp_usermeta (to hang on trigger wp_usermeta.after_update)

DELIMITER $$

CREATE PROCEDURE `update_wp_usermeta`(
  p_username VARCHAR(64),
  p_first_name TINYTEXT,
  p_last_name TINYTEXT,
  p_nickname TINYTEXT,
  p_user_level TINYTEXT)
BEGIN
  DECLARE v_user_id INTEGER;

  SELECT id INTO v_user_id
  FROM user
  WHERE username=p_username;

  IF v_user_id IS NULL THEN

    UPDATE user SET first_name=p_first_name,
					last_name=p_last_name,
					call_name=p_nickname,
					type=(CASE(p_user_level) WHEN '10' THEN 1 WHEN '9' THEN 2 ELSE 3 END)
    WHERE username=p_username;

  END IF;
END $$

DELIMITER ;

--

-- update wp_users (to hang on trigger wp_users.after_update)

DELIMITER $$

CREATE PROCEDURE `update_wp_users`(
  p_username VARCHAR(64),
  p_user_email TINYTEXT,
  p_user_status INTEGER)
BEGIN
  DECLARE v_user_id INTEGER;

  SELECT id INTO v_user_id
  FROM user
  WHERE username=p_username;

  IF v_user_id IS NULL THEN

    UPDATE user SET email=p_user_email,
					active=IF(u.user_status=0,1,0)
	WHERE username=p_username;

  END IF;
END $$

DELIMITER ;

-- NEXT WISHES ->


-- -------------------------------------------------------------------------------------------------------------
-- views [not used]

-- -------------------------------------------------------------------------------------------------------------
-- procedures

DELIMITER $$

CREATE /*DEFINER='admin'@'localhost'*/ PROCEDURE `add_audit`(
  p_username VARCHAR(64),
  p_name VARCHAR(48),
  p_description VARCHAR(128))
-- SQL SECURITY INVOKER (execute under executor grants)
BEGIN
  DECLARE v_audit BOOLEAN;

  SELECT audit FROM preferences WHERE code='fA' INTO v_audit;

  IF v_audit THEN
    INSERT INTO audit (username,name,description) VALUES (p_username,p_name,p_description);
  END IF;

END $$

DELIMITER ;

--

DELIMITER $$

CREATE /*DEFINER='admin'@'localhost'*/ PROCEDURE `update_audit_message`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),  
  p_audit_id INTEGER,
  p_message VARCHAR(256))
BEGIN
  UPDATE audit SET message=p_message WHERE id=p_audit_id;
END $$

DELIMITER ;

-- -------------------------------------------------------------------------------------------------------------
-- functions

DELIMITER $$

CREATE /*DEFINER='admin'@'localhost'*/ FUNCTION `replace_text`(
  p_text MEDIUMBLOB) RETURNS MEDIUMBLOB
BEGIN
  RETURN CAST(REPLACE(REPLACE(p_text,'\\','\\\\'),CHAR(34),CONCAT('\\',CHAR(34))) AS CHAR);
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_project`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_name VARCHAR(64),
  p_description VARCHAR(128),
  p_ip_address VARCHAR(15)) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);
  
  DECLARE v_api_key VARCHAR(64);
  DECLARE v_name VARCHAR(64);
  DECLARE v_schema_name VARCHAR(64);
  DECLARE v_schema_prefix_name VARCHAR(256); -- as settings value
  DECLARE v_ip_address VARCHAR(15);
  DECLARE v_time_left INTEGER; -- in hours
  
  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_project',CONCAT('error: code=-1 username=',p_user,' name=',p_name,' ip_address=',p_ip_address));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR v_user='fa_client' THEN

    -- name verify for unique
    SELECT name INTO v_name FROM project WHERE name=p_name;
    
    IF v_name IS NOT NULL THEN
      CALL add_audit(p_user,'add_project',CONCAT('error: code=-11 username=',p_user,' name=',p_name,' ip_address=',p_ip_address));
      RETURN -11;
	END IF;
        
    -- ip_address and time creation verify
    IF v_user_type>1 THEN
      SELECT ip_address,(UNIX_TIMESTAMP(Now())-UNIX_TIMESTAMP(create_date))/3600 INTO v_ip_address,v_time_left FROM project WHERE ip_address=p_ip_address ORDER BY create_date ASC LIMIT 0,1;

      IF v_ip_address IS NOT NULL AND v_time_left < 2 THEN
        CALL add_audit(p_user,'add_project',CONCAT('error: code=-12 username=',p_user,' name=',p_name,' ip_address=',p_ip_address));
        RETURN -12;
      END IF;
    END IF;

	-- api_key
    SELECT value INTO v_schema_prefix_name FROM settings WHERE code='schema_prefix_name';
    IF v_schema_prefix_name IS NULL THEN SET v_schema_prefix_name='_'; END IF;

    SET v_schema_name=LOWER(CONCAT(v_schema_prefix_name,REPLACE(p_name,' ','_')));
    SET v_api_key=UPPER(MD5(ENCODE(v_schema_name,p_pass)));
    -- SET v_api_key=v_schema_name;

    INSERT INTO project (user_id,name,description,ip_address,api_key,schema_name,active)
    VALUES (v_user_inner_id,p_name,p_description,p_ip_address,v_api_key,v_schema_name,TRUE);

    CALL add_audit(p_user,'add_project',CONCAT('success: code=0 username=',p_user,' api_key=',v_api_key,/*' schema_name=',v_schema_name,*/' ip_address=',p_ip_address));

  ELSE
    CALL add_audit(p_user,'add_project',CONCAT('error: code=-3 username=',p_user,' name=',p_name,' ip_address=',p_ip_address));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_project`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_schema_name VARCHAR(64)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_project',CONCAT('error: code=-1 username=',p_user,' schema_name=',p_schema_name));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM project WHERE schema_name=p_schema_name;    

    CALL add_audit(p_user,'remove_project',CONCAT('success: code=0 username=',p_user,' schema_name=',p_schema_name));

  ELSE
    CALL add_audit(p_user,'remove_project',CONCAT('error: code=-3 username=',p_user,' schema_name=',p_schema_name));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_project_activity`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_schema_name VARCHAR(64),
  p_activity TINYINT(1)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_project_activity',CONCAT('error: code=-1 username=',p_user,' schema_name=',p_schema_name));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    UPDATE project SET active=p_activity WHERE schema_name=p_schema_name;

    CALL add_audit(p_user,'update_project_activity',CONCAT('success: code=0 username=',p_user,' schema_name=',p_schema_name,' active=',p_activity));

  ELSE
    CALL add_audit(p_user,'update_project_activity',CONCAT('error: code=-3 username=',p_user,' schema_name=',p_schema_name));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_user`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_type INTEGER,
  p_first_name VARCHAR(32),
  p_last_name VARCHAR(32),
  p_call_name VARCHAR(32),
  p_email VARCHAR(64),
  p_phone VARCHAR(20),
  p_username VARCHAR(64),
  p_password VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user_count INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT count(*) INTO v_user_count
  FROM user
  WHERE username=p_username;

  IF v_user_count>0 THEN

    SELECT id INTO v_id
    FROM user
    WHERE username=p_username AND password=p_password;

    IF v_id IS NULL THEN
      CALL add_audit(p_username,'add_user',CONCAT('error: code=-2 username=',p_username));
      RETURN -2;
    ELSE
      RETURN v_id;
    END IF;

  END IF;

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     v_user='fa_client' THEN

    IF v_user_type=1 THEN
      INSERT INTO user (type,first_name,last_name,call_name,email,phone,username,password)
      VALUES (p_type,p_first_name,p_last_name,p_call_name,p_email,p_phone,p_username,p_password);
    ELSE
      INSERT INTO user (type,first_name,last_name,call_name,email,phone,username,password)
      VALUES (3,p_first_name,p_last_name,p_call_name,p_email,p_phone,p_username,p_password);
    END IF;

    SET ret_val=LAST_INSERT_ID();

    CALL add_audit(p_username,'add_user',CONCAT('success: code=0 username=',p_username,' id=',ret_val));

  ELSE
    CALL add_audit(p_username,'add_user',CONCAT('error: code=-3 username=',p_username));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_user`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_user',CONCAT('error: code=-1 username=',p_user,' id=',p_user_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM user WHERE id=p_user_id;

    CALL add_audit(p_user,'remove_user',CONCAT('success: code=0 username=',p_user,' id=',p_user_id));

  ELSE
    CALL add_audit(p_user,'remove_user',CONCAT('error: code=-3 username=',p_user,' id=',p_user_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_user`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_type INTEGER,
  p_first_name VARCHAR(32),
  p_last_name VARCHAR(32),
  p_call_name VARCHAR(32),
  p_email VARCHAR(64),
  p_phone VARCHAR(20)) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_type INTEGER;
  DECLARE v_first_name VARCHAR(32);
  DECLARE v_last_name VARCHAR(32);
  DECLARE v_call_name VARCHAR(32);
  DECLARE v_email VARCHAR(64);
  DECLARE v_phone VARCHAR(20);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_user',CONCAT('error: code=-1 username=',p_user,' id=',p_user_id));
    RETURN -1;
  END IF;

  IF v_user_type=1 THEN
    SELECT id,type,first_name,last_name,call_name,email,phone INTO v_user_id,v_type,v_first_name,v_last_name,v_call_name,v_email,v_phone
    FROM user
    WHERE id=p_user_id;
  ELSE
    SELECT id,type,first_name,last_name,call_name,email,phone INTO v_user_id,v_type,v_first_name,v_last_name,v_call_name,v_email,v_phone
    FROM user
    WHERE username=p_user AND password=p_pass;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF p_user_id=-1 THEN SET p_user_id=v_user_id; END IF;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=p_user_id) THEN

    IF p_type!=-1 AND v_user_type=1 THEN SET v_type=p_type; END IF;

    IF p_first_name!='' THEN SET v_first_name=p_first_name; END IF;
    IF p_last_name!='' THEN SET v_last_name=p_last_name; END IF;
    IF p_call_name!='' THEN SET v_call_name=p_call_name; END IF;
    IF p_email!='' THEN SET v_email=p_email; END IF;
    IF p_phone!='' THEN SET v_phone=p_phone; END IF;

    UPDATE user SET type=v_type,
                    first_name=v_first_name,
                    last_name=v_last_name,
                    call_name=v_call_name,
                    email=v_email,
                    phone=v_phone
    WHERE id=p_user_id;

    CALL add_audit(p_user,'update_user',CONCAT('success: code=0 username=',p_user,' id=',p_user_id));

  ELSE
    CALL add_audit(p_user,'update_user',CONCAT('error: code=-3 username=',p_user,' id=',p_user_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_user_picture`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_picture MEDIUMBLOB) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_user_picture',CONCAT('error: code=-1 username=',p_user,' id=',p_user_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF p_user_id=-1 THEN SET p_user_id=v_user_id; END IF;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=p_user_id) THEN

    IF p_picture IS NOT NULL THEN
      UPDATE user SET picture=p_picture
      WHERE id=p_user_id;

      CALL add_audit(p_user,'update_user_picture',CONCAT('success: code=0 username=',p_user,' id=',p_user_id));

    END IF;

  ELSE
    CALL add_audit(p_user,'update_user_picture',CONCAT('error: code=-3 username=',p_user,' id=',p_user_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_user_discount`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_discount_code VARCHAR(8)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_user_discount',CONCAT('error: code=-1 username=',p_user,' id=',p_user_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_discount_code='' THEN SET p_discount_code=NULL; END IF;

    UPDATE user SET discount_code=p_discount_code WHERE id=p_user_id;

    CALL add_audit(p_user,'update_user_discount',CONCAT('success: code=0 username=',p_user,' id=',p_user_id));

  ELSE
    CALL add_audit(p_user,'update_user_discount',CONCAT('error: code=-3 username=',p_user,' id=',p_user_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_user_activity`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_activity TINYINT(1)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_user_activity',CONCAT('error: code=-1 username=',p_user,' id=',p_user_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    UPDATE user SET active=p_activity WHERE id=p_user_id;

    CALL add_audit(p_user,'update_user_activity',CONCAT('success: code=0 username=',p_user,' id=',p_user_id,' active=',p_activity));

  ELSE
    CALL add_audit(p_user,'update_user_activity',CONCAT('error: code=-3 username=',p_user,' id=',p_user_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_user_password`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_password_for_update VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_user_password',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF (v_user='root' OR v_user='fa_admin' OR
      v_user='fa_client') AND p_password_for_update!='' THEN
    UPDATE user SET password=p_password_for_update
    WHERE username=p_user;
    CALL add_audit(p_user,'update_user_password',CONCAT('success: code=0 username=',p_user));
  ELSE
    CALL add_audit(p_user,'update_user_password',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_user_prepaid_amount`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_prepaid_amount VARCHAR(8)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_user_prepaid_amount',CONCAT('error: code=-1 username=',p_user,' id=',p_user_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_prepaid_amount='' THEN SET p_prepaid_amount=NULL; END IF;

    UPDATE user SET prepaid_amount=p_prepaid_amount WHERE id=p_user_id;

    CALL add_audit(p_user,'update_user_prepaid_amount',CONCAT('success: code=0 username=',p_user,' id=',p_user_id));

  ELSE
    CALL add_audit(p_user,'update_user_prepaid_amount',CONCAT('error: code=-3 username=',p_user,' id=',p_user_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `increase_user_prepaid_amount`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_amount DECIMAL(6,2)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);
  
  DECLARE v_prepaid_amount DECIMAL(6,2);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'increase_user_prepaid_amount',CONCAT('error: code=-1 username=',p_user,' id=',p_user_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1 AND p_prepaid_amount!='') THEN

    SELECT prepaid_amount INTO v_prepaid_amount FROM user WHERE id=p_user_id;

    SET v_prepaid_amount=v_prepaid_amount+p_amount;

    UPDATE user SET prepaid_amount=v_prepaid_amount WHERE id=p_user_id;

    CALL add_audit(p_user,'increase_user_prepaid_amount',CONCAT('success: code=0 username=',p_user,' id=',p_user_id,' +',p_amount));

  ELSE
    CALL add_audit(p_user,'increase_user_prepaid_amount',CONCAT('error: code=-3 username=',p_user,' id=',p_user_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `decrease_user_prepaid_amount`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_amount DECIMAL(6,2)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);
  
  DECLARE v_prepaid_amount DECIMAL(6,2);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'decrease_user_prepaid_amount',CONCAT('error: code=-1 username=',p_user,' id=',p_user_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1 AND p_prepaid_amount!='') THEN

    SELECT prepaid_amount INTO v_prepaid_amount FROM user WHERE id=p_user_id;

    SET v_prepaid_amount=v_prepaid_amount-p_amount;

    UPDATE user SET prepaid_amount=v_prepaid_amount WHERE id=p_user_id;

    CALL add_audit(p_user,'decrease_user_prepaid_amount',CONCAT('success: code=0 username=',p_user,' id=',p_user_id,' -',p_amount));

  ELSE
    CALL add_audit(p_user,'decrease_user_prepaid_amount',CONCAT('error: code=-3 username=',p_user,' id=',p_user_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `increase_user_prepaid_amount_by_prepaid_code`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_prepaid_code VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);
  
  DECLARE v_prepaid_amount DECIMAL(6,2);
  DECLARE v_amount DECIMAL(6,2);
  
  DECLARE v_prepaid_card_id INTEGER;
  DECLARE v_active BOOLEAN;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'increase_user_prepaid_amount_by_prepaid_code',CONCAT('error: code=-1 username=',p_user,' prepaid_code=',p_prepaid_code));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND p_prepaid_code!='') THEN

    SELECT id,amount,active INTO v_prepaid_card_id,v_amount,v_active FROM prepaid_card WHERE prepaid_code=p_prepaid_code;

    IF (v_active IS NULL) OR (v_active IS FALSE) THEN
      CALL add_audit(p_user,'increase_user_prepaid_amount_by_prepaid_code',CONCAT('error: code=-4 username=',p_user,' prepaid_code=',p_prepaid_code));
      RETURN -4;
    END IF;

    SELECT prepaid_amount INTO v_prepaid_amount FROM user WHERE id=v_user_id;

    SET v_prepaid_amount=v_prepaid_amount+v_amount;

    UPDATE user SET prepaid_amount=v_prepaid_amount WHERE id=v_user_id;
    
    UPDATE prepaid_card SET active=FALSE WHERE id=v_prepaid_card_id;

    CALL add_audit(p_user,'increase_user_prepaid_amount_by_prepaid_code',CONCAT('success: code=0 username=',p_user,' prepaid_code=',p_prepaid_code,' id=',p_user_id,' +',v_amount));

  ELSE
    CALL add_audit(p_user,'increase_user_prepaid_amount_by_prepaid_code',CONCAT('error: code=-3 username=',p_user,' prepaid_code=',p_prepaid_code));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_user_review`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_review_user_id INTEGER,
  p_review_count INTEGER,
  p_review_value INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_user_review',CONCAT('error: code=-1 username=',p_user,' id=',p_review_user_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    UPDATE user SET review_count=p_review_count,
                    review_value=p_review_value
	WHERE id=p_review_user_id;

    CALL add_audit(p_user,'update_user_review',CONCAT('success: code=0 username=',p_user,' id=',p_review_user_id));

  ELSE
    CALL add_audit(p_user,'update_user_review',CONCAT('error: code=-3 username=',p_user,' id=',p_review_user_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_user_review`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_review_user_id INTEGER,
  p_description VARCHAR(128),
  p_value INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_review_count INTEGER;
  DECLARE v_review_value INTEGER;

  DECLARE v_count INTEGER;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_user_review',CONCAT('error: code=-1 username=',p_user,' id=',p_review_user_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF p_user_id=-1 THEN SET p_user_id=v_user_id; END IF;

  SELECT count(*) INTO v_count
  FROM user_review
  WHERE user_id=p_user_id AND review_user_id=p_review_user_id;

  IF v_count>10 THEN
    CALL add_audit(p_user,'add_user_review',CONCAT('error: code=-4 username=',p_user,' id=',p_review_user_id,' value=',p_value));
    RETURN -4;
  END IF;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR v_user_id=p_user_id THEN

    SELECT review_count,review_value INTO v_review_count,v_review_value
    FROM user
    WHERE id=p_review_user_id;

    SET v_review_count=v_review_count+1;
    SET v_review_value=v_review_value+p_value;

    INSERT INTO user_review (user_id,review_user_id,description,value)
    VALUES (p_user_id,p_review_user_id,p_description,p_value);

    UPDATE user SET review_count=v_review_count,
                    review_value=v_review_value
    WHERE id=p_review_user_id;

    CALL add_audit(p_user,'add_user_review',CONCAT('success: code=0 username=',p_user,' id=',p_review_user_id));

  ELSE
    CALL add_audit(p_user,'add_user_review',CONCAT('error: code=-3 username=',p_user,' id=',p_review_user_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_user_review`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_review_user_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_review_count INTEGER;
  DECLARE v_review_value INTEGER;

  DECLARE v_value INTEGER;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_user_review',CONCAT('error: code=-1 username=',p_user,' id=',p_review_user_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF p_user_id=-1 THEN SET p_user_id=v_user_id; END IF;

  SELECT value INTO v_value
  FROM user_review
  WHERE user_id=p_user_id AND review_user_id=p_review_user_id;

  IF v_value IS NULL THEN
    CALL add_audit(p_user,'remove_user_review',CONCAT('error: code=-4 username=',p_user,' id=',p_review_user_id));
    RETURN -4;
  END IF;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR v_user_id=p_user_id THEN

    SELECT review_count,review_value INTO v_review_count,v_review_value
    FROM user
    WHERE id=p_review_user_id;

    SET v_review_count=v_review_count-1;
    SET v_review_value=v_review_value-v_value;

    DELETE FROM user_review
    WHERE user_id=p_user_id AND review_user_id=p_review_user_id;

    UPDATE user SET review_count=v_review_count,
                    review_value=v_review_value
    WHERE id=p_review_user_id;

    CALL add_audit(p_user,'remove_user_review',CONCAT('success: code=0 username=',p_user,' id=',p_review_user_id));

  ELSE
    CALL add_audit(p_user,'remove_user_review',CONCAT('error: code=-1 username=',p_user,' id=',p_review_user_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_prepaid_card`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_serial_number VARCHAR(64),
  p_prepaid_code VARCHAR(16),
  p_amount VARCHAR(8)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_prepaid_card',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_amount='' THEN SET p_amount='0.00'; END IF;

    INSERT INTO prepaid_card (serial_number,prepaid_code,amount)
    VALUES (p_serial_number,p_prepaid_code,p_amount);

    SET ret_val=LAST_INSERT_ID();

    CALL add_audit(p_user,'add_prepaid_card',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_prepaid_card',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_prepaid_card_activity`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_prepaid_card_id INTEGER,
  p_activity TINYINT(1)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_prepaid_card_activity',CONCAT('error: code=-1 username=',p_user,' id=',p_prepaid_card_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    UPDATE prepaid_card SET active=p_activity WHERE id=p_prepaid_card_id;

    CALL add_audit(p_user,'update_prepaid_card_activity',CONCAT('success: code=0 username=',p_user,' id=',p_prepaid_card_id,' active=',p_activity));

  ELSE
    CALL add_audit(p_user,'update_prepaid_card_activity',CONCAT('error: code=-3 username=',p_user,' id=',p_prepaid_card_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_prepaid_card`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_prepaid_card_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_prepaid_card',CONCAT('error: code=-1 username=',p_user,' id=',p_prepaid_card_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM prepaid_card WHERE id=p_prepaid_card_id;

    CALL add_audit(p_user,'remove_prepaid_card',CONCAT('success: code=0 username=',p_user,' id=',p_prepaid_card_id));

  ELSE
    CALL add_audit(p_user,'remove_prepaid_card',CONCAT('error: code=-3 username=',p_user,' id=',p_prepaid_card_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_discount`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_product_type_id INTEGER,
  p_type INTEGER,
  p_code VARCHAR(8),
  p_name VARCHAR(32),
  p_value INTEGER,
  p_language VARCHAR(32),
  p_start_date VARCHAR(20),
  p_finish_date VARCHAR(20)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_discount',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_product_type_id=-1 THEN SET p_product_type_id=NULL; END IF;
    IF p_start_date='' THEN SET p_start_date=NULL; END IF;
    IF p_finish_date='' THEN SET p_finish_date=NULL; END IF;

    INSERT INTO discount (product_type_id,type,code,name,value,language,start_date,finish_date)
    VALUES (p_product_type_id,p_type,p_code,p_name,p_value,p_language,p_start_date,p_finish_date);

    SET ret_val=LAST_INSERT_ID();

    CALL add_audit(p_user,'add_discount',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_discount',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_discount`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_discount_id INTEGER,
  p_product_type_id INTEGER,
  p_type INTEGER,
  p_code VARCHAR(8),
  p_name VARCHAR(32),
  p_value INTEGER,
  p_language VARCHAR(32),
  p_start_date VARCHAR(20),
  p_finish_date VARCHAR(20)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_product_type_id INTEGER;
  DECLARE v_type INTEGER;
  DECLARE v_code VARCHAR(8);
  DECLARE v_name VARCHAR(32);
  DECLARE v_value INTEGER;
  DECLARE v_language VARCHAR(32);
  DECLARE v_start_date VARCHAR(20);
  DECLARE v_finish_date VARCHAR(20);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_discount',CONCAT('error: code=-1 username=',p_user,' id=',p_discount_id));
    RETURN -1;
  END IF;

  SELECT product_type_id,type,code,name,value,language,start_date,finish_date INTO v_product_type_id,v_type,v_code,v_name,v_value,v_language,v_start_date,v_finish_date
  FROM discount
  WHERE id=p_discount_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_product_type_id!=-1 THEN SET v_product_type_id=p_product_type_id; END IF;
    IF p_type!=-1 THEN SET v_type=p_type; END IF;
    IF p_code!='' THEN SET v_code=p_code; END IF;
    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_value!=-1 THEN SET v_value=p_value; END IF;
    IF p_language!='' THEN SET v_language=p_language; END IF;
    IF p_start_date!='' THEN SET v_start_date=p_start_date; END IF;
    IF p_finish_date!='' THEN SET v_finish_date=p_finish_date; END IF;

    UPDATE discount SET product_type_id=v_product_type_id,
                        type=v_type,
                        code=v_code,
                        name=v_name,
                        value=v_value,
                        language=v_language,
                        start_date=v_start_date,
                        finish_date=v_finish_date
    WHERE id=p_discount_id;

    CALL add_audit(p_user,'update_discount',CONCAT('success: code=0 username=',p_user,' id=',p_discount_id));

  ELSE
    CALL add_audit(p_user,'update_discount',CONCAT('error: code=-3 username=',p_user,' id=',p_discount_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_discount`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_discount_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_discount',CONCAT('error: code=-1 username=',p_user,' id=',p_discount_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM discount WHERE id=p_discount_id;

    CALL add_audit(p_user,'remove_discount',CONCAT('success: code=0 username=',p_user,' id=',p_discount_id));

  ELSE
    CALL add_audit(p_user,'remove_discount',CONCAT('error: code=-3 username=',p_user,' id=',p_discount_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_manufacture`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_name VARCHAR(32),
  p_description VARCHAR(128)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_manufacture',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO manufacture (name,description)
    VALUES (p_name,p_description);

    SET ret_val=LAST_INSERT_ID();

    CALL add_audit(p_user,'add_manufacture',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_manufacture',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_manufacture`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_manufacture_id INTEGER,
  p_name VARCHAR(32),
  p_description VARCHAR(128)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_name VARCHAR(32);
  DECLARE v_description VARCHAR(128);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_manufacture',CONCAT('error: code=-1 username=',p_user,' id=',p_manufacture_id));
    RETURN -1;
  END IF;

  SELECT name,description INTO v_name,v_description
  FROM manufacture
  WHERE id=p_manufacture_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_description!='' THEN SET v_description=p_description; END IF;

    UPDATE manufacture SET name=v_name,
                           description=v_description
    WHERE id=p_manufacture_id;

    CALL add_audit(p_user,'update_manufacture',CONCAT('success: code=0 username=',p_user,' id=',p_manufacture_id));

  ELSE
    CALL add_audit(p_user,'update_manufacture',CONCAT('error: code=-3 username=',p_user,' id=',p_manufacture_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_manufacture`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_manufacture_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_manufacture',CONCAT('error: code=-1 username=',p_user,' id=',p_manufacture_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM manufacture WHERE id=p_manufacture_id;

    CALL add_audit(p_user,'remove_manufacture',CONCAT('success: code=0 username=',p_user,' id=',p_manufacture_id));

  ELSE
    CALL add_audit(p_user,'remove_manufacture',CONCAT('error: code=-3 username=',p_user,' id=',p_manufacture_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_manufacture_picture`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_manufacture_id INTEGER,
  p_picture MEDIUMBLOB) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_manufacture_picture',CONCAT('error: code=-1 username=',p_user,' id=',p_manufacture_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_picture IS NOT NULL THEN
      UPDATE manufacture SET picture=p_picture
      WHERE id=p_manufacture_id;
      CALL add_audit(p_user,'update_manufacture_picture',CONCAT('success: code=0 username=',p_user,' id=',p_manufacture_id));
    END IF;

  ELSE
    CALL add_audit(p_user,'update_manufacture_picture',CONCAT('error: code=-3 username=',p_user,' id=',p_manufacture_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_attr_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_object_id INTEGER,
  p_object_name VARCHAR(32),
  p_attr_name VARCHAR(32),
  p_attr_value VARCHAR(256),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_attr_part',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO attr_part (object_id,object_name,name,value,language)
    VALUES (p_object_id,p_object_name,p_attr_name,p_attr_value,p_language);

    SET ret_val=LAST_INSERT_ID();

    CALL add_audit(p_user,'add_attr_part',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_attr_part',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_attr_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_attr_part_id INTEGER,
  p_object_id INTEGER,
  p_object_name VARCHAR(32),
  p_attr_name VARCHAR(32),
  p_attr_value VARCHAR(256),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_object_id INTEGER;
  DECLARE v_object_name VARCHAR(64);
  DECLARE v_attr_name VARCHAR(32);
  DECLARE v_attr_value VARCHAR(256);
  DECLARE v_language VARCHAR(32);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_attr_part',CONCAT('error: code=-1 username=',p_user,' id=',p_attr_part_id));
    RETURN -1;
  END IF;

  SELECT object_id,object_name,name,value,language INTO v_object_id,v_object_name,v_attr_name,v_attr_value,v_language
  FROM attr_part
  WHERE id=p_attr_part_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_object_id!=-1 THEN SET v_object_id=p_object_id; END IF;
    IF p_object_name!='' THEN SET v_object_name=p_object_name; END IF;
    IF p_attr_name!='' THEN SET v_attr_name=p_attr_name; END IF;
    IF p_attr_value!='' THEN SET v_attr_value=p_attr_value; END IF;
    IF p_language!='' THEN SET v_language=p_language; END IF;

    UPDATE attr_part SET object_id=v_object_id,
                         object_name=v_object_name,
                         name=v_attr_name,
                         value=v_attr_value,
                         language=v_language
    WHERE id=p_attr_part_id;

    CALL add_audit(p_user,'update_attr_part',CONCAT('success: code=0 username=',p_user,' id=',p_attr_part_id));

  ELSE
    CALL add_audit(p_user,'update_manufacture',CONCAT('error: code=-3 username=',p_user,' id=',p_attr_part_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_attr_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_attr_part_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_manufacture',CONCAT('error: code=-1 username=',p_user,' id=',p_attr_part_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM attr_part WHERE id=p_attr_part_id;

    CALL add_audit(p_user,'remove_manufacture',CONCAT('success: code=0 username=',p_user,' id=',p_attr_part_id));

  ELSE
    CALL add_audit(p_user,'remove_manufacture',CONCAT('error: code=-3 username=',p_user,' id=',p_attr_part_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_product`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_manufacture_id INTEGER,
  p_name VARCHAR(32),
  p_description VARCHAR(128),
  p_code VARCHAR(32),
  p_price VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_product',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_manufacture_id=-1 THEN SET p_manufacture_id=NULL; END IF;

    INSERT INTO product (manufacture_id,name,description,code,price)
    VALUES (p_manufacture_id,p_name,p_description,p_code,p_price);

    SET ret_val=LAST_INSERT_ID();

    CALL add_audit(p_user,'add_product',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_product',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_product`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_product_id INTEGER,
  p_manufacture_id INTEGER,
  p_name VARCHAR(32),
  p_description VARCHAR(128),
  p_code VARCHAR(32),
  p_price VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_manufacture_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_description VARCHAR(128);
  DECLARE v_code VARCHAR(32);
  DECLARE v_price VARCHAR(16);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_product',CONCAT('error: code=-1 username=',p_user,' id=',p_product_id));
    RETURN -1;
  END IF;

  SELECT manufacture_id,name,description,code,price INTO v_manufacture_id,v_name,v_description,v_code,v_price
  FROM product
  WHERE id=p_product_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_manufacture_id!=-1 THEN SET v_manufacture_id=p_manufacture_id; END IF;
    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_description!='' THEN SET v_description=p_description; END IF;
    IF p_code!='' THEN SET v_code=p_code; END IF;
    IF p_price!='' THEN SET v_price=p_price; END IF;

    UPDATE product SET manufacture_id=v_manufacture_id,
                       name=v_name,
                       description=v_description,
                       code=v_code,
                       price=v_price
    WHERE id=p_product_id;

    CALL add_audit(p_user,'update_product',CONCAT('success: code=0 username=',p_user,' id=',p_product_id));

  ELSE
    CALL add_audit(p_user,'update_product',CONCAT('error: code=-3 username=',p_user,' id=',p_product_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_product`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_product_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_product',CONCAT('error: code=-1 username=',p_user,' id=',p_product_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM product WHERE id=p_product_id;

    CALL add_audit(p_user,'remove_product',CONCAT('success: code=0 username=',p_user,' id=',p_product_id));

  ELSE
    CALL add_audit(p_user,'remove_product',CONCAT('error: code=-3 username=',p_user,' id=',p_product_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_product_picture`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_product_id INTEGER,
  p_picture MEDIUMBLOB) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_product_picture',CONCAT('error: code=-1 username=',p_user,' id=',p_product_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_picture IS NOT NULL THEN
      UPDATE product SET picture=p_picture
      WHERE id=p_product_id;
      CALL add_audit(p_user,'update_product_picture',CONCAT('success: code=0 username=',p_user,' id=',p_product_id));
    END IF;

  ELSE
    CALL add_audit(p_user,'update_product_picture',CONCAT('error: code=-3 username=',p_user,' id=',p_product_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_product_discount`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_product_id INTEGER,
  p_discount_code VARCHAR(8)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_product_discount',CONCAT('error: code=-1 username=',p_user,' id=',p_product_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_discount_code='' THEN SET p_discount_code=NULL; END IF;

    UPDATE product SET discount_code=p_discount_code WHERE id=p_product_id;

    CALL add_audit(p_user,'update_product_discount',CONCAT('success: code=0 username=',p_user,' id=',p_product_id));

  ELSE
    CALL add_audit(p_user,'update_product_discount',CONCAT('error: code=-3 username=',p_user,' id=',p_product_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_product_review`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_product_id INTEGER,
  p_review_count INTEGER,
  p_review_value INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_product_review',CONCAT('error: code=-1 username=',p_user,' id=',p_product_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    UPDATE product SET review_count=p_review_count,
                       review_value=p_review_value
	WHERE id=p_product_id;

    CALL add_audit(p_user,'update_product_review',CONCAT('success: code=0 username=',p_user,' id=',p_product_id));

  ELSE
    CALL add_audit(p_user,'update_product_review',CONCAT('error: code=-3 username=',p_user,' id=',p_product_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_product_review`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_product_id INTEGER,
  p_description VARCHAR(128),
  p_value INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_review_count INTEGER;
  DECLARE v_review_value INTEGER;

  DECLARE v_count INTEGER;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_product_review',CONCAT('error: code=-1 username=',p_user,' id=',p_product_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF p_user_id=-1 THEN SET p_user_id=v_user_id; END IF;

  SELECT count(*) INTO v_count
  FROM product_review
  WHERE user_id=p_user_id AND product_id=p_product_id;

  IF v_count>0 THEN
    CALL add_audit(p_user,'add_product_review',CONCAT('error: code=-4 username=',p_user,' id=',p_product_id,' value=',p_value));
    RETURN -4;
  END IF;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR v_user_id=p_user_id THEN

    SELECT review_count,review_value INTO v_review_count,v_review_value
    FROM product
    WHERE id=p_product_id;

    SET v_review_count=v_review_count+1;
    SET v_review_value=v_review_value+p_value;

    INSERT INTO product_review (user_id,product_id,description,value)
    VALUES (p_user_id,p_product_id,p_description,p_value);

    UPDATE product SET review_count=v_review_count,
                       review_value=v_review_value
    WHERE id=p_product_id;

    CALL add_audit(p_user,'add_product_review',CONCAT('success: code=0 username=',p_user,' id=',p_product_id));

  ELSE
    CALL add_audit(p_user,'add_product_review',CONCAT('error: code=-3 username=',p_user,' id=',p_product_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_product_review`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_product_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_review_count INTEGER;
  DECLARE v_review_value INTEGER;

  DECLARE v_value INTEGER;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_product_review',CONCAT('error: code=-1 username=',p_user,' id=',p_product_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF p_user_id=-1 THEN SET p_user_id=v_user_id; END IF;

  SELECT value INTO v_value
  FROM product_review
  WHERE user_id=p_user_id AND product_id=p_product_id;

  IF v_value IS NULL THEN
    CALL add_audit(p_user,'remove_product_review',CONCAT('error: code=-4 username=',p_user,' id=',p_product_id));
    RETURN -4;
  END IF;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR v_user_id=p_user_id THEN

    SELECT review_count,review_value INTO v_review_count,v_review_value
    FROM product
    WHERE id=p_product_id;

    SET v_review_count=v_review_count-1;
    SET v_review_value=v_review_value-v_value;

    DELETE FROM product_review
    WHERE user_id=p_user_id AND product_id=p_product_id;

    UPDATE product SET review_count=v_review_count,
                       review_value=v_review_value
    WHERE id=p_product_id;

    CALL add_audit(p_user,'remove_product_review',CONCAT('success: code=0 username=',p_user,' id=',p_product_id));

  ELSE
    CALL add_audit(p_user,'remove_product_review',CONCAT('error: code=-1 username=',p_user,' id=',p_product_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_product_circle`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_productA_id INTEGER,
  p_productB_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_product_circle',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO product_circle (productA_id,productB_id)
    VALUES (p_productA_id,p_productB_id);

    CALL add_audit(p_user,'add_product_circle',CONCAT('success: code=0 username=',p_user,' productA_id=',p_productA_id,' productB_id=',p_productB_id));

  ELSE
    CALL add_audit(p_user,'add_product_circle',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_product_circle`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_productA_id INTEGER,
  p_productB_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_product_circle',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM product_circle WHERE productA_id=p_productA_id AND productB_id=p_productB_id;

    CALL add_audit(p_user,'remove_product_circle',CONCAT('success: code=0 username=',p_user,' productA_id=',p_productA_id,' productB_id=',p_productB_id));

  ELSE
    CALL add_audit(p_user,'remove_product_circle',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_product_param`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_parent_id INTEGER,
  p_name VARCHAR(32),
  p_value VARCHAR(64),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_product_param',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_parent_id=-1 THEN SET p_parent_id=NULL; END IF;

    INSERT INTO product_param (parent_id,name,value,language)
    VALUES (p_parent_id,p_name,p_value,p_language);

    SET ret_val=LAST_INSERT_ID();

    CALL add_audit(p_user,'add_product_param',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_product_param',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_product_param`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_param_id INTEGER,
  p_parent_id INTEGER,
  p_name VARCHAR(32),
  p_value VARCHAR(64),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_parent_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_value VARCHAR(64);
  DECLARE v_language VARCHAR(32);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_product_param',CONCAT('error: code=-1 username=',p_user,' id=',p_param_id));
    RETURN -1;
  END IF;

  SELECT parent_id,name,value,language INTO v_parent_id,v_name,v_value,v_language
  FROM product_param
  WHERE id=p_param_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_parent_id!=-1 THEN SET v_parent_id=p_parent_id; END IF;
    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_value!='' THEN SET v_value=p_value; END IF;
    IF p_language!='' THEN SET v_language=p_language; END IF;

    UPDATE product_param SET parent_id=v_parent_id,
                             name=v_name,
                             value=v_value,
                             language=v_language
    WHERE id=p_param_id;

    CALL add_audit(p_user,'update_product_param',CONCAT('success: code=0 username=',p_user,' id=',p_param_id));

  ELSE
    CALL add_audit(p_user,'update_product_param',CONCAT('error: code=-3 username=',p_user,' id=',p_param_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_product_param`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_param_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_product_param',CONCAT('error: code=-1 username=',p_user,' id=',p_param_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM product_param WHERE id=p_param_id;

    CALL add_audit(p_user,'remove_product_param',CONCAT('success: code=0 username=',p_user,' id=',p_param_id));

  ELSE
    CALL add_audit(p_user,'remove_product_param',CONCAT('error: code=-3 username=',p_user,' id=',p_param_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_product_param_picture`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_param_id INTEGER,
  p_picture MEDIUMBLOB) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_product_param_picture',CONCAT('error: code=-1 username=',p_user,' id=',p_param_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_picture IS NOT NULL THEN
      UPDATE product_param SET picture=p_picture
      WHERE id=p_param_id;
      CALL add_audit(p_user,'update_product_param_picture',CONCAT('success: code=0 username=',p_user,' id=',p_param_id));
    END IF;

  ELSE
    CALL add_audit(p_user,'update_product_param_picture',CONCAT('error: code=-3 username=',p_user,' id=',p_param_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_product_type`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_parent_id INTEGER,
  p_name VARCHAR(32),
  p_description VARCHAR(128),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_product_type',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_parent_id=-1 THEN SET p_parent_id=NULL; END IF;

    INSERT INTO product_type (parent_id,name,description,language)
    VALUES (p_parent_id,p_name,p_description,p_language);

    SET ret_val=LAST_INSERT_ID();
    
    CALL add_audit(p_user,'add_product_type',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_product_type',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_product_type`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_type_id INTEGER,
  p_parent_id INTEGER,
  p_name VARCHAR(32),
  p_description VARCHAR(128),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_parent_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_description VARCHAR(128);
  DECLARE v_language VARCHAR(32);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_product_type',CONCAT('error: code=-1 username=',p_user,' id=',p_type_id));
    RETURN -1;
  END IF;

  SELECT parent_id,name,description,language INTO v_parent_id,v_name,v_description,v_language
  FROM product_type
  WHERE id=p_type_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_parent_id!=-1 THEN SET v_parent_id=p_parent_id; END IF;
    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_description!='' THEN SET v_description=p_description; END IF;
    IF p_language!='' THEN SET v_language=p_language; END IF;

    UPDATE product_type SET parent_id=v_parent_id,
                            name=v_name,
                            description=v_description,
                            language=v_language
    WHERE id=p_type_id;
    
    CALL add_audit(p_user,'update_product_type',CONCAT('success: code=0 username=',p_user,' id=',p_type_id));

  ELSE
    CALL add_audit(p_user,'update_product_type',CONCAT('error: code=-3 username=',p_user,' id=',p_type_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_product_type`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_type_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_product_type',CONCAT('error: code=-1 username=',p_user,' id=',p_type_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM product_type WHERE id=p_type_id;
    
    CALL add_audit(p_user,'remove_product_type',CONCAT('success: code=0 username=',p_user,' id=',p_type_id));

  ELSE
    CALL add_audit(p_user,'remove_product_type',CONCAT('error: code=-3 username=',p_user,' id=',p_type_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_product_param_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_product_id INTEGER,
  p_param_id INTEGER,
  p_value VARCHAR(64),
  p_price VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_product_param_part',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO product_param_part (product_id,product_param_id,value,price)
    VALUES (p_product_id,p_param_id,p_value,p_price);
    
    CALL add_audit(p_user,'add_product_param_part',CONCAT('success: code=0 username=',p_user,' product_id=',p_product_id,' param_id=',p_param_id));

  ELSE
    CALL add_audit(p_user,'add_product_param_part',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_product_param_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_product_id INTEGER,
  p_param_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_product_param',CONCAT('error: code=-1 username=',p_user,' product_id=',p_product_id,' param_id=',p_param_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM product_param_part WHERE product_id=p_product_id AND product_param_id=p_param_id;
    
    CALL add_audit(p_user,'remove_product_param_part',CONCAT('success: code=0 username=',p_user,' product_id=',p_product_id,' param_id=',p_param_id));

  ELSE
    CALL add_audit(p_user,'remove_product_param_part',CONCAT('error: code=-3 username=',p_user,' product_id=',p_product_id,' param_id=',p_param_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_product_param_part_picture`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_product_id INTEGER,
  p_param_id INTEGER,
  p_picture MEDIUMBLOB) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_product_param_picture',CONCAT('error: code=-1 username=',p_user,' product_id=',p_product_id,' param_id=',p_param_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_picture IS NOT NULL THEN
      UPDATE product_param_part SET picture=p_picture
      WHERE product_id=p_product_id AND product_param_id=p_param_id;
      CALL add_audit(p_user,'update_product_param_picture',CONCAT('success: code=0 username=',p_user,' product_id=',p_product_id,' param_id=',p_param_id));
    END IF;

  ELSE
    CALL add_audit(p_user,'update_product_param_picture',CONCAT('error: code=-3 username=',p_user,' product_id=',p_product_id,' param_id=',p_param_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_product_type_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_product_id INTEGER,
  p_type_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_product_type_part',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO product_type_part (product_id,product_type_id)
    VALUES (p_product_id,p_type_id);
    
    CALL add_audit(p_user,'add_product_type_part',CONCAT('success: code=0 username=',p_user,' product_id=',p_product_id,' type_id=',p_type_id));

  ELSE
    CALL add_audit(p_user,'add_product_type_part',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_product_type_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_product_id INTEGER,
  p_type_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_product_type_part',CONCAT('error: code=-1 username=',p_user,' product_id=',p_product_id,' type_id=',p_type_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM product_type_part WHERE product_id=p_product_id AND product_type_id=p_type_id;
    
    CALL add_audit(p_user,'remove_product_type_part',CONCAT('success: code=0 username=',p_user,' product_id=',p_product_id,' type_id=',p_type_id));

  ELSE
    CALL add_audit(p_user,'remove_product_type_part',CONCAT('error: code=-3 username=',p_user,' product_id=',p_product_id,' type_id=',p_type_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_purchase`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_invoice_code VARCHAR(32),
  p_invoice_date VARCHAR(20),
  p_total_price VARCHAR(16),
  p_total_tax VARCHAR(10),
  p_order_id INTEGER,
  p_order_date VARCHAR(20),
  p_order_info VARCHAR(128),
  p_delivery_id INTEGER,
  p_delivery_code VARCHAR(32),
  p_delivery_type_id INTEGER,
  p_delivery_date VARCHAR(20),
  p_delivery_price VARCHAR(16),
  p_payment_date VARCHAR(20),
  p_payment_info VARCHAR(128),
  p_payment_amount VARCHAR(10)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);
  DECLARE v_count INTEGER;

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_purchase',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  SELECT count(*) INTO v_count
  FROM purchase
  WHERE order_id=p_order_id AND user_id=p_user_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     ((v_user='fa_client' AND v_user_type=1) OR 
      (v_user='fa_client' AND v_user_type=2 AND is_driver_of_order(p_user,p_order_id)=1 AND v_count<1)) THEN

    IF p_invoice_date='' THEN SET p_invoice_date=NULL; END IF;
    IF p_order_id=-1 THEN SET p_order_id=NULL; END IF;
    IF p_order_date='' THEN SET p_order_date=NULL; END IF;
    IF p_delivery_id=-1 THEN SET p_delivery_id=NULL; END IF;
    IF p_delivery_date='' THEN SET p_delivery_date=NULL; END IF;
    IF p_delivery_price='' THEN SET p_delivery_price=NULL; END IF;
    IF p_delivery_type_id=-1 THEN SET p_delivery_type_id=NULL; END IF;
    IF p_payment_date='' THEN SET p_payment_date=NULL; END IF;
    IF p_payment_amount='' THEN SET p_payment_amount=NULL; END IF;

    INSERT INTO purchase (user_id,invoice_code,invoice_date,total_price,total_tax,
                          order_id,order_date,order_info,
                          delivery_id,delivery_code,delivery_type_id,delivery_date,delivery_price,
                          payment_date,payment_info,payment_amount)
    VALUES (p_user_id,p_invoice_code,p_invoice_date,p_total_price,p_total_tax,
            p_order_id,p_order_date,p_order_info,
            p_delivery_id,p_delivery_code,p_delivery_type_id,p_delivery_date,p_delivery_price,
            p_payment_date,p_payment_info,p_payment_amount);

    SET ret_val=LAST_INSERT_ID();
    
    CALL add_audit(p_user,'add_purchase',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_purchase',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_purchase`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_purchase_id INTEGER,
  p_user_id INTEGER,
  p_invoice_code VARCHAR(32),
  p_invoice_date VARCHAR(20),
  p_total_price VARCHAR(16),
  p_total_tax VARCHAR(10),
  p_order_id INTEGER,
  p_order_date VARCHAR(20),
  p_order_info VARCHAR(128),
  p_delivery_id INTEGER,
  p_delivery_code VARCHAR(32),
  p_delivery_type_id INTEGER,
  p_delivery_date VARCHAR(20),
  p_delivery_price VARCHAR(16),
  p_payment_date VARCHAR(20),
  p_payment_info VARCHAR(128),
  p_payment_amount VARCHAR(10)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_invoice_code VARCHAR(32);
  DECLARE v_invoice_date VARCHAR(20);
  DECLARE v_total_price VARCHAR(16);
  DECLARE v_total_tax VARCHAR(10);
  DECLARE v_order_id INTEGER;
  DECLARE v_order_date VARCHAR(20);
  DECLARE v_order_info VARCHAR(128);
  DECLARE v_delivery_id INTEGER;
  DECLARE v_delivery_code VARCHAR(32);
  DECLARE v_delivery_type_id INTEGER;
  DECLARE v_delivery_date VARCHAR(20);
  DECLARE v_delivery_price VARCHAR(16);
  DECLARE v_payment_date VARCHAR(20);
  DECLARE v_payment_info VARCHAR(128);
  DECLARE v_payment_amount VARCHAR(10);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_purchase',CONCAT('error: code=-1 username=',p_user,' id=',p_purchase_id));
    RETURN -1;
  END IF;

  SELECT user_id,invoice_code,invoice_date,total_price,total_tax,
		 order_id,order_date,order_info,
		 delivery_id,delivery_code,delivery_type_id,delivery_date,delivery_price,
		 payment_date,payment_info,payment_amount
  INTO v_user_id,v_invoice_code,v_invoice_date,v_total_price,v_total_tax,
       v_order_id,v_order_date,v_order_info,
       v_delivery_id,v_delivery_code,v_delivery_type_id,v_delivery_date,v_delivery_price,
       v_payment_date,v_payment_info,v_payment_amount
  FROM purchase
  WHERE id=p_purchase_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_user_id!=-1 THEN SET v_user_id=p_user_id; END IF;
    IF p_invoice_code!='' THEN SET v_invoice_code=p_invoice_code; END IF;
    IF p_invoice_date!='' THEN SET v_invoice_date=p_invoice_date; END IF;
    IF p_total_price!='' THEN SET v_total_price=p_total_price; END IF;
    IF p_total_tax!='' THEN SET v_total_tax=p_total_tax; END IF;
    IF p_order_id!=-1 THEN SET v_order_id=p_order_id; END IF;
    IF p_order_date!='' THEN SET v_order_date=p_order_date; END IF;
    IF p_order_info!='' THEN SET v_order_info=p_order_info; END IF;
    IF p_delivery_id!=-1 THEN SET v_delivery_id=p_delivery_id; END IF;
    IF p_delivery_code!='' THEN SET v_delivery_code=p_delivery_code; END IF;
    IF p_delivery_type_id!=-1 THEN SET v_delivery_type_id=p_delivery_type_id; END IF;
    IF p_delivery_date!='' THEN SET v_delivery_date=p_delivery_date; END IF;
    IF p_delivery_price!='' THEN SET v_delivery_price=p_delivery_price; END IF;
    IF p_payment_date!='' THEN SET v_payment_date=p_payment_date; END IF;
    IF p_payment_info!='' THEN SET v_payment_info=p_payment_info; END IF;
    IF p_payment_amount!='' THEN SET v_payment_amount=p_payment_amount; END IF;

    UPDATE purchase SET
           user_id=v_user_id,
           invoice_code=v_invoice_code,
           invoice_date=v_invoice_date,
           total_price=v_total_price,
           total_tax=v_total_tax,
           order_id=v_order_id,
           order_date=v_order_date,
           order_info=v_order_info,
           delivery_id=v_delivery_id,
           delivery_code=v_delivery_code,
           delivery_type_id=v_delivery_type_id,
           delivery_date=v_delivery_date,
           delivery_price=v_delivery_price,
           payment_date=v_payment_date,
           payment_info=v_payment_info,
           payment_amount=v_payment_amount
    WHERE id=p_purchase_id;
    
    CALL add_audit(p_user,'update_purchase',CONCAT('success: code=0 username=',p_user,' id=',p_purchase_id));

  ELSE
    CALL add_audit(p_user,'update_purchase',CONCAT('error: code=-3 username=',p_user,' id=',p_purchase_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_purchase`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_purchase_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_purchase',CONCAT('error: code=-1 username=',p_user,' id=',p_purchase_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN
    
    DELETE FROM purchase WHERE id=p_purchase_id;
    
    CALL add_audit(p_user,'remove_purchase',CONCAT('success: code=0 username=',p_user,' id=',p_purchase_id));

  ELSE
    CALL add_audit(p_user,'remove_purchase',CONCAT('error: code=-3 username=',p_user,' id=',p_purchase_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_payment`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_purchase_id INTEGER,
  p_amount VARCHAR(10),
  p_currency VARCHAR(3),
  p_description VARCHAR(128),
  p_status VARCHAR(20),
  p_transaction_id VARCHAR(128),
  p_phone VARCHAR(20)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_payment',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_amount='' THEN SET p_amount=0; END IF;

    INSERT INTO payment (purchase_id,amount,currency,description,status,transaction_id,phone)
    VALUES (p_purchase_id,p_amount,p_currency,p_description,p_status,p_transaction_id,p_phone);
    
    CALL add_audit(p_user,'add_payment',CONCAT('success: code=0 username=',p_user));

  ELSE
    CALL add_audit(p_user,'add_payment',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_payment`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_purchase_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_payment',CONCAT('error: code=-1 username=',p_user,' id=',p_purchase_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM payment WHERE purchase_id=p_purchase_id;
    
    CALL add_audit(p_user,'remove_payment',CONCAT('success: code=0 username=',p_user,' id=',p_purchase_id));

  ELSE
    CALL add_audit(p_user,'remove_payment',CONCAT('error: code=-3 username=',p_user,' id=',p_purchase_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_currency`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_name VARCHAR(3),
  p_description VARCHAR(128),
  p_value VARCHAR(10),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_currency',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO currency (name,description,value,language)
    VALUES (p_name,p_description,p_value,p_language);

    SET ret_val=LAST_INSERT_ID();
    
    CALL add_audit(p_user,'add_currency',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_currency',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_currency`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_currency_id INTEGER,
  p_name VARCHAR(3),
  p_description VARCHAR(128),
  p_value VARCHAR(10),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_name VARCHAR(3);
  DECLARE v_description VARCHAR(128);
  DECLARE v_value VARCHAR(10);
  DECLARE v_language VARCHAR(32);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_currency',CONCAT('error: code=-1 username=',p_user,' id=',p_currency_id));
    RETURN -1;
  END IF;

  SELECT name,description,value,language INTO v_name,v_description,v_value,v_language
  FROM currency
  WHERE id=p_currency_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_description!='' THEN SET v_description=p_description; END IF;
    IF p_value!='' THEN SET v_value=p_value; END IF;
    IF p_language!='' THEN SET v_language=p_language; END IF;

    UPDATE currency SET name=v_name,
                        description=v_description,
                        value=v_value,
                        language=v_language
    WHERE id=p_currency_id;
    
    CALL add_audit(p_user,'update_currency',CONCAT('success: code=0 username=',p_user,' id=',p_currency_id));

  ELSE
    CALL add_audit(p_user,'update_currency',CONCAT('error: code=-3 username=',p_user,' id=',p_currency_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_currency`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_currency_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_currency',CONCAT('error: code=-1 username=',p_user,' id=',p_currency_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM currency WHERE id=p_currency_id;

    CALL add_audit(p_user,'remove_currency',CONCAT('success: code=0 username=',p_user,' id=',p_currency_id));

  ELSE
    CALL add_audit(p_user,'remove_currency',CONCAT('error: code=-3 username=',p_user,' id=',p_currency_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_currency_activity`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_currency_id INTEGER,
  p_activity TINYINT(1)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_currency_activity',CONCAT('error: code=-1 username=',p_user,' id=',p_currency_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    UPDATE currency SET active=p_activity WHERE id=p_currency_id;
    
    CALL add_audit(p_user,'update_currency_activity',CONCAT('success: code=0 username=',p_user,' id=',p_currency_id,' active=',p_activity));

  ELSE
    CALL add_audit(p_user,'update_currency_activity',CONCAT('error: code=-3 username=',p_user,' id=',p_currency_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_attr`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_attr_name VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_attr',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO attr (name) VALUES (p_attr_name);
    
    CALL add_audit(p_user,'add_attr',CONCAT('success: code=0 username=',p_user,' name=',p_attr_name));

  ELSE
    CALL add_audit(p_user,'add_attr',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_attr`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_attr_name VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_attr',CONCAT('error: code=-1 username=',p_user,' name=',p_attr_name));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM attr WHERE name=p_attr_name;
    
    CALL add_audit(p_user,'remove_attr',CONCAT('success: code=0 username=',p_user,' name=',p_attr_name));

  ELSE
    CALL add_audit(p_user,'remove_attr',CONCAT('error: code=-3 username=',p_user,' name=',p_attr_name));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_color`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_color_name VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_color',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO color (name) VALUES (p_color_name);
    
    CALL add_audit(p_user,'add_color',CONCAT('success: code=0 username=',p_user,' name=',p_color_name));

  ELSE
    CALL add_audit(p_user,'add_color',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_color`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_color_name VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_color',CONCAT('error: code=-1 username=',p_user,' name=',p_color_name));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM color WHERE name=p_color_name;
    
    CALL add_audit(p_user,'remove_color',CONCAT('success: code=0 username=',p_user,' name=',p_color_name));

  ELSE
    CALL add_audit(p_user,'remove_color',CONCAT('error: code=-3 username=',p_user,' name=',p_color_name));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_language`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_language_id INTEGER,
  p_name VARCHAR(32),
  p_value VARCHAR(32),
  p_code VARCHAR(5)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_language',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO language (id,name,value,code) VALUES (p_language_id,p_name,p_value,p_code);
    
    CALL add_audit(p_user,'add_language',CONCAT('success: code=0 username=',p_user,' id=',p_language_id,' name=',p_name));

  ELSE
    CALL add_audit(p_user,'add_language',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_language`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_language_id INTEGER,
  p_name VARCHAR(32),
  p_value VARCHAR(32),
  p_code VARCHAR(5)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_name VARCHAR(32);
  DECLARE v_value VARCHAR(32);
  DECLARE v_code VARCHAR(5);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_language',CONCAT('error: code=-1 username=',p_user,' id=',p_language_id));
    RETURN -1;
  END IF;

  SELECT name,value,code INTO v_name,v_value,v_code
  FROM language
  WHERE id=p_language_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_value!='' THEN SET v_value=p_value; END IF;
    IF p_code!='' THEN SET v_code=p_code; END IF;

    UPDATE language SET name=p_name,
                        value=p_value,
                        code=p_code
    WHERE id=p_language_id;
    
    CALL add_audit(p_user,'update_language',CONCAT('success: code=0 username=',p_user,' id=',p_language_id));

  ELSE
    CALL add_audit(p_user,'update_language',CONCAT('error: code=-3 username=',p_user,' id=',p_language_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_language`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_language_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_language',CONCAT('error: code=-1 username=',p_user,' id=',p_language_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM language WHERE id=p_language_id;
    
    CALL add_audit(p_user,'remove_language',CONCAT('success: code=0 username=',p_user,' id=',p_language_id));

  ELSE
    CALL add_audit(p_user,'remove_language',CONCAT('error: code=-3 username=',p_user,' id=',p_language_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_language_activity`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_language_id INTEGER,
  p_activity TINYINT(1)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_language_activity',CONCAT('error: code=-1 username=',p_user,' id=',p_language_id));  
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    UPDATE language SET active=p_activity WHERE id=p_language_id;
    
    CALL add_audit(p_user,'update_language_activity',CONCAT('success: code=0 username=',p_user,' id=',p_language_id,' active=',p_activity));

  ELSE
    CALL add_audit(p_user,'update_language_activity',CONCAT('error: code=-3 username=',p_user,' id=',p_language_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_delivery_type`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_name VARCHAR(32),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_delivery_type',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO delivery_type (name,language) VALUES (p_name,p_language);

    SET ret_val=LAST_INSERT_ID();
    
    CALL add_audit(p_user,'add_delivery_type',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_delivery_type',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_delivery_type`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_type_id INTEGER,
  p_name VARCHAR(32),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_name VARCHAR(32);
  DECLARE v_language VARCHAR(32);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_delivery_type',CONCAT('error: code=-1 username=',p_user,' id=',p_type_id));
    RETURN -1;
  END IF;

  SELECT name,language INTO v_name,v_language
  FROM delivery_type
  WHERE id=p_type_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_language!='' THEN SET v_language=p_language; END IF;

    UPDATE delivery_type SET name=v_name,
                             language=v_language
    WHERE id=p_type_id;

    CALL add_audit(p_user,'update_delivery_type',CONCAT('success: code=0 username=',p_user,' id=',p_type_id));

  ELSE
    CALL add_audit(p_user,'update_delivery_type',CONCAT('error: code=-3 username=',p_user,' id=',p_type_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_delivery_type`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_type_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_delivery_type',CONCAT('error: code=-1 username=',p_user,' id=',p_type_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM delivery_type WHERE id=p_type_id;
    
    CALL add_audit(p_user,'remove_delivery_type',CONCAT('success: code=0 username=',p_user,' id=',p_type_id));

  ELSE
    CALL add_audit(p_user,'remove_delivery_type',CONCAT('error: code=-3 username=',p_user,' id=',p_type_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

-- status_id defined by admin (negotive values for purchase canceling and positive for payment,shipping)

DELIMITER $$

CREATE FUNCTION `add_order_status`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_status_id INTEGER,
  p_name VARCHAR(32),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_order_status',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO order_status (id,name,language) VALUES (p_status_id,p_name,p_language);
    
    CALL add_audit(p_user,'add_order_status',CONCAT('success: code=0 username=',p_user,' id=',p_status_id,' name=',p_name));

  ELSE
    CALL add_audit(p_user,'add_order_status',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_order_status`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_status_id INTEGER,
  p_name VARCHAR(32),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_name VARCHAR(32);
  DECLARE v_language VARCHAR(32);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_order_status',CONCAT('error: code=-1 username=',p_user,' id=',p_status_id));
    RETURN -1;
  END IF;

  SELECT name,language INTO v_name,v_language
  FROM order_status
  WHERE id=p_status_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_language!='' THEN SET v_language=p_language; END IF;

    UPDATE order_status SET name=v_name,
                            language=v_language
    WHERE id=p_status_id;
    
    CALL add_audit(p_user,'update_order_status',CONCAT('success: code=0 username=',p_user,' id=',p_status_id));

  ELSE
    CALL add_audit(p_user,'update_order_status',CONCAT('error: code=-3 username=',p_user,' id=',p_status_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_order_status`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_status_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_order_status',CONCAT('error: code=-1 username=',p_user,' id=',p_status_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM order_status WHERE id=p_status_id;
    
    CALL add_audit(p_user,'remove_order_status',CONCAT('success: code=0 username=',p_user,' id=',p_status_id));

  ELSE
    CALL add_audit(p_user,'remove_order_status',CONCAT('error: code=-3 username=',p_user,' id=',p_status_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_pickup_status`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_status_id INTEGER,
  p_name VARCHAR(32),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_pickup_status',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO pickup_status (id,name,language) VALUES (p_status_id,p_name,p_language);
    
    CALL add_audit(p_user,'add_pickup_status',CONCAT('success: code=0 username=',p_user,' id=',p_status_id,' name=',p_name));

  ELSE
    CALL add_audit(p_user,'add_pickup_status',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_pickup_status`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_status_id INTEGER,
  p_name VARCHAR(32),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_name VARCHAR(32);
  DECLARE v_language VARCHAR(32);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_pickup_status',CONCAT('error: code=-1 username=',p_user,' id=',p_status_id));
    RETURN -1;
  END IF;

  SELECT name,language INTO v_name,v_language
  FROM order_status
  WHERE id=p_status_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_language!='' THEN SET v_language=p_language; END IF;

    UPDATE pickup_status SET name=v_name,
                             language=v_language
    WHERE id=p_status_id;
    
    CALL add_audit(p_user,'update_pickup_status',CONCAT('success: code=0 username=',p_user,' id=',p_status_id));

  ELSE
    CALL add_audit(p_user,'update_pickup_status',CONCAT('error: code=-3 username=',p_user,' id=',p_status_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_pickup_status`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_status_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_pickup_status',CONCAT('error: code=-1 username=',p_user,' id=',p_status_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM pickup_status WHERE id=p_status_id;
    
    CALL add_audit(p_user,'remove_pickup_status',CONCAT('success: code=0 username=',p_user,' id=',p_status_id));

  ELSE
    CALL add_audit(p_user,'remove_pickup_status',CONCAT('error: code=-3 username=',p_user,' id=',p_status_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_tax`(
  p_user VARCHAR(64),  
  p_pass VARCHAR(16),
  p_code VARCHAR(32),
  p_name VARCHAR(32),
  p_value VARCHAR(10),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_tax',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO tax (code,name,value,language) VALUES (p_code,p_name,p_value,p_language);

    SET ret_val=LAST_INSERT_ID();
    
    CALL add_audit(p_user,'add_tax',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_tax',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_tax`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),  
  p_tax_id INTEGER,
  p_code VARCHAR(32),
  p_name VARCHAR(32),
  p_value VARCHAR(10),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_code VARCHAR(32);
  DECLARE v_name VARCHAR(32);
  DECLARE v_value VARCHAR(10);
  DECLARE v_language VARCHAR(32);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_tax',CONCAT('error: code=-1 username=',p_user,' id=',p_tax_id));
    RETURN -1;
  END IF;

  SELECT code,name,value,language INTO v_code,v_name,v_value,v_language
  FROM tax
  WHERE id=p_tax_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_code!='' THEN SET v_code=p_code; END IF;
    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_value!='' THEN SET v_value=p_value; END IF;
    IF p_language!='' THEN SET v_language=p_language; END IF;

    UPDATE tax SET code=v_code,
                   name=v_name,
                   value=v_value,
                   language=v_language
    WHERE id=p_tax_id;
    
    CALL add_audit(p_user,'update_tax',CONCAT('success: code=0 username=',p_user,' id=',p_tax_id));

  ELSE
    CALL add_audit(p_user,'update_tax',CONCAT('error: code=-3 username=',p_user,' id=',p_tax_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_tax`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_tax_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_tax',CONCAT('error: code=-1 username=',p_user,' id=',p_tax_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM tax WHERE id=p_tax_id;
    
    CALL add_audit(p_user,'remove_tax',CONCAT('success: code=0 username=',p_user,' id=',p_tax_id));

  ELSE
    CALL add_audit(p_user,'remove_tax',CONCAT('error: code=-3 username=',p_user,' id=',p_tax_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_store`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_name VARCHAR(128),
  p_description VARCHAR(128)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_store',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO store (name,description) VALUES (p_name,p_description);

    SET ret_val=LAST_INSERT_ID();
    
    CALL add_audit(p_user,'add_store',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_store',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_store`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_store_id INTEGER,
  p_name VARCHAR(128),
  p_description VARCHAR(128)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_name VARCHAR(128);
  DECLARE v_description VARCHAR(128);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_store',CONCAT('error: code=-1 username=',p_user,' id=',p_store_id));
    RETURN -1;
  END IF;

  SELECT name,description INTO v_name,v_description
  FROM store
  WHERE id=p_store_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_description!='' THEN SET v_description=p_description; END IF;

    UPDATE store SET name=v_name,
                     description=v_description
    WHERE id=p_store_id;
    
    CALL add_audit(p_user,'update_store',CONCAT('success: code=0 username=',p_user,' id=',p_store_id));

  ELSE
    CALL add_audit(p_user,'update_store',CONCAT('error: code=-3 username=',p_user,' id=',p_store_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_store`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_store_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_store',CONCAT('error: code=-1 username=',p_user,' id=',p_store_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM store WHERE id=p_store_id;
    
    CALL add_audit(p_user,'remove_store',CONCAT('success: code=0 username=',p_user,' id=',p_store_id));

  ELSE
    CALL add_audit(p_user,'remove_store',CONCAT('error: code=-3 username=',p_user,' id=',p_store_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_store_activity`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_store_id INTEGER,
  p_activity TINYINT(1)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_store_activity',CONCAT('error: code=-1 username=',p_user,' id=',p_store_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    UPDATE store SET active=p_activity WHERE id=p_store_id;
    
    CALL add_audit(p_user,'update_store_activity',CONCAT('success: code=0 username=',p_user,' id=',p_store_id,' active=',p_activity));

  ELSE
    CALL add_audit(p_user,'update_store_activity',CONCAT('error: code=-3 username=',p_user,' id=',p_store_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_store_picture`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_store_id INTEGER,
  p_picture MEDIUMBLOB) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_store_picture',CONCAT('error: code=-1 username=',p_user,' id=',p_store_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_picture IS NOT NULL THEN
      UPDATE store SET picture=p_picture WHERE id=p_store_id;
      CALL add_audit(p_user,'update_store_picture',CONCAT('success: code=0 username=',p_user,' id=',p_store_id));
    END IF;

  ELSE
    CALL add_audit(p_user,'update_store_picture',CONCAT('error: code=-3 username=',p_user,' id=',p_store_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_store_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_store_id INTEGER,
  p_name VARCHAR(32),
  p_description VARCHAR(128),
  p_latitude VARCHAR(20),
  p_longitude VARCHAR(20),
  p_email VARCHAR(64),
  p_phone1 VARCHAR(20),
  p_phone2 VARCHAR(20),
  p_address VARCHAR(128),
  p_city VARCHAR(64),
  p_postcode VARCHAR(12),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_store_part',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_latitude='' THEN SET p_latitude=NULL; END IF;
    IF p_longitude='' THEN SET p_longitude=NULL; END IF;

    INSERT INTO store_part (store_id,name,description,latitude,longitude,email,phone1,phone2,address,city,postcode,language)
    VALUES (p_store_id,p_name,p_description,p_latitude,p_longitude,p_email,p_phone1,p_phone2,p_address,p_city,p_postcode,p_language);
    
    SET ret_val=LAST_INSERT_ID();
    
    CALL add_audit(p_user,'add_store_part',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_store_part',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_store_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_part_id INTEGER,
  p_store_id INTEGER,
  p_name VARCHAR(32),
  p_description VARCHAR(128),
  p_latitude VARCHAR(20),
  p_longitude VARCHAR(20),
  p_email VARCHAR(64),
  p_phone1 VARCHAR(20),
  p_phone2 VARCHAR(20),
  p_address VARCHAR(128),
  p_city VARCHAR(64),
  p_postcode VARCHAR(12),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_store_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_description VARCHAR(128);
  DECLARE v_latitude VARCHAR(20);
  DECLARE v_longitude VARCHAR(20);
  DECLARE v_email VARCHAR(64);
  DECLARE v_phone1 VARCHAR(20);
  DECLARE v_phone2 VARCHAR(20);
  DECLARE v_address VARCHAR(128);
  DECLARE v_city VARCHAR(64);
  DECLARE v_postcode VARCHAR(12);
  DECLARE v_language VARCHAR(32);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_store_part',CONCAT('error: code=-1 username=',p_user,' id=',p_part_id));
    RETURN -1;
  END IF;

  SELECT store_id,name,description,latitude,longitude,email,phone1,phone2,address,city,postcode,language
  INTO v_store_id,v_name,v_description,v_latitude,v_longitude,v_email,v_phone1,v_phone2,v_address,v_city,v_postcode,v_language
  FROM store_part
  WHERE id=p_part_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_store_id!=-1 THEN SET v_store_id=p_store_id; END IF;
    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_description!='' THEN SET v_description=p_description; END IF;
    IF p_latitude!='' THEN SET v_latitude=p_latitude; END IF;
    IF p_longitude!='' THEN SET v_longitude=p_longitude; END IF;
    IF p_email!='' THEN SET v_email=p_email; END IF;
    IF p_phone1!='' THEN SET v_phone1=p_phone1; END IF;
    IF p_phone2!='' THEN SET v_phone2=p_phone2; END IF;
    IF p_address!='' THEN SET v_address=p_address; END IF;
    IF p_city!='' THEN SET v_city=p_city; END IF;
    IF p_postcode!='' THEN SET v_postcode=p_postcode; END IF;
    IF p_language!='' THEN SET v_language=p_language; END IF;

    UPDATE store_part SET store_id=v_store_id,
                          name=v_name,
                          description=v_description,
                          latitude=v_latitude,
                          longitude=v_longitude,
                          email=v_email,
                          phone1=v_phone1,
                          phone2=v_phone2,
                          address=v_address,
                          city=v_city,
                          postcode=v_postcode,
                          language=v_language
    WHERE id=p_part_id;
    
    CALL add_audit(p_user,'update_store_part',CONCAT('success: code=0 username=',p_user,' id=',p_part_id));

  ELSE
    CALL add_audit(p_user,'update_store_part',CONCAT('error: code=-3 username=',p_user,' id=',p_part_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_store_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_part_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_store_part',CONCAT('error: code=-1 username=',p_user,' id=',p_part_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM store_part WHERE id=p_part_id;
    
    CALL add_audit(p_user,'remove_store_part',CONCAT('success: code=0 username=',p_user,' id=',p_part_id));

  ELSE
    CALL add_audit(p_user,'remove_store_part',CONCAT('error: code=-3 username=',p_user,' id=',p_part_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_store_part_picture`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_part_id INTEGER,
  p_picture MEDIUMBLOB) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_store_part_picture',CONCAT('error: code=-1 username=',p_user,' id=',p_part_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_picture IS NOT NULL THEN
      UPDATE store_part SET picture=p_picture WHERE id=p_part_id;
      CALL add_audit(p_user,'update_store_part_picture',CONCAT('success: code=0 username=',p_user,' id=',p_part_id));
    END IF;

  ELSE
    CALL add_audit(p_user,'update_store_part_picture',CONCAT('error: code=-3 username=',p_user,' id=',p_part_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_stock`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_product_id INTEGER,
  p_store_id INTEGER,
  p_count INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_stock',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO stock (product_id,store_id,count)
    VALUES (p_product_id,p_store_id,p_count);
    
    CALL add_audit(p_user,'add_stock',CONCAT('success: code=0 username=',p_user,' product_id=',p_product_id,' store_id=',p_store_id));

  ELSE
    CALL add_audit(p_user,'add_stock',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_stock`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_product_id INTEGER,
  p_store_id INTEGER,
  p_count INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_count INTEGER;

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_stock',CONCAT('error: code=-1 username=',p_user,' product_id=',p_product_id,' store_id=',p_store_id));
    RETURN -1;
  END IF;

  SELECT count INTO v_count
  FROM stock
  WHERE product_id=p_product_id AND store_id=p_store_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_count!=-1 THEN SET v_count=p_count; END IF;

    UPDATE stock SET count=v_count
    WHERE product_id=p_product_id AND store_id=p_store_id;
    
    CALL add_audit(p_user,'update_stock',CONCAT('success: code=0 username=',p_user,' product_id=',p_product_id,' store_id=',p_store_id));

  ELSE
    CALL add_audit(p_user,'update_stock',CONCAT('error: code=-3 username=',p_user,' product_id=',p_product_id,' store_id=',p_store_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_stock`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_product_id INTEGER,
  p_store_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_stock',CONCAT('error: code=-1 username=',p_user,' product_id=',p_product_id,' store_id=',p_store_id));    
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM stock WHERE product_id=p_product_id AND store_id=p_store_id;
    
    CALL add_audit(p_user,'remove_stock',CONCAT('success: code=0 username=',p_user,' product_id=',p_product_id,' store_id=',p_store_id));

  ELSE
    CALL add_audit(p_user,'remove_stock',CONCAT('error: code=-3 username=',p_user,' product_id=',p_product_id,' store_id=',p_store_id));      
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_stock_invoice`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_invoice_code VARCHAR(32),
  p_invoice_date VARCHAR(20),
  p_total_price VARCHAR(16),
  p_total_tax VARCHAR(10),
  p_supplier VARCHAR(32),
  p_phone VARCHAR(20),
  p_delivery_id INTEGER,
  p_delivery_code VARCHAR(32),
  p_delivery_type_id INTEGER,
  p_delivery_date VARCHAR(20),
  p_delivery_price VARCHAR(16),
  p_payment_date VARCHAR(20),
  p_payment_info VARCHAR(32),
  p_payment_amount VARCHAR(10)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_stock_invoice',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_invoice_date='' THEN SET p_invoice_date=NULL; END IF;
    IF p_delivery_id=-1 THEN SET p_delivery_id=NULL; END IF;
    IF p_delivery_date='' THEN SET p_delivery_date=NULL; END IF;
    IF p_delivery_price='' THEN SET p_delivery_price=NULL; END IF;
    IF p_delivery_type_id=-1 THEN SET p_delivery_type_id=NULL; END IF;
    IF p_payment_date='' THEN SET p_payment_date=NULL; END IF;
    IF p_payment_amount='' THEN SET p_payment_amount=NULL; END IF;

    INSERT INTO stock_invoice (invoice_code,invoice_date,total_price,total_tax,supplier,phone,
                               delivery_id,delivery_code,delivery_type_id,delivery_date,delivery_price,
                               payment_date,payment_info,payment_amount)
    VALUES (p_invoice_code,p_invoice_date,p_total_price,p_total_tax,p_supplier,p_phone,
            p_delivery_id,p_delivery_code,p_delivery_type_id,p_delivery_date,p_delivery_price,
            p_payment_date,p_payment_info,p_payment_amount);

    SET ret_val=LAST_INSERT_ID();
    
    CALL add_audit(p_user,'add_stock_invoice',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_stock_invoice',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_stock_invoice`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_stock_invoice_id INTEGER,
  p_invoice_code VARCHAR(32),
  p_invoice_date VARCHAR(20),
  p_total_price VARCHAR(16),
  p_total_tax VARCHAR(10),
  p_supplier VARCHAR(32),
  p_phone VARCHAR(20),
  p_delivery_id INTEGER,
  p_delivery_code VARCHAR(32),
  p_delivery_type_id INTEGER,
  p_delivery_date VARCHAR(20),
  p_delivery_price VARCHAR(16),
  p_payment_date VARCHAR(20),
  p_payment_info VARCHAR(32),
  p_payment_amount VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_invoice_code VARCHAR(32);
  DECLARE v_invoice_date VARCHAR(20);
  DECLARE v_total_price VARCHAR(16);
  DECLARE v_total_tax VARCHAR(10);
  DECLARE v_supplier VARCHAR(32);
  DECLARE v_phone VARCHAR(20);
  DECLARE v_delivery_id INTEGER;
  DECLARE v_delivery_code VARCHAR(32);
  DECLARE v_delivery_type_id INTEGER;
  DECLARE v_delivery_date VARCHAR(20);
  DECLARE v_delivery_price VARCHAR(16);
  DECLARE v_payment_date VARCHAR(20);
  DECLARE v_payment_info VARCHAR(32);
  DECLARE v_payment_amount VARCHAR(16);
  DECLARE v_reserved_date VARCHAR(20);
  DECLARE v_reserved_hours VARCHAR(6);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_stock_invoice',CONCAT('error: code=-1 username=',p_user,' id=',p_stock_invoice_id));
    RETURN -1;
  END IF;

  SELECT invoice_code,invoice_date,total_price,total_tax,supplier,phone,
         delivery_id,delivery_code,delivery_type_id,delivery_date,delivery_price,
         payment_date,payment_info,payment_amount
  INTO v_invoice_code,v_invoice_date,v_total_price,v_total_tax,v_supplier,v_phone,
       v_delivery_id,v_delivery_code,v_delivery_type_id,v_delivery_date,v_delivery_price,
       v_payment_date,v_payment_info,v_payment_amount
  FROM stock_invoice
  WHERE id=p_stock_invoice_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_invoice_code!='' THEN SET v_invoice_code=p_invoice_code; END IF;
    IF p_invoice_date!='' THEN SET v_invoice_date=p_invoice_date; END IF;
    IF p_total_price!='' THEN SET v_total_price=p_total_price; END IF;
    IF p_total_tax!='' THEN SET v_total_tax=p_total_tax; END IF;
    IF p_supplier!='' THEN SET v_supplier=p_supplier; END IF;
    IF p_phone!='' THEN SET v_phone=p_phone; END IF;
    IF p_delivery_id!=-1 THEN SET v_delivery_id=p_delivery_id; ELSE SET v_delivery_id=NULL; END IF;
    IF p_delivery_code!='' THEN SET v_delivery_code=p_delivery_code; END IF;
    IF p_delivery_type_id!=-1 THEN SET v_delivery_type_id=p_delivery_type_id; ELSE SET v_delivery_type_id=NULL; END IF;
    IF p_delivery_date!='' THEN SET v_delivery_date=p_delivery_date; END IF;
    IF p_delivery_price!='' THEN SET v_delivery_price=p_delivery_price; END IF;
    IF p_payment_date!='' THEN SET v_payment_date=p_payment_date; END IF;
    IF p_payment_info!='' THEN SET v_payment_info=p_payment_info; END IF;
    IF p_payment_amount!='' THEN SET v_payment_amount=p_payment_amount; END IF;

    UPDATE stock_invoice SET
           invoice_code=v_invoice_code,
           invoice_date=v_invoice_date,
           total_price=v_total_price,
           total_tax=v_total_tax,
           supplier=v_supplier,
           phone=v_phone,
           delivery_id=v_delivery_id,
           delivery_code=v_delivery_code,
           delivery_type_id=v_delivery_type_id,
           delivery_date=v_delivery_date,
           delivery_price=v_delivery_price,
           payment_date=v_payment_date,
           payment_info=v_payment_info,
           payment_amount=v_payment_amount
    WHERE id=p_stock_invoice_id;
    
    CALL add_audit(p_user,'update_stock_invoice',CONCAT('success: code=0 username=',p_user,' id=',p_stock_invoice_id));

  ELSE
    CALL add_audit(p_user,'update_stock_invoice',CONCAT('error: code=-3 username=',p_user,' id=',p_stock_invoice_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_stock_invoice`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_stock_invoice_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_stock_invoice',CONCAT('error: code=-1 username=',p_user,' id=',p_stock_invoice_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM stock_invoice WHERE id=p_stock_invoice_id;
    
    CALL add_audit(p_user,'remove_stock_invoice',CONCAT('success: code=0 username=',p_user,' id=',p_stock_invoice_id));

  ELSE
    CALL add_audit(p_user,'remove_stock_invoice',CONCAT('error: code=-3 username=',p_user,' id=',p_stock_invoice_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

-- reset total_price,total_tax in stock_invoce AND reset count in stock

DELIMITER $$

CREATE FUNCTION `add_stock_invoice_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_stock_invoice_id INTEGER,
  p_product_id INTEGER,
  p_tax_id INTEGER,
  p_count INTEGER,
  p_price VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_stock_invoice_part',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_tax_id=-1 THEN SET p_tax_id=NULL; END IF;

    INSERT INTO stock_invoice_part (stock_invoice_id,product_id,tax_id,count,price)
    VALUES (p_stock_invoice_id,p_product_id,p_tax_id,p_count,p_price);

    -- UPDATE STOCK_INVOICE IN total_price,total_tax
    -- UPDATE STOCK IN count
    
    CALL add_audit(p_user,'add_stock_invoice_part',CONCAT('success: code=0 username=',p_user,' stock_invoice_id=',p_stock_invoice_id,' product_id=',p_product_id));

  ELSE
    CALL add_audit(p_user,'add_stock_invoice_part',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_stock_invoice_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_stock_invoice_id INTEGER,
  p_product_id INTEGER,
  p_tax_id INTEGER,
  p_count INTEGER,
  p_price VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_tax_id INTEGER;
  DECLARE v_count INTEGER;
  DECLARE v_price VARCHAR(16);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_stock_invoice_part',CONCAT('error: code=-1 username=',p_user,' stock_invoice_id=',p_stock_invoice_id,' product_id=',p_product_id));
    RETURN -1;
  END IF;

  SELECT tax_id,count,price INTO v_tax_id,v_count,v_price
  FROM stock_invoice_part
  WHERE stock_invoice_id=p_stock_invoice_id AND product_id=p_product_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_tax_id!=-1 THEN SET v_tax_id=p_tax_id; ELSE SET v_tax_id=NULL; END IF;
    IF p_count!=-1 THEN SET v_count=p_count; END IF;
    IF p_price!='' THEN SET v_price=p_price; END IF;

    UPDATE stock_invoice_part SET tax_id=v_tax_id,
                                  count=v_count,
                                  price=v_price
    WHERE stock_invoice_id=p_stock_invoice_id AND product_id=p_product_id;

    -- UPDATE STOCK_INVOICE IN total_price,total_tax
    -- UPDATE STOCK IN count
    
    CALL add_audit(p_user,'update_stock_invoice_part',CONCAT('success: code=0 username=',p_user,' stock_invoice_id=',p_stock_invoice_id,' product_id=',p_product_id));

  ELSE
    CALL add_audit(p_user,'update_stock_invoice_part',CONCAT('error: code=-3 username=',p_user,' stock_invoice_id=',p_stock_invoice_id,' product_id=',p_product_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_stock_invoice_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_stock_invoice_id INTEGER,
  p_product_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_stock_invoice_part',CONCAT('error: code=-1 username=',p_user,' stock_invoice_id=',p_stock_invoice_id,' product_id=',p_product_id));  
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM stock_invoice_part WHERE stock_invoice_id=p_stock_invoice_id AND product_id=p_product_id;

    -- UPDATE STOCK_INVOICE IN total_price,total_tax
    -- UPDATE STOCK IN count

    CALL add_audit(p_user,'remove_stock_invoice_part',CONCAT('success: code=0 username=',p_user,' stock_invoice_id=',p_stock_invoice_id,' product_id=',p_product_id));

  ELSE
    CALL add_audit(p_user,'remove_stock_invoice_part',CONCAT('error: code=-3 username=',p_user,' stock_invoice_id=',p_stock_invoice_id,' product_id=',p_product_id));  
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

-- type_id defined by admin (negotive values for alarm events and positive for activities)

DELIMITER $$

CREATE FUNCTION `add_track_type`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_type_id INTEGER,
  p_name VARCHAR(32),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_track_type',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO track_type (id,name,language) VALUES (p_type_id,p_name,p_language);
    
    SET ret_val=LAST_INSERT_ID();
    
    CALL add_audit(p_user,'add_track_type',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_track_type',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_track_type`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_type_id INTEGER,
  p_name VARCHAR(32),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_name VARCHAR(32);
  DECLARE v_language VARCHAR(32);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_track_type',CONCAT('error: code=-1 username=',p_user,' id=',p_type_id));
    RETURN -1;
  END IF;

  SELECT name,language INTO v_name,v_language
  FROM track_type
  WHERE id=p_type_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_language!='' THEN SET v_language=p_language; END IF;

    UPDATE track_type SET name=v_name,
                          language=v_language
    WHERE id=p_type_id;
    
    CALL add_audit(p_user,'update_track_type',CONCAT('success: code=0 username=',p_user,' id=',p_type_id));

  ELSE
    CALL add_audit(p_user,'update_track_type',CONCAT('error: code=-3 username=',p_user,' id=',p_type_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_track_type`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_type_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_track_type',CONCAT('error: code=-1 username=',p_user,' id=',p_type_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM track_type WHERE id=p_type_id;
    
    CALL add_audit(p_user,'remove_track_type',CONCAT('success: code=0 username=',p_user,' id=',p_type_id));

  ELSE
    CALL add_audit(p_user,'remove_track_type',CONCAT('error: code=-3 username=',p_user,' id=',p_type_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_transport`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_sensor_id INTEGER,
  p_name VARCHAR(128),
  p_color VARCHAR(16),
  p_license_plate VARCHAR(12)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_transport',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND (v_user_type=1 OR v_user_type=2)) THEN

    IF p_sensor_id=-1 THEN SET p_sensor_id=NULL; END IF;

    INSERT INTO transport (sensor_id,name,color,license_plate)
    VALUES (p_sensor_id,p_name,p_color,p_license_plate);

    SET ret_val=LAST_INSERT_ID();
    
    CALL add_audit(p_user,'add_transport',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_transport',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_transport`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_transport_id INTEGER,
  p_sensor_id INTEGER,
  p_name VARCHAR(128),
  p_color VARCHAR(16),
  p_license_plate VARCHAR(12)) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_sensor_id INTEGER;
  DECLARE v_name VARCHAR(128);
  DECLARE v_color VARCHAR(16);
  DECLARE v_license_plate VARCHAR(12);

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_transport',CONCAT('error: code=-1 username=',p_user,' id=',p_transport_id));
    RETURN -1;
  END IF;

  SELECT sensor_id,name,color,license_plate INTO v_sensor_id,v_name,v_color,v_license_plate
  FROM transport
  WHERE id=p_transport_id;

  IF v_sensor_id IS NOT NULL THEN
    SELECT user_id INTO v_user_id FROM sensor WHERE id=v_sensor_id;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_type=2 AND v_user_id=v_user_inner_id) THEN

    IF p_sensor_id!=-1 THEN SET v_sensor_id=p_sensor_id; END IF;
    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_color!='' THEN SET v_color=p_color; END IF;
    IF p_license_plate!='' THEN SET v_license_plate=p_license_plate; END IF;

    UPDATE transport SET sensor_id=v_sensor_id,
                         name=v_name,
                         color=v_color,
                         license_plate=v_license_plate
    WHERE id=p_transport_id;
    
    CALL add_audit(p_user,'update_transport',CONCAT('success: code=0 username=',p_user,' id=',p_transport_id));

  ELSE
    CALL add_audit(p_user,'update_transport',CONCAT('error: code=-3 username=',p_user,' id=',p_transport_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_transport`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_transport_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_transport',CONCAT('error: code=-1 username=',p_user,' id=',p_transport_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM transport WHERE id=p_transport_id;
    
    CALL add_audit(p_user,'remove_transport',CONCAT('success: code=0 username=',p_user,' id=',p_transport_id));

  ELSE
    CALL add_audit(p_user,'remove_transport',CONCAT('error: code=-3 username=',p_user,' id=',p_transport_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_transport_picture`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_transport_id INTEGER,
  p_picture MEDIUMBLOB) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_sensor_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_transport_picture',CONCAT('error: code=-1 username=',p_user,' id=',p_transport_id));
    RETURN -1;
  END IF;

  SELECT sensor_id INTO v_sensor_id
  FROM transport
  WHERE id=p_transport_id;

  IF v_sensor_id IS NOT NULL THEN
    SELECT user_id INTO v_user_id FROM sensor WHERE id=v_sensor_id;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_type=2 AND v_user_id=v_user_inner_id) THEN

    IF p_picture IS NOT NULL THEN
      UPDATE transport SET picture=p_picture
      WHERE id=p_transport_id;
      CALL add_audit(p_user,'update_transport_picture',CONCAT('success: code=0 username=',p_user,' id=',p_transport_id));
    END IF;

  ELSE
    CALL add_audit(p_user,'update_transport_picture',CONCAT('error: code=-3 username=',p_user,' id=',p_transport_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_transport_type`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_parent_id INTEGER,
  p_name VARCHAR(32),
  p_description VARCHAR(128),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_transport_type',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_parent_id=-1 THEN SET p_parent_id=NULL; END IF;

    INSERT INTO transport_type (parent_id,name,description,language)
    VALUES (p_parent_id,p_name,p_description,p_language);

    SET ret_val=LAST_INSERT_ID();
    
    CALL add_audit(p_user,'add_transport_type',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_transport_type',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_transport_type`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_type_id INTEGER,
  p_parent_id INTEGER,
  p_name VARCHAR(32),
  p_description VARCHAR(128),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_parent_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_description VARCHAR(128);
  DECLARE v_language VARCHAR(32);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_transport_type',CONCAT('error: code=-1 username=',p_user,' id=',p_type_id));
    RETURN -1;
  END IF;

  SELECT parent_id,name,description,language INTO v_parent_id,v_name,v_description,v_language
  FROM transport_type
  WHERE id=p_type_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    IF p_parent_id!=-1 THEN SET v_parent_id=p_parent_id; END IF;
    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_description!='' THEN SET v_description=p_description; END IF;
    IF p_language!='' THEN SET v_language=p_language; END IF;

    UPDATE transport_type SET parent_id=v_parent_id,
                              name=v_name,
                              description=v_description,
                              language=v_language
    WHERE id=p_type_id;
    
    CALL add_audit(p_user,'update_transport_type',CONCAT('success: code=0 username=',p_user,' id=',p_type_id));

  ELSE
    CALL add_audit(p_user,'update_transport_type',CONCAT('error: code=-3 username=',p_user,' id=',p_type_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_transport_type`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_type_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_transport_type',CONCAT('error: code=-1 username=',p_user,' id=',p_type_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM transport_type WHERE id=p_type_id;
    
    CALL add_audit(p_user,'remove_transport_type',CONCAT('success: code=0 username=',p_user,' id=',p_type_id));

  ELSE
    CALL add_audit(p_user,'remove_transport_type',CONCAT('error: code=-3 username=',p_user,' id=',p_type_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--


DELIMITER $$

CREATE FUNCTION `add_transport_type_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_transport_id INTEGER,
  p_type_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_transport_type_part',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO transport_type_part (transport_id,transport_type_id)
    VALUES (p_transport_id,p_type_id);
    
    CALL add_audit(p_user,'add_transport_type_part',CONCAT('success: code=0 username=',p_user,' transport_id=',p_transport_id,' type_id=',p_type_id));

  ELSE
    CALL add_audit(p_user,'add_transport_type_part',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_transport_type_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_transport_id INTEGER,
  p_type_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_transport_type_part',CONCAT('error: code=-1 username=',p_user,' transport_id=',p_transport_id,' type_id=',p_type_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM transport_type_part WHERE transport_id=p_transport_id AND transport_type_id=p_type_id;
    
    CALL add_audit(p_user,'remove_transport_type_part',CONCAT('success: code=0 username=',p_user,' transport_id=',p_transport_id,' type_id=',p_type_id));

  ELSE
    CALL add_audit(p_user,'remove_transport_type_part',CONCAT('error: code=-3 username=',p_user,' transport_id=',p_transport_id,' type_id=',p_type_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_transport_product_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_transport_id INTEGER,
  p_product_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_transport_product_part',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    INSERT INTO transport_product_part (transport_id,product_id)
    VALUES (p_transport_id,p_product_id);

    CALL add_audit(p_user,'add_transport_product_part',CONCAT('success: code=0 username=',p_user,' transport_id=',p_transport_id,' product_id=',p_product_id));

  ELSE
    CALL add_audit(p_user,'add_transport_product_part',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_transport_product_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_transport_id INTEGER,
  p_product_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_transport_product_part',CONCAT('error: code=-1 username=',p_user,' transport_id=',p_transport_id,' product_id=',p_product_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM transport_product_part WHERE transport_id=p_transport_id AND product_id=p_product_id;

    CALL add_audit(p_user,'remove_transport_product_part',CONCAT('success: code=0 username=',p_user,' transport_id=',p_transport_id,' product_id=',p_product_id));

  ELSE
    CALL add_audit(p_user,'remove_transport_product_part',CONCAT('error: code=-3 username=',p_user,' transport_id=',p_transport_id,' product_id=',p_product_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--
-- admin revision only

DELIMITER $$

CREATE FUNCTION `update_transport_review`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_transport_id INTEGER,
  p_review_count INTEGER,
  p_review_value INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_transport_review',CONCAT('error: code=-1 username=',p_user,' id=',p_transport_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    UPDATE transport SET review_count=p_review_count,
                         review_value=p_review_value
	WHERE id=p_transport_id;

   CALL add_audit(p_user,'update_transport_review',CONCAT('success: code=0 username=',p_user,' id=',p_transport_id,' count=',p_review_count,' value=',p_review_value));

  ELSE
    CALL add_audit(p_user,'update_transport_review',CONCAT('error: code=-3 username=',p_user,' id=',p_transport_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_transport_review`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_user_id INTEGER,
  p_transport_id INTEGER,
  p_description VARCHAR(128),
  p_value INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_review_count INTEGER;
  DECLARE v_review_value INTEGER;

  DECLARE v_count INTEGER;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_transport_review',CONCAT('error: code=-1 username=',p_user,' id=',p_transport_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF p_user_id=-1 THEN SET p_user_id=v_user_id; END IF;

  SELECT count(*) INTO v_count
  FROM transport_review
  WHERE order_id=p_order_id AND user_id=p_user_id AND transport_id=p_transport_id;

  IF v_count>0 THEN
    CALL add_audit(p_user,'add_transport_review',CONCAT('error: code=-4 username=',p_user,' id=',p_transport_id));
    RETURN -4;
  END IF;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR (v_user_id=p_user_id AND is_my_order(p_user,p_order_id)) THEN

    SELECT review_count,review_value INTO v_review_count,v_review_value
    FROM transport
    WHERE id=p_transport_id;

    SET v_review_count=v_review_count+1;
    SET v_review_value=v_review_value+p_value;

    INSERT INTO transport_review (order_id,user_id,transport_id,description,value)
    VALUES (p_order_id,p_user_id,p_transport_id,p_description,p_value);

    UPDATE transport SET review_count=v_review_count,
                         review_value=v_review_value
    WHERE id=p_transport_id;

    CALL add_audit(p_user,'add_transport_review',CONCAT('success: code=0 username=',p_user,' id=',p_transport_id,' value=',p_value));

  ELSE
    CALL add_audit(p_user,'add_transport_review',CONCAT('error: code=-3 username=',p_user,' id=',p_transport_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_transport_review`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_user_id INTEGER,
  p_transport_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_review_count INTEGER;
  DECLARE v_review_value INTEGER;

  DECLARE v_value INTEGER;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_transport_review',CONCAT('error: code=-4 username=',p_user,' id=',p_transport_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF p_user_id=-1 THEN SET p_user_id=v_user_id; END IF;

  SELECT value INTO v_value
  FROM transport_review
  WHERE order_id=p_order_id AND user_id=p_user_id AND transport_id=p_transport_id;

  IF v_value IS NULL THEN
    CALL add_audit(p_user,'remove_transport_review',CONCAT('error: code=-4 username=',p_user,' id=',p_transport_id));
    RETURN -4;
  END IF;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR v_user_id=p_user_id THEN

    SELECT review_count,review_value INTO v_review_count,v_review_value
    FROM transport
    WHERE id=p_transport_id;

    SET v_review_count=v_review_count-1;
    SET v_review_value=v_review_value-v_value;

    DELETE FROM transport_review
    WHERE order_id=p_order_id AND user_id=p_user_id AND transport_id=p_transport_id;

    UPDATE transport SET review_count=v_review_count,
                         review_value=v_review_value
    WHERE id=p_transport_id;

    CALL add_audit(p_user,'remove_transport_review',CONCAT('success: code=0 username=',p_user,' id=',p_transport_id));

  ELSE
    CALL add_audit(p_user,'remove_transport_review',CONCAT('error: code=-1 username=',p_user,' id=',p_transport_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_delivery`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_transport_id INTEGER,
  p_latitude VARCHAR(20),
  p_longitude VARCHAR(20),
  p_address VARCHAR(128)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_delivery',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND (v_user_type=1 OR v_user_type=2)) THEN

    -- admin only can set user_id
    IF v_user_type=2 THEN SET p_user_id=v_user_inner_id; END IF;

    IF p_transport_id=-1 THEN SET p_transport_id=NULL; END IF;
    IF p_latitude='' THEN SET p_latitude=NULL; END IF;
    IF p_longitude='' THEN SET p_longitude=NULL; END IF;

    INSERT INTO delivery (user_id,transport_id,latitude,longitude,address)
    VALUES (p_user_id,p_transport_id,p_latitude,p_longitude,p_address);

    SET ret_val=LAST_INSERT_ID();
    
    CALL add_audit(p_user,'add_delivery',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_delivery',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_delivery`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_delivery_id INTEGER,
  p_user_id INTEGER,
  p_transport_id INTEGER,
  p_latitude VARCHAR(20),
  p_longitude VARCHAR(20),
  p_address VARCHAR(128)) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_transport_id INTEGER;
  DECLARE v_latitude VARCHAR(20);
  DECLARE v_longitude VARCHAR(20);
  DECLARE v_address VARCHAR(128);

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_delivery',CONCAT('error: code=-1 username=',p_user,' id=',p_delivery_id));
    RETURN -1;
  END IF;

  SELECT user_id,transport_id,latitude,longitude,address
  INTO v_user_id,v_transport_id,v_latitude,v_longitude,v_address
  FROM delivery
  WHERE id=p_delivery_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_type=2 AND v_user_id=v_user_inner_id) THEN

    IF v_user_type=1 THEN -- admin can changing user or transport in action
      IF p_user_id!=-1 THEN SET v_user_id=p_user_id; END IF;
      IF p_transport_id!=-1 THEN SET v_transport_id=p_transport_id; END IF;
    END IF;

    IF p_latitude!='' THEN SET v_latitude=p_latitude; END IF;
    IF p_longitude!='' THEN SET v_longitude=p_longitude; END IF;
    IF p_address!='' THEN SET v_address=p_address; END IF;

    UPDATE delivery SET user_id=v_user_id,
                        transport_id=v_transport_id,
                        latitude=v_latitude,
                        longitude=v_longitude,
                        address=v_address
    WHERE id=p_delivery_id;
    
    CALL add_audit(p_user,'update_delivery',CONCAT('success: code=0 username=',p_user,' id=',p_delivery_id));

  ELSE
    CALL add_audit(p_user,'update_delivery',CONCAT('error: code=-3 username=',p_user,' id=',p_delivery_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_delivery`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_delivery_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_delivery',CONCAT('error: code=-1 username=',p_user,' id=',p_delivery_id));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM delivery WHERE id=p_delivery_id;
    
    CALL add_audit(p_user,'remove_delivery',CONCAT('success: code=0 username=',p_user,' id=',p_delivery_id));

  ELSE
    CALL add_audit(p_user,'remove_delivery',CONCAT('error: code=-3 username=',p_user,' id=',p_delivery_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_order_AB`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_transport_id INTEGER,
  p_total_price VARCHAR(16),
  p_route_distance INTEGER,
  p_route_duration INTEGER,
  p_route_data BLOB,
  p_order_lat VARCHAR(20),
  p_order_lon VARCHAR(20),
  p_order_address VARCHAR(128),
  p_delivery_lat VARCHAR(20),
  p_delivery_lon VARCHAR(20),
  p_delivery_address VARCHAR(128),
  p_delivery_type_id INTEGER,
  p_reserved_date VARCHAR(20),
  p_reserved_hours INTEGER) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_keyword VARCHAR(8) DEFAULT 'E2E4F117';

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_order_AB',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND (v_user_type=1 OR v_user_type=2 OR v_user_type=3)) THEN

    -- set inner user_id for non admin
    IF v_user_type=2 OR v_user_type=3 THEN SET p_user_id=v_user_inner_id;END IF;

    IF p_transport_id=-1 THEN SET p_transport_id=NULL; END IF;
    IF p_total_price='' THEN SET p_total_price=0; END IF;
    IF p_route_distance=-1 THEN SET p_route_distance=NULL; END IF;
    IF p_route_duration=-1 THEN SET p_route_duration=NULL; END IF;
    IF p_route_data='' THEN SET p_route_data=NULL; END IF;
    IF p_order_lat='' THEN SET p_order_lat=NULL; END IF;
    IF p_order_lon='' THEN SET p_order_lon=NULL; END IF;
    IF p_delivery_lat='' THEN SET p_delivery_lat=NULL; END IF;
    IF p_delivery_lon='' THEN SET p_delivery_lon=NULL; END IF;
    IF p_delivery_type_id=-1 THEN SET p_delivery_type_id=NULL; END IF;
    IF p_reserved_date='' THEN SET p_reserved_date=NULL; END IF;
    IF p_reserved_hours=-1 THEN SET p_reserved_hours=NULL; END IF;

    INSERT INTO order_AB (user_id,transport_id,total_price,route_distance,route_duration,route_data,delivery_lat,delivery_lon,delivery_address,delivery_type_id,order_lat,order_lon,order_address,reserved_date,reserved_hours)
    VALUES (p_user_id,p_transport_id,p_total_price,p_route_distance,p_route_duration,p_route_data,p_delivery_lat,p_delivery_lon,p_delivery_address,p_delivery_type_id,p_order_lat,p_order_lon,p_order_address,p_reserved_date,p_reserved_hours);

    SET ret_val=LAST_INSERT_ID();
    
    CALL add_audit(p_user,'add_order_AB',CONCAT('success: code=0 username=',p_user,' id=',ret_val));
    
    IF p_transport_id IS NOT NULL THEN
	  CALL update_user_prepaid_amount_by_order_AB(p_user,ret_val,p_transport_id,FALSE,v_keyword); -- transfer fund
    END IF;

  ELSE
    CALL add_audit(p_user,'add_order_AB',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_order_AB`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_user_id INTEGER,
  p_transport_id INTEGER,
  p_status_id INTEGER,
  p_total_price VARCHAR(16),
  p_route_distance INTEGER,
  p_route_duration INTEGER,
  p_route_data BLOB,
  p_order_lat VARCHAR(20),
  p_order_lon VARCHAR(20),
  p_order_address VARCHAR(128),
  p_delivery_lat VARCHAR(20),
  p_delivery_lon VARCHAR(20),
  p_delivery_address VARCHAR(128),
  p_delivery_type_id INTEGER,
  p_reserved_date VARCHAR(20),
  p_reserved_hours INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_transport_id INTEGER;
  DECLARE v_status_id INTEGER;
  DECLARE v_total_price VARCHAR(16);
  DECLARE v_route_distance INTEGER;
  DECLARE v_route_duration INTEGER;
  DECLARE v_route_data BLOB;
  DECLARE v_order_lat VARCHAR(20);
  DECLARE v_order_lon VARCHAR(20);
  DECLARE v_order_address VARCHAR(128);
  DECLARE v_delivery_lat VARCHAR(20);
  DECLARE v_delivery_lon VARCHAR(20);
  DECLARE v_delivery_address VARCHAR(128);
  DECLARE v_delivery_type_id INTEGER;
  DECLARE v_reserved_date VARCHAR(20);
  DECLARE v_reserved_hours INTEGER;

  DECLARE v_order_transport_id INTEGER;
  DECLARE v_order_status_id INTEGER;

  DECLARE v_keyword VARCHAR(8) DEFAULT 'E2E4F117';

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_order_AB',CONCAT('error: code=-1 username=',p_user,' id=',p_order_id));
    RETURN -1;
  END IF;

  SELECT user_id,transport_id,order_status_id,total_price,route_distance,route_duration,route_data,delivery_lat,delivery_lon,delivery_address,delivery_type_id,order_lat,order_lon,order_address,reserved_date,reserved_hours
  INTO v_user_id,v_transport_id,v_status_id,v_total_price,v_route_distance,v_route_duration,v_route_data,v_delivery_lat,v_delivery_lon,v_delivery_address,v_delivery_type_id,v_order_lat,v_order_lon,v_order_address,v_reserved_date,v_reserved_hours
  FROM order_AB
  WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_type=2 AND is_my_transport(p_user,v_transport_id)=1) OR
     (v_user='fa_client' AND (v_user_type=2 OR v_user_type=3) AND v_user_id=v_user_inner_id AND v_status_id=1) THEN

    IF p_user_id!=-1 AND v_user_type=1 THEN SET v_user_id=p_user_id; END IF;

    IF p_transport_id!=-1 AND v_user_type=1 THEN SET v_order_transport_id=p_transport_id; END IF;
    IF p_transport_id=-1 AND v_user_type=1 THEN SET v_order_transport_id=NULL;SET p_transport_id=NULL; END IF;

    IF v_user_type=1 OR (v_user_type=2 AND p_status_id>0 AND v_status_id<5 AND v_status_id>0) OR 
                        (v_user_type=3 AND p_status_id<0 AND v_status_id<3 AND v_status_id>0)
    THEN SET v_order_status_id=p_status_id; END IF;

    IF p_total_price!='' AND v_user_type=1 THEN SET v_total_price=p_total_price; END IF;
    IF p_route_distance!=-1 AND v_user_type=1 THEN SET v_route_distance=p_route_distance; END IF;
    IF p_route_duration!=-1 AND v_user_type=1 THEN SET v_route_duration=p_route_duration; END IF;
    IF p_route_data!='' AND v_user_type=1 THEN SET v_route_data=p_route_data; END IF;
    IF p_order_lat!='' AND v_user_type=1 THEN SET v_order_lat=p_order_lat; END IF;
    IF p_order_lon!='' AND v_user_type=1 THEN SET v_order_lon=p_order_lon; END IF;
    IF p_order_address!='' AND v_user_type=1 THEN SET v_order_address=p_order_address; END IF;
    IF p_delivery_lat!='' AND v_user_type=1 THEN SET v_delivery_lat=p_delivery_lat; END IF;
    IF p_delivery_lon!='' AND v_user_type=1 THEN SET v_delivery_lon=p_delivery_lon; END IF;
    IF p_delivery_address!='' AND v_user_type=1 THEN SET v_delivery_address=p_delivery_address; END IF;
    IF p_delivery_type_id!=-1 AND v_user_type=1 THEN SET v_delivery_type_id=p_delivery_type_id; END IF;
    IF p_reserved_date!='' AND v_user_type=1 THEN SET v_reserved_date=p_reserved_date; END IF;
    IF p_reserved_hours!=-1 AND v_user_type=1 THEN SET v_reserved_hours=p_reserved_hours; END IF;

    UPDATE order_AB SET
           user_id=v_user_id,
           transport_id=v_order_transport_id,
           order_status_id=v_order_status_id,
           total_price=v_total_price,
           route_distance=v_route_distance,
           route_duration=v_route_duration,
           route_data=v_route_data,
           order_lat=v_order_lat,
           order_lon=v_order_lon,
           order_address=v_order_address,
           delivery_lat=v_delivery_lat,
           delivery_lon=v_delivery_lon,
           delivery_address=v_delivery_address,
           delivery_type_id=v_delivery_type_id,
           reserved_date=v_reserved_date,
           reserved_hours=v_reserved_hours
    WHERE id=p_order_id;
    
    CALL add_audit(p_user,'update_order_AB',CONCAT('success: code=0 username=',p_user,' id=',p_order_id));

    -- driver fee
    IF v_transport_id IS NOT NULL AND p_transport_id IS NULL AND v_status_id>0 THEN
	  CALL update_user_prepaid_amount_by_order_AB(p_user,p_order_id,v_transport_id,TRUE,v_keyword); -- return fund
    END IF;

    IF v_transport_id IS NULL AND p_transport_id IS NOT NULL THEN
	  CALL update_user_prepaid_amount_by_order_AB(p_user,p_order_id,p_transport_id,FALSE,v_keyword); -- transfer fund
    END IF;

    IF v_transport_id IS NOT NULL AND p_transport_id IS NOT NULL THEN
      IF v_status_id>0 THEN CALL update_user_prepaid_amount_by_order_AB(p_user,p_order_id,v_transport_id,TRUE,v_keyword); END IF; -- return fund
      CALL update_user_prepaid_amount_by_order_AB(p_user,p_order_id,p_transport_id,FALSE,v_keyword); -- transfer fund
    END IF;

  ELSE
    CALL add_audit(p_user,'update_order_AB',CONCAT('error: code=-3 username=',p_user,' id=',p_order_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_order_AB_status`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_status_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);
  DECLARE v_user_id INTEGER;
  DECLARE v_transport_id INTEGER;
  DECLARE v_status_id INTEGER;
  
  DECLARE v_keyword VARCHAR(8) DEFAULT 'E2E4F117';

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_order_AB_status',CONCAT('error: code=-1 username=',p_user,' order_id=',p_order_id,' status_id=',p_status_id));
    RETURN -1;
  END IF;

  SELECT user_id,transport_id,order_status_id INTO v_user_id,v_transport_id,v_status_id FROM order_AB WHERE id=p_order_id;

  # SELECT worker (driver) account for prepaid_amount sum
  # if sum > 0 can set status for worker (driver) transport

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_type=2 AND is_my_transport(p_user,v_transport_id)=1 AND p_status_id>0 AND v_status_id<5 AND v_status_id>0) OR
     (v_user='fa_client' AND (v_user_type=2 OR v_user_type=3) AND v_user_id=v_user_inner_id AND p_status_id<0 AND v_status_id<3 AND v_status_id>0) THEN

    UPDATE order_AB SET order_status_id=p_status_id WHERE id=p_order_id;
    
    CALL add_audit(p_user,'update_order_AB_status',CONCAT('success: code=0 username=',p_user,' order_id=',p_order_id,' status_id=',p_status_id));
    
    -- driver fee
    IF v_transport_id IS NOT NULL AND p_status_id<0 AND v_status_id>0 THEN -- current status is active and cancelling order
	  CALL update_user_prepaid_amount_by_order_AB(p_user,p_order_id,v_transport_id,TRUE,v_keyword); -- return fund
    END IF;

  ELSE
    CALL add_audit(p_user,'update_order_AB_status',CONCAT('error: code=-3 username=',p_user,' order_id=',p_order_id,' status_id=',p_status_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_order_AB_transport`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_transport_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);
  DECLARE v_user_id INTEGER;
  DECLARE v_transport_id INTEGER;
  DECLARE v_order_transport_id INTEGER;
  DECLARE v_status_id INTEGER;
  DECLARE v_order_status_id INTEGER;

  DECLARE v_keyword VARCHAR(8) DEFAULT 'E2E4F117';
  
  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_order_AB_transport',CONCAT('error: code=-1 username=',p_user,' order_id=',p_order_id,' transport_id=',p_transport_id));
    RETURN -1;
  END IF;

  SELECT user_id,order_status_id,transport_id INTO v_user_id,v_status_id,v_transport_id FROM order_AB WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_type=2 AND
       ((p_transport_id=-1 AND is_my_transport(p_user,v_transport_id)=1) OR
        (p_transport_id!=-1 AND v_transport_id IS NULL))) OR
     (v_user='fa_client' AND v_user_type=3 AND v_user_id=v_user_inner_id AND p_transport_id=-1) THEN


    IF p_transport_id=-1 THEN
      SET v_order_transport_id=NULL;SET p_transport_id=NULL;SET v_order_status_id=1;
    ELSE SET v_order_transport_id=p_transport_id;SET v_order_status_id=2; END IF;

    UPDATE order_AB SET transport_id=v_order_transport_id,order_status_id=v_order_status_id WHERE id=p_order_id;

    CALL add_audit(p_user,'update_order_AB_transport',CONCAT('success: code=0 username=',p_user,' order_id=',p_order_id,' transport_id=',p_transport_id));

    -- driver fee
    IF v_transport_id IS NOT NULL AND p_transport_id IS NULL AND v_status_id>0 THEN
	  CALL update_user_prepaid_amount_by_order_AB(p_user,p_order_id,v_transport_id,TRUE,v_keyword); -- return fund
    END IF;

    IF v_transport_id IS NULL AND p_transport_id IS NOT NULL THEN
	  CALL update_user_prepaid_amount_by_order_AB(p_user,p_order_id,p_transport_id,FALSE,v_keyword); -- transfer fund
    END IF;

    IF v_transport_id IS NOT NULL AND p_transport_id IS NOT NULL THEN
      IF v_status_id>0 THEN CALL update_user_prepaid_amount_by_order_AB(p_user,p_order_id,v_transport_id,TRUE,v_keyword); END IF; -- return fund
      CALL update_user_prepaid_amount_by_order_AB(p_user,p_order_id,p_transport_id,FALSE,v_keyword); -- transfer fund
    END IF;

  ELSE
    CALL add_audit(p_user,'update_order_AB_transport',CONCAT('error: code=-3 username=',p_user,' order_id=',p_order_id,' transport_id=',p_transport_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

-- (driver fee tool procedure)

DELIMITER $$

CREATE PROCEDURE `update_user_prepaid_amount_by_order_AB`(
  p_user VARCHAR(64),
  p_order_id INTEGER,
  p_transport_id INTEGER,
  p_is_increase BOOLEAN,
  p_keyword VARCHAR(8)) -- SQL SECURITY INVOKER
BEGIN
  DECLARE v_driver_id INTEGER;

  DECLARE v_driver_fee_value DECIMAL(6,2);
  DECLARE v_driver_fee_percent DECIMAL(6,2);
  DECLARE v_total_price DECIMAL(6,2);

  SELECT value INTO v_driver_fee_value FROM tax WHERE code='driver_fee_value';
  SELECT value INTO v_driver_fee_percent FROM tax WHERE code='driver_fee_percent';

  IF p_keyword='E2E4F117' AND (v_driver_fee_value IS NOT NULL OR v_driver_fee_percent IS NOT NULL) THEN

    SELECT u.id INTO v_driver_id
    FROM transport t LEFT JOIN sensor s ON s.id=t.sensor_id LEFT JOIN user u ON u.id=s.user_id
    WHERE t.id=p_transport_id;

	IF v_driver_id IS NOT NULL THEN

      SELECT total_price INTO v_total_price FROM order_AB WHERE id=p_order_id;

	  IF v_driver_fee_value IS NOT NULL AND (v_total_price IS NULL OR v_driver_fee_percent IS NULL) THEN
        IF p_is_increase THEN
          UPDATE user SET prepaid_amount=prepaid_amount+v_driver_fee_value WHERE id=v_driver_id;
          CALL add_audit(p_user,'update_user_prepaid_amount_by_order_AB',CONCAT('success: code=0 username=',p_user,' user_id=',v_driver_id,' order_id=',p_order_id,' +',v_driver_fee_value));
	    ELSE
          UPDATE user SET prepaid_amount=prepaid_amount-v_driver_fee_value WHERE id=v_driver_id;
          CALL add_audit(p_user,'update_user_prepaid_amount_by_order_AB',CONCAT('success: code=0 username=',p_user,' user_id=',v_driver_id,' order_id=',p_order_id,' -',v_driver_fee_value));
        END IF;
      END IF;

      IF v_driver_fee_percent IS NOT NULL AND v_total_price IS NOT NULL THEN
		IF p_is_increase THEN
		  UPDATE user SET prepaid_amount=prepaid_amount+(v_total_price*v_driver_fee_percent)/100;
		  CALL add_audit(p_user,'update_user_prepaid_amount_by_order_AB',CONCAT('success: code=0 username=',p_user,' user_id=',v_driver_id,' order_id=',p_order_id,' +',v_driver_fee_percent,'%'));
		ELSE
		  UPDATE user SET prepaid_amount=prepaid_amount-(v_total_price*v_driver_fee_percent)/100;
		  CALL add_audit(p_user,'update_user_prepaid_amount_by_order_AB',CONCAT('success: code=0 username=',p_user,' user_id=',v_driver_id,' order_id=',p_order_id,' -',v_driver_fee_percent,'%'));
		END IF;
      END IF;

    END IF;
  END IF;

END $$

DELIMITER ;

-- (user fee tool procedure)

DELIMITER $$

CREATE PROCEDURE `update_user_prepaid_amount_by_fee_value`(
  p_user VARCHAR(64),
  p_user_id INTEGER,
  p_is_increase BOOLEAN,
  v_user_fee_value DECIMAL(6,2),
  v_user_fee_percent DECIMAL(6,2),
  v_total_price DECIMAL(6,2),
  p_keyword VARCHAR(8)) -- SQL SECURITY INVOKER
BEGIN
  IF p_keyword='E2E4F117' AND p_user_id IS NOT NULL AND (p_user_fee_value IS NOT NULL OR p_user_fee_percent IS NOT NULL) THEN

    IF p_user_fee_value IS NOT NULL AND (p_total_price IS NULL OR p_user_fee_percent IS NULL) THEN
	  IF p_is_increase THEN
		UPDATE user SET prepaid_amount=prepaid_amount+p_user_fee_value WHERE id=p_user_id;
		CALL add_audit(p_user,'update_user_prepaid_amount_by_fee_value',CONCAT('success: code=0 username=',p_user,' user_id=',p_user_id,' +',p_user_fee_value));
	  ELSE
		UPDATE user SET prepaid_amount=prepaid_amount-p_user_fee_value WHERE id=p_user_id;
		CALL add_audit(p_user,'update_user_prepaid_amount_by_fee_value',CONCAT('success: code=0 username=',p_user,' user_id=',p_user_id,' -',p_user_fee_value));
	  END IF;
	END IF;

	IF v_user_fee_percent IS NOT NULL AND p_total_price IS NOT NULL THEN
	  IF p_is_increase THEN
		UPDATE user SET prepaid_amount=prepaid_amount+(p_total_price*p_user_fee_percent)/100;
		CALL add_audit(p_user,'update_user_prepaid_amount_by_fee_value',CONCAT('success: code=0 username=',p_user,' user_id=',p_user_id,' +',p_user_fee_percent,'%'));
	  ELSE
        UPDATE user SET prepaid_amount=prepaid_amount-(p_total_price*p_user_fee_percent)/100;
		CALL add_audit(p_user,'update_user_prepaid_amount_by_fee_value',CONCAT('success: code=0 username=',p_user,' user_id=',p_user_id,' -',v_user_fee_percent,'%'));
	  END IF;
	END IF;
      
  END IF;

END $$

DELIMITER ;

-- (removing with suborder->part->product_param_part)

DELIMITER $$

CREATE FUNCTION `remove_order_AB`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_order_status INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_order_AB',CONCAT('error: code=-1 username=',p_user,' id=',p_order_id));
    RETURN -1;
  END IF;

  SELECT user_id,order_status_id INTO v_user_id,v_order_status FROM order_AB WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND (v_user_type=2 OR v_user_type=3) AND v_user_id=v_user_inner_id AND v_order_status=1) THEN

    DELETE FROM order_AB_user_part WHERE order_id=p_order_id;
    DELETE FROM order_AB_product_param_part WHERE order_id=p_order_id;
    DELETE FROM order_AB_product_part WHERE order_id=p_order_id;
    DELETE FROM order_AB WHERE id=p_order_id;
    
    CALL add_audit(p_user,'remove_order_AB',CONCAT('success: code=0 username=',p_user,' id=',p_order_id));

  ELSE
    CALL add_audit(p_user,'remove_order_AB',CONCAT('error: code=-3 username=',p_user,' id=',p_order_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_order_AB_product_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_product_id INTEGER,
  p_count INTEGER,
  p_price VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_order_status INTEGER;
  DECLARE v_price VARCHAR(16);
  -- DECLARE v_discount INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_order_AB_product_part',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  SELECT user_id,order_status_id INTO v_user_id,v_order_status FROM order_AB WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND (v_user_type=2 OR v_user_type=3) AND v_user_id=v_user_inner_id AND v_order_status=1) THEN

    -- set price for one product without discount
    IF v_user_type=2 OR v_user_type=3 THEN
      SELECT price INTO v_price FROM product WHERE id=p_product_id;
      SET p_price=v_price;
    END IF;

    INSERT INTO order_AB_product_part (order_id,product_id,count,price)
    VALUES (p_order_id,p_product_id,p_count,p_price);
    
    CALL add_audit(p_user,'add_order_AB_product_part',CONCAT('success: code=0 username=',p_user,' order_id=',p_order_id,' product_id=',p_product_id));

  ELSE
    CALL add_audit(p_user,'add_order_AB_product_part',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_order_AB_product_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_product_id INTEGER,
  p_count INTEGER,
  p_price VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_order_status INTEGER;
  DECLARE v_count INTEGER;
  DECLARE v_price VARCHAR(16);
  -- DECLARE v_discount INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_order_AB_product_part',CONCAT('error: code=-1 username=',p_user,' order_id=',p_order_id,' product_id=',p_product_id));
    RETURN -1;
  END IF;

  SELECT count,price INTO v_count,v_price
  FROM order_AB_product_part
  WHERE order_id=p_order_id AND product_id=p_product_id;

  SELECT user_id,order_status_id INTO v_user_id,v_order_status FROM order_AB WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND (v_user_type=2 OR v_user_type=3) AND v_user_id=v_user_inner_id AND v_order_status=1) THEN

    IF p_count!=-1 THEN SET v_count=p_count; END IF;
    -- set price for one product without discount
    IF p_price!=''  AND v_user_type=1 THEN SET v_price=p_price; END IF;

    UPDATE order_AB_product_part SET count=v_count,
							 price=v_price
    WHERE order_id=p_order_id AND product_id=p_product_id;
    
    CALL add_audit(p_user,'update_order_AB_product_part',CONCAT('success: code=0 username=',p_user,' order_id=',p_order_id,' product_id=',p_product_id));

  ELSE
    CALL add_audit(p_user,'update_order_AB_product_part',CONCAT('error: code=-3 username=',p_user,' order_id=',p_order_id,' product_id=',p_product_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_order_AB_product_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_product_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_order_status INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_order_AB_product_part',CONCAT('error: code=-1 username=',p_user,' order_id=',p_order_id,' product_id=',p_product_id));
    RETURN -1;
  END IF;

  SELECT user_id,order_status_id INTO v_user_id,v_order_status FROM order_AB WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND (v_user_type=2 OR v_user_type=3) AND v_user_id=v_user_inner_id AND v_order_status=1) THEN

    DELETE FROM order_AB_product_part WHERE order_id=p_order_id AND product_id=p_product_id;
    
    CALL add_audit(p_user,'remove_order_AB_product_part',CONCAT('success: code=0 username=',p_user,' order_id=',p_order_id,' product_id=',p_product_id));

  ELSE
    CALL add_audit(p_user,'remove_order_AB_product_part',CONCAT('error: code=-3 username=',p_user,' order_id=',p_order_id,' product_id=',p_product_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_order_AB_product_param_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_product_id INTEGER,
  p_param_id INTEGER,
  p_count INTEGER,
  p_price VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_order_status INTEGER;
  DECLARE v_price VARCHAR(16);
  -- DECLARE v_discount INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_order_AB_product_param_part',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  SELECT user_id,order_status_id INTO v_user_id,v_order_status FROM order_AB WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND (v_user_type=2 OR v_user_type=3) AND v_user_id=v_user_inner_id AND v_order_status=1) THEN

    -- set price for one product without discount
    IF v_user_type=3 THEN
      SELECT price INTO v_price FROM product_param_part WHERE product_id=p_product_id AND product_param_id=p_param_id;
      SET p_price=v_price;
    END IF;

    INSERT INTO order_AB_product_param_part (order_id,product_id,product_param_id,count,price)
    VALUES (p_order_id,p_product_id,p_param_id,p_count,p_price);

    CALL add_audit(p_user,'add_order_AB_product_param_part',CONCAT('success: code=0 username=',p_user,' order_id=',p_order_id,' product_id=',p_product_id,' param_id=',p_param_id));

  ELSE
    CALL add_audit(p_user,'add_order_AB_product_param_part',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_order_AB_product_param_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_product_id INTEGER,
  p_param_id INTEGER,
  p_count INTEGER,
  p_price VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_order_status INTEGER;
  DECLARE v_count INTEGER;
  DECLARE v_price VARCHAR(16);
  -- DECLARE v_discount INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_order_AB_product_param_part',CONCAT('error: code=-1 username=',p_user,' order_id=',p_order_id,' product_id=',p_product_id,' param_id=',p_param_id));
    RETURN -1;
  END IF;

  SELECT count,price INTO v_count,v_price
  FROM order_AB_product_param_part
  WHERE order_id=p_order_id AND product_id=p_product_id AND product_param_id=p_param_id;

  SELECT user_id,order_status_id INTO v_user_id,v_order_status FROM order_AB WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND (v_user_type=2 OR v_user_type=3) AND v_user_id=v_user_inner_id AND v_order_status=1) THEN

    IF p_count!=-1 THEN SET v_count=p_count; END IF;
    -- set price for one product without discount
    IF p_price!=''  AND v_user_type=1 THEN SET v_price=p_price; END IF;

    UPDATE order_AB_product_param_part SET count=v_count,
							               price=v_price
    WHERE order_id=p_order_id AND product_id=p_product_id AND product_param_id=p_param_id;
    
    CALL add_audit(p_user,'update_order_AB_product_param_part',CONCAT('success: code=0 username=',p_user,' order_id=',p_order_id,' product_id=',p_product_id,' param_id=',p_param_id));

  ELSE
    CALL add_audit(p_user,'update_order_AB_product_param_part',CONCAT('error: code=-3 username=',p_user,' order_id=',p_order_id,' product_id=',p_product_id,' param_id=',p_param_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_order_AB_product_param_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_product_id INTEGER,
  p_param_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_order_status INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_order_AB_product_param_part',CONCAT('error: code=-1 username=',p_user,' order_id=',p_order_id,' product_id=',p_product_id,' param_id=',p_param_id));
    RETURN -1;
  END IF;

  SELECT user_id,order_status_id INTO v_user_id,v_order_status FROM order_AB WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND (v_user_type=2 OR v_user_type=3) AND v_user_id=v_user_inner_id AND v_order_status=1) THEN

    DELETE FROM order_AB_product_param_part WHERE order_id=p_order_id AND product_id=p_product_id AND product_param_id=p_param_id;
    
    CALL add_audit(p_user,'remove_order_AB_product_param_part',CONCAT('success: code=0 username=',p_user,' order_id=',p_order_id,' product_id=',p_product_id,' param_id=',p_param_id));

  ELSE
    CALL add_audit(p_user,'remove_order_AB_product_param_part',CONCAT('error: code=-3 username=',p_user,' order_id=',p_order_id,' product_id=',p_product_id,' param_id=',p_param_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_order_AB_user_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_user_id INTEGER,
  p_product_param_id_1 INTEGER,
  p_product_param_id_2 INTEGER,
  p_pickup_status_id INTEGER,
  p_pickup_lat VARCHAR(20),
  p_pickup_lon VARCHAR(20),
  p_pickup_address VARCHAR(128),
  p_pickup_date VARCHAR(20),
  p_pickup_info VARCHAR(128),
  p_price VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_status_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_order_AB_user_part',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  SELECT order_status_id INTO v_status_id FROM order_AB WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND (v_user_type=2 OR v_user_type=3) AND v_status_id=1) THEN

    -- set inner user_id for non admin
    IF v_user_type=2 OR v_user_type=3 THEN SET p_user_id=v_user_inner_id;END IF;

    INSERT INTO order_AB_user_part (order_id,user_id,product_param_id_1,product_param_id_2,pickup_lat,pickup_lon,pickup_address,pickup_date,pickup_info,price)
    VALUES (p_order_id,p_user_id,p_product_param_id_1,p_product_param_id_2,p_pickup_lat,p_pickup_lon,p_pickup_address,p_pickup_date,p_pickip_info,p_price);
    
    CALL add_audit(p_user,'add_order_AB_user_part',CONCAT('success: code=0 username=',p_user,' order_id=',p_order_id,' user_id=',p_user_id));

  ELSE
    CALL add_audit(p_user,'add_order_AB_user_part',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

-- (not updating product_param_id_N as static allocated object)

DELIMITER $$

CREATE FUNCTION `update_order_AB_user_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_user_id INTEGER,
  p_pickup_status_id INTEGER,
  p_pickup_lat VARCHAR(20),
  p_pickup_lon VARCHAR(20),
  p_pickup_address VARCHAR(128),
  p_pickup_date VARCHAR(20),
  p_pickup_info VARCHAR(128),
  p_price VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_order_user_id INTEGER;
  DECLARE v_order_status_id INTEGER;
  DECLARE v_pickup_user_id INTEGER;
  DECLARE v_pickup_status_id INTEGER;

  DECLARE v_pickup_lat VARCHAR(20);
  DECLARE v_pickup_lon VARCHAR(20);
  DECLARE v_pickup_address VARCHAR(128);
  DECLARE v_pickup_date VARCHAR(20);
  DECLARE v_pickup_info VARCHAR(128);
  DECLARE v_price VARCHAR(16);
  
  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_order_AB_user_part',CONCAT('error: code=-1 username=',p_user,' order_id=',p_order_id,' user_id=',p_user_id));
    RETURN -1;
  END IF;

  SELECT user_id,pickup_status_id,pickup_lat,pickup_lon,pickup_address,pickup_date,pickup_info,price
  INTO v_pickup_user_id,v_pickup_status_id,v_pickup_lat,v_pickup_lon,v_pickup_address,v_pickup_date,v_pickup_info,v_price
  FROM order_AB_user_part
  WHERE order_id=p_order_id AND user_id=p_user_id;

  SELECT user_id,order_status_id INTO v_order_user_id,v_order_status_id FROM order_AB WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND (v_user_type=2 OR v_user_type=3) 
                                   AND (v_order_user_id=v_user_inner_id OR v_pickup_user_id=v_user_inner_id) 
                                   AND v_order_status_id=1 AND v_pickup_status_id=1) THEN

    IF v_user_type=1 OR (v_order_user_id=v_user_inner_id) OR (p_pickup_status_id<0 AND v_pickup_user_id=v_user_inner_id)
    THEN SET v_pickup_status_id=p_pickup_status_id; END IF;

    IF p_price!='' AND (v_user_type=1 OR v_order_user_id=v_user_inner_id) THEN SET v_price=p_price; END IF;
    
    IF p_pickup_lat!='' AND (v_user_type=1 OR v_pickup_user_id=v_user_inner_id) THEN SET v_pickup_lat=p_pickup_lat; END IF;
    IF p_pickup_lon!='' AND (v_user_type=1 OR v_pickup_user_id=v_user_inner_id) THEN SET v_pickup_lon=p_pickup_lon; END IF;
    IF p_pickup_address!='' AND (v_user_type=1 OR v_pickup_user_id=v_user_inner_id) THEN SET v_pickup_address=p_pickup_address; END IF;
    IF p_pickup_date!='' AND (v_user_type=1 OR v_pickup_user_id=v_user_inner_id) THEN SET v_pickup_date=p_pickup_date; END IF;
    IF p_pickup_info!='' AND (v_user_type=1 OR v_pickup_user_id=v_user_inner_id) THEN SET v_pickup_info=p_pickup_info; END IF;

    UPDATE order_AB_user_part SET pickup_status_id=p_pickup_status_id,
							      price=v_price,
                                  pickup_lat=v_pickup_lat,
                                  pickup_lon=v_pickup_lon,
                                  pickup_address=v_pickup_address,
                                  pickup_date=v_pickup_date,
                                  pickup_info=v_pickup_info
    WHERE order_id=p_order_id AND user_id=p_user_id;
    
    CALL add_audit(p_user,'update_order_AB_user_part',CONCAT('success: code=0 username=',p_user,' order_id=',p_order_id,' user_id=',p_user_id));

  ELSE
    CALL add_audit(p_user,'update_order_AB_user_part',CONCAT('error: code=-3 username=',p_user,' order_id=',p_order_id,' user_id=',p_user_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_order_AB_user_part_status`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_user_id INTEGER,
  p_status_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_order_user_id INTEGER;
  DECLARE v_order_status_id INTEGER;
  DECLARE v_pickup_user_id INTEGER;
  DECLARE v_pickup_status_id INTEGER; /*not used*/

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_order_AB_user_part_status',CONCAT('error: code=-1 username=',p_user,' order_id=',p_order_id,' user_id=',p_user_id,' status_id=',p_status_id));
    RETURN -1;
  END IF;

  SELECT user_id,pickup_status_id INTO v_pickup_user_id,v_pickup_status_id
  FROM order_AB_user_part
  WHERE order_id=p_order_id AND user_id=p_user_id;

  SELECT user_id,order_status_id INTO v_order_user_id,v_order_status_id FROM order_AB WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND (v_user_type=2 OR v_user_type=3)
                                   AND (v_order_user_id=v_user_inner_id OR (p_status_id<0 AND v_pickup_user_id=v_user_inner_id))
                                   AND v_order_status_id=1) THEN

    UPDATE order_AB_user_part SET pickup_status_id=p_status_id WHERE order_id=p_order_id AND user_id=p_user_id;
    
    CALL add_audit(p_user,'update_order_AB_user_part_status',CONCAT('success: code=0 username=',p_user,' order_id=',p_order_id,' user_id=',p_user_id,' status_id=',p_status_id));

  ELSE
    CALL add_audit(p_user,'update_order_AB_user_part_status',CONCAT('error: code=-3 username=',p_user,' order_id=',p_order_id,' user_id=',p_user_id,' status_id=',p_status_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_order_AB_user_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_user_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_order_user_id INTEGER;
  DECLARE v_order_status_id INTEGER;
  DECLARE v_pickup_user_id INTEGER;
  DECLARE v_pickup_status_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_order_AB_user_part',CONCAT('error: code=-1 username=',p_user,' order_id=',p_order_id,' user_id=',p_user_id));
    RETURN -1;
  END IF;

  SELECT user_id,pickup_status_id INTO v_pickup_user_id,v_pickup_status_id
  FROM order_AB_user_part
  WHERE order_id=p_order_id AND user_id=p_user_id;

  SELECT user_id,order_status_id INTO v_order_user_id,v_order_status_id FROM order_AB WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND (v_user_type=2 OR v_user_type=3)
                                   AND (v_order_user_id=v_user_inner_id OR (v_pickup_status_id=1 AND v_pickup_user_id=v_user_inner_id))
                                   AND v_order_status_id=1) THEN

    DELETE FROM order_AB_user_part WHERE order_id=p_order_id AND user_id=p_user_id;
    
    CALL add_audit(p_user,'remove_order_AB_user_part',CONCAT('success: code=0 username=',p_user,' order_id=',p_order_id,' user_id=',p_user_id));

  ELSE
    CALL add_audit(p_user,'remove_order_AB_user_part',CONCAT('error: code=-3 username=',p_user,' order_id=',p_order_id,' user_id=',p_user_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_order_job`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_customer_user_id INTEGER,
  p_worker_user_id INTEGER,
  p_total_price VARCHAR(16),
  p_order_lat VARCHAR(20),
  p_order_lon VARCHAR(20),
  p_order_address VARCHAR(128),
  p_product_id INTEGER,
  p_reserved_date VARCHAR(20),
  p_reserved_hours INTEGER) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_order_job',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND (v_user_type=1 OR v_user_type=2 OR v_user_type=3)) THEN

    -- set inner user_id for non admin
    IF v_user_type=2 OR v_user_type=3 THEN SET p_customer_user_id=v_user_inner_id;END IF;

    IF p_worker_user_id=-1 THEN SET p_worker_user_id=NULL; END IF;
    IF p_total_price='' THEN SET p_total_price=0; END IF;
    IF p_order_lat='' THEN SET p_order_lat=NULL; END IF;
    IF p_order_lon='' THEN SET p_order_lon=NULL; END IF;
    IF p_product_id=-1 THEN SET p_product_id=NULL; END IF;
    IF p_reserved_date='' THEN SET p_reserved_date=NULL; END IF;
    IF p_reserved_hours=-1 THEN SET p_reserved_hours=NULL; END IF;

    INSERT INTO order_job (customer_user_id,worker_user_id,total_price,order_lat,order_lon,order_address,product_id,reserved_date,reserved_hours)
    VALUES (p_customer_user_id,p_worker_user_id,p_total_price,p_order_lat,p_order_lon,p_order_address,p_product_id,p_reserved_date,p_reserved_hours);

    SET ret_val=LAST_INSERT_ID();
    
    CALL add_audit(p_user,'add_order_job',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_order_job',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_order_job`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_customer_user_id INTEGER,
  p_worker_user_id INTEGER,
  p_status_id INTEGER,
  p_total_price VARCHAR(16),
  p_order_lat VARCHAR(20),
  p_order_lon VARCHAR(20),
  p_order_address VARCHAR(128),
  p_product_id INTEGER,
  p_reserved_date VARCHAR(20),
  p_reserved_hours INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_customer_user_id INTEGER;
  DECLARE v_worker_user_id INTEGER;
  DECLARE v_status_id INTEGER;
  DECLARE v_total_price VARCHAR(16);
  DECLARE v_order_lat VARCHAR(20);
  DECLARE v_order_lon VARCHAR(20);
  DECLARE v_order_address VARCHAR(128);
  DECLARE v_product_id INTEGER;
  DECLARE v_reserved_date VARCHAR(20);
  DECLARE v_reserved_hours INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_order_job',CONCAT('error: code=-1 username=',p_user,' id=',p_order_id));
    RETURN -1;
  END IF;

  SELECT customer_user_id,worker_user_id,order_status_id,total_price,order_lat,order_lon,order_address,product_id,reserved_date,reserved_hours
  INTO v_customer_user_id,v_worker_user_id,v_status_id,v_total_price,v_order_lat,v_order_lon,v_order_address,v_product_id,v_reserved_date,v_reserved_hours
  FROM order_job
  WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_type=3 AND v_customer_user_id=v_user_inner_id AND v_status_id=1) THEN

    IF p_customer_user_id!=-1 AND v_user_type=1 THEN SET v_customer_user_id=p_customer_user_id; END IF;

    IF p_worker_user_id!=-1 AND v_user_type=1 THEN SET v_worker_user_id=p_worker_user_id; END IF;
    IF p_worker_user_id=-1 AND v_user_type=1 THEN SET v_worker_user_id=NULL; END IF;

    IF v_user_type=1 OR (v_user_type=2 AND p_status_id>0 AND v_status_id<4 AND v_status_id>0) OR 
                        (v_user_type=3 AND p_status_id<0 AND v_status_id<3 AND v_status_id>0)
    THEN SET v_status_id=p_status_id; END IF;

    IF p_total_price!='' AND v_user_type=1 THEN SET v_total_price=p_total_price; END IF;
    IF p_order_lat!='' AND v_user_type=1 THEN SET v_order_lat=p_order_lat; END IF;
    IF p_order_lon!='' AND v_user_type=1 THEN SET v_order_lon=p_order_lon; END IF;
    IF p_order_address!='' AND v_user_type=1 THEN SET v_order_address=p_order_address; END IF;
    IF p_product_id!=-1 AND v_user_type=1 THEN SET v_product_id=p_product_id; END IF;
    IF p_reserved_date!='' AND (v_user_type=1 OR v_user_type=3) THEN SET v_reserved_date=p_reserved_date; END IF;
    IF p_reserved_hours!=-1 AND (v_user_type=1 OR v_user_type=3) THEN SET v_reserved_hours=p_reserved_hours; END IF;

    UPDATE order_job SET
           customer_user_id=v_customer_user_id,
           worker_user_id=v_worker_user_id,
           order_status_id=v_status_id,
           total_price=v_total_price,
           order_lat=v_order_lat,
           order_lon=v_order_lon,
           order_address=v_order_address,
           product_id=v_product_id,
           reserved_date=v_reserved_date,
           reserved_hours=v_reserved_hours
    WHERE id=p_order_id;
    
    CALL add_audit(p_user,'update_order_job',CONCAT('success: code=0 username=',p_user,' id=',p_order_id));

  ELSE
    CALL add_audit(p_user,'update_order_job',CONCAT('error: code=-3 username=',p_user,' id=',p_order_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_order_job_status`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER,
  p_status_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);
  DECLARE v_transport_id INTEGER;
  DECLARE v_status_id INTEGER;

  DECLARE v_customer_user_id INTEGER;
  DECLARE v_worker_user_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_order_job_status',CONCAT('error: code=-1 username=',p_user,' order_id=',p_order_id,' status_id=',p_status_id));
    RETURN -1;
  END IF;

  SELECT customer_user_id,worker_user_id,order_status_id INTO v_customer_user_id,v_worker_user_id,v_status_id FROM order_job WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_type=2 AND v_worker_user_id=v_user_inner_id AND p_status_id>0 AND v_status_id<4 AND v_status_id>0) OR
     (v_user='fa_client' AND v_user_type=3 AND v_customer_user_id=v_user_inner_id AND p_status_id<0 AND v_status_id<3 AND v_status_id>0) THEN

    UPDATE order_job SET order_status_id=p_status_id WHERE id=p_order_id;
    
    CALL add_audit(p_user,'update_order_job_status',CONCAT('success: code=0 username=',p_user,' order_id=',p_order_id,' status_id=',p_status_id));

  ELSE
    CALL add_audit(p_user,'update_order_job_status',CONCAT('error: code=-3 username=',p_user,' order_id=',p_order_id,' status_id=',p_status_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_order_job`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_order_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_customer_user_id INTEGER;
  DECLARE v_order_status INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_order_job',CONCAT('error: code=-1 username=',p_user,' id=',p_order_id));
    RETURN -1;
  END IF;

  SELECT customer_user_id,order_status_id INTO v_customer_user_id,v_order_status FROM order_job WHERE id=p_order_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_type=3 AND v_customer_user_id=v_user_inner_id AND v_order_status=1) THEN

    DELETE FROM order_job WHERE id=p_order_id;
    
    CALL add_audit(p_user,'remove_order_job',CONCAT('success: code=0 username=',p_user,' id=',p_order_id));

  ELSE
    CALL add_audit(p_user,'remove_order_job',CONCAT('error: code=-3 username=',p_user,' id=',p_order_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_sensor`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_user_id INTEGER,
  p_name VARCHAR(32),
  p_serial_number VARCHAR(64),
  p_device_name VARCHAR(64),
  p_phone VARCHAR(20)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_sensor',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     v_user='fa_client' THEN

    -- set inner user_id for non adminadd_sensoradd_sensor
    IF v_user_type=3 OR v_user_type=2 THEN SET p_user_id=v_user_inner_id;END IF;

    SELECT id INTO ret_val FROM sensor WHERE user_id=p_user_id AND device_name=p_device_name AND phone=p_phone;

    IF ret_val IS NULL THEN
      INSERT INTO sensor (user_id,name,serial_number,device_name,phone)
      VALUES (p_user_id,p_name,p_serial_number,p_device_name,p_phone);

      SET ret_val=LAST_INSERT_ID();
      
      CALL add_audit(p_user,'add_sensor',CONCAT('success: code=0 username=',p_user,' id=',ret_val));
      
    END IF;

  ELSE
    CALL add_audit(p_user,'add_sensor',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_sensor`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_sensor_id INTEGER,
  p_user_id INTEGER,
  p_name VARCHAR(32),
  p_serial_number VARCHAR(64),
  p_device_name VARCHAR(64),
  p_phone VARCHAR(20)) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_serial_number VARCHAR(64);
  DECLARE v_device_name VARCHAR(64);
  DECLARE v_phone VARCHAR(20);

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_sensor',CONCAT('error: code=-1 username=',p_user,' id=',p_sensor_id));
    RETURN -1;
  END IF;

  SELECT user_id,name,serial_number,device_name,phone
  INTO v_user_id,v_name,v_serial_number,v_device_name,v_phone
  FROM sensor
  WHERE id=p_sensor_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    IF p_user_id!=-1 AND v_user_type=1 THEN SET v_user_id=p_user_id; END IF;
    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_serial_number!='' THEN SET v_serial_number=p_serial_number; END IF;
    IF p_device_name!='' THEN SET v_device_name=p_device_name; END IF;
    IF p_phone!='' THEN SET v_phone=p_phone; END IF;

    UPDATE sensor SET user_id=v_user_id,
                      name=v_name,
                      serial_number=v_serial_number,
                      device_name=v_device_name,
                      phone=v_phone
    WHERE id=p_sensor_id;
    
    CALL add_audit(p_user,'update_sensor',CONCAT('success: code=0 username=',p_user,' id=',p_sensor_id));

  ELSE
    CALL add_audit(p_user,'update_sensor',CONCAT('error: code=-3 username=',p_user,' id=',p_sensor_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_sensor`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_sensor_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_sensor',CONCAT('error: code=-1 username=',p_user,' id=',p_sensor_id));
    RETURN -1;
  END IF;

  SELECT user_id INTO v_user_id FROM sensor WHERE id=p_sensor_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    DELETE FROM sensor WHERE id=p_sensor_id;
    
    CALL add_audit(p_user,'remove_sensor',CONCAT('success: code=0 username=',p_user,' id=',p_sensor_id));

  ELSE
    CALL add_audit(p_user,'remove_sensor',CONCAT('error: code=-3 username=',p_user,' id=',p_sensor_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_sensor_activity`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_sensor_id INTEGER,
  p_activity TINYINT(1)) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_sensor_activity',CONCAT('error: code=-1 username=',p_user,' id=',p_sensor_id));
    RETURN -1;
  END IF;

  SELECT user_id INTO v_user_id FROM sensor WHERE id=p_sensor_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    UPDATE sensor SET active=p_activity WHERE id=p_sensor_id;
    
    CALL add_audit(p_user,'update_sensor_activity',CONCAT('success: code=0 username=',p_user,' id=',p_sensor_id,' active=',p_activity));

  ELSE
    CALL add_audit(p_user,'update_sensor_activity',CONCAT('error: code=-3 username=',p_user,' id=',p_sensor_id));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

-- ---------------------------------------------------------AUDIT END (no needs to track features)

-- usertype=2,3 has privileges to open a circle for owner sensors on other sensors
-- sensorA->sensorB (sensorB 'can see' sensorA)

DELIMITER $$

CREATE FUNCTION `add_sensor_circle`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_sensorA_id INTEGER,
  p_sensorB_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_create_date DATE;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT user_id INTO v_user_id FROM sensor WHERE id=p_sensorA_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    SELECT create_date INTO v_create_date FROM sensor_circle WHERE sensorA_id=p_sensorA_id AND sensorB_id=p_sensorB_id;

    IF v_create_date IS NULL THEN
      INSERT INTO sensor_circle (sensorA_id,sensorB_id)
      VALUES (p_sensorA_id,p_sensorB_id);
    END IF;

  ELSE
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_sensor_circle_to_user`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_sensor_id INTEGER,
  p_user_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE v_id INTEGER;

  DECLARE c_sensor CURSOR FOR
  SELECT id FROM sensor WHERE user_id=p_user_id ORDER BY id;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_sensor;
  get_data: LOOP

  FETCH c_sensor INTO v_id;

  IF v_finished=1 THEN LEAVE get_data;
  END IF;

  SELECT add_sensor_circle(p_user,p_pass,p_sensor_id,v_id) INTO v_id;

  END LOOP get_data;
  CLOSE c_sensor;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_sensor_circle`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_sensorA_id INTEGER,
  p_sensorB_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT user_id INTO v_user_id FROM sensor WHERE id=p_sensorA_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    DELETE FROM sensor_circle WHERE sensorA_id=p_sensorA_id AND sensorB_id=p_sensorB_id;

  ELSE
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_sensor_circle_to_user`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_sensor_id INTEGER,
  p_user_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE v_id INTEGER;

  DECLARE c_sensor CURSOR FOR
  SELECT id FROM sensor WHERE user_id=p_user_id ORDER BY id;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_sensor;
  get_data: LOOP

  FETCH c_sensor INTO v_id;

  IF v_finished=1 THEN LEAVE get_data;
  END IF;

  SELECT remove_sensor_circle(p_user,p_pass,p_sensor_id,v_id) INTO v_id;

  END LOOP get_data;
  CLOSE c_sensor;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_sensor_group`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_sensor_id INTEGER,
  p_name VARCHAR(32),
  p_start_date VARCHAR(20),
  p_finish_date VARCHAR(20)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT user_id INTO v_user_id FROM sensor WHERE id=p_sensor_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    INSERT INTO sensor_group (sensor_id,name,start_date,finish_date)
    VALUES (p_sensor_id,p_name,p_start_date,p_finish_date);

    SET ret_val=LAST_INSERT_ID();

  ELSE
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_sensor_group`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_group_id INTEGER,
  p_sensor_id INTEGER,
  p_name VARCHAR(32),
  p_start_date VARCHAR(20),
  p_finish_date VARCHAR(20)) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_sensor_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_start_date VARCHAR(20);
  DECLARE v_finish_date VARCHAR(20);

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT sensor_id,name,start_date,finish_date
  INTO v_sensor_id,v_name,v_start_date,v_finish_date
  FROM sensor_group
  WHERE id=p_group_id;

  SELECT user_id INTO v_user_id FROM sensor WHERE id=v_sensor_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    IF p_sensor_id!=-1 AND v_user_type=1 THEN SET v_sensor_id=p_sensor_id; END IF;
    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_start_date!='' THEN SET v_start_date=p_start_date; END IF;
    IF p_finish_date!='' THEN SET v_finish_date=p_finish_date; END IF;

    UPDATE sensor_group SET sensor_id=v_sensor_id,
							name=v_name,
							start_date=v_start_date,
							finish_date=v_finish_date
    WHERE id=p_group_id;

  ELSE
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_sensor_group`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_group_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_sensor_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT sensor_id INTO v_sensor_id FROM sensor_group WHERE id=p_group_id;

  IF v_sensor_id IS NOT NULL THEN
    SELECT user_id INTO v_user_id FROM sensor WHERE id=v_sensor_id;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    DELETE FROM sensor_group WHERE id=p_group_id;

  ELSE
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_sensor_group_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_group_id INTEGER,
  p_sensor_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT user_id INTO v_user_id FROM sensor WHERE id=p_sensor_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    INSERT INTO sensor_group_part (sensor_group_id,sensor_id)
    VALUES (p_group_id,p_sensor_id);

  ELSE
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_sensor_group_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_group_id INTEGER,
  p_sensor_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_user_group_master_id INTEGER;
  DECLARE v_sensor_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  -- admin, joiner to group and group master can remove

  SELECT user_id INTO v_user_id FROM sensor WHERE id=p_sensor_id;

  SELECT sensor_id INTO v_sensor_id FROM sensor_group WHERE id=p_group_id;

  IF v_sensor_id IS NOT NULL THEN
    SELECT user_id INTO v_user_group_master_id FROM sensor WHERE id=v_sensor_id;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) OR
     (v_user='fa_client' AND v_user_group_master_id=v_user_inner_id) THEN

    DELETE FROM sensor_group_part WHERE sensor_group_id=p_group_id AND sensor_id=p_sensor_id;

  ELSE
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_sensor_place`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_sensor_id INTEGER,
  p_latitude VARCHAR(20),
  p_longitude VARCHAR(20),
  p_name VARCHAR(32),
  p_description VARCHAR(128),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT user_id INTO v_user_id FROM sensor WHERE id=p_sensor_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    INSERT INTO sensor_place (sensor_id,latitude,longitude,name,description,language)
    VALUES (p_sensor_id,p_latitude,p_longitude,p_name,p_description,p_language);

    SET ret_val=LAST_INSERT_ID();

  ELSE
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_sensor_place`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_place_id INTEGER,
  p_sensor_id INTEGER,
  p_latitude VARCHAR(20),
  p_longitude VARCHAR(20),
  p_name VARCHAR(32),
  p_description VARCHAR(128),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_sensor_id INTEGER;

  DECLARE v_latitude VARCHAR(20);
  DECLARE v_longitude VARCHAR(20);
  DECLARE v_name VARCHAR(32);
  DECLARE v_description VARCHAR(128);
  DECLARE v_language VARCHAR(32);

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT sensor_id,latitude,longitude,name,description,language
  INTO v_sensor_id,v_latitude,v_longitude,v_name,v_description,v_language
  FROM sensor_place
  WHERE id=p_place_id;

  SELECT user_id INTO v_user_id FROM sensor WHERE id=v_sensor_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    IF p_sensor_id!=-1 AND v_user_type=1 THEN SET v_sensor_id=p_sensor_id; END IF;
    IF p_latitude!='' THEN SET v_latitude=p_latitude; END IF;
    IF p_longitude!='' THEN SET v_longitude=p_longitude; END IF;
    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_description!='' THEN SET v_description=p_description; END IF;
    IF p_language!='' THEN SET v_language=p_language; END IF;

    UPDATE sensor_place SET sensor_id=v_sensor_id,
                            latitude=v_latitude,
                            longitude=v_longitude,
                            name=v_name,
                            description=v_description,
                            language=v_language
    WHERE id=p_place_id;

  ELSE
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_sensor_place`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_place_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_sensor_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT sensor_id INTO v_sensor_id FROM sensor_place WHERE id=p_place_id;

  IF v_sensor_id IS NOT NULL THEN
    SELECT user_id INTO v_user_id FROM sensor WHERE id=v_sensor_id;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    DELETE FROM sensor_place WHERE id=p_place_id;

  ELSE
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_sensor_place_picture`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_place_id INTEGER,
  p_picture MEDIUMBLOB) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_sensor_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT sensor_id INTO v_sensor_id FROM sensor_place WHERE id=p_place_id;

  IF v_sensor_id IS NOT NULL THEN
    SELECT user_id INTO v_user_id FROM sensor WHERE id=v_sensor_id;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    IF p_picture IS NOT NULL THEN
      UPDATE sensor_place SET picture=p_picture
      WHERE id=p_place_id;
    END IF;

  ELSE
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_track`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_sensor_id INTEGER,
  p_type_id INTEGER,
  p_latitude VARCHAR(20),
  p_longitude VARCHAR(20),
  p_time VARCHAR(20),
  p_altitude VARCHAR(20),
  p_accuracy VARCHAR(20),
  p_bearing VARCHAR(20),
  p_speed VARCHAR(20),
  p_satellites VARCHAR(3),
  p_battery VARCHAR(3),
  p_timezone_offset VARCHAR(6)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT user_id INTO v_user_id FROM sensor WHERE id=p_sensor_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    IF p_time='' THEN SET p_time=NULL; END IF;
    IF p_altitude='' THEN SET p_altitude=NULL; END IF;
    IF p_accuracy='' THEN SET p_accuracy=NULL; END IF;
    IF p_bearing='' THEN SET p_bearing=NULL; END IF;
    IF p_speed='' THEN SET p_speed=NULL; END IF;
    IF p_satellites='' THEN SET p_satellites=NULL; END IF;
    IF p_battery='' THEN SET p_battery=NULL; END IF;
    IF p_timezone_offset='' THEN SET p_timezone_offset=NULL; END IF;

    INSERT INTO track (sensor_id,track_type_id,latitude,longitude,time,altitude,accuracy,bearing,speed,satellites,battery,timezone_offset)
    VALUES (p_sensor_id,p_type_id,p_latitude,p_longitude,p_time,p_altitude,p_accuracy,p_bearing,p_speed,p_satellites,p_battery,p_timezone_offset);

    SET ret_val=LAST_INSERT_ID();

  ELSE
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_track`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_track_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_sensor_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT sensor_id INTO v_sensor_id FROM track WHERE id=p_track_id;

  IF v_sensor_id IS NOT NULL THEN
    SELECT user_id INTO v_user_id FROM sensor WHERE id=v_sensor_id;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    DELETE FROM track WHERE id=p_track_id;

  ELSE
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_track_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_track_id INTEGER,
  p_name VARCHAR(32),
  p_description VARCHAR(128),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_sensor_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT sensor_id INTO v_sensor_id FROM track WHERE id=p_track_id;

  IF v_sensor_id IS NOT NULL THEN
    SELECT user_id INTO v_user_id FROM sensor WHERE id=v_sensor_id;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    INSERT INTO track_part (track_id,name,description,language)
    VALUES (p_track_id,p_name,p_description,p_language);

    SET ret_val=LAST_INSERT_ID();

  ELSE
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_track_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_part_id INTEGER,
  p_track_id INTEGER,
  p_name VARCHAR(32),
  p_description VARCHAR(128),
  p_language VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_sensor_id INTEGER;

  DECLARE v_track_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_description VARCHAR(128);
  DECLARE v_language VARCHAR(32);

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT track_id,name,description,language
  INTO v_track_id,v_name,v_description,v_language
  FROM track_part
  WHERE id=p_part_id;

  SELECT sensor_id INTO v_sensor_id FROM track WHERE id=v_track_id;

  IF v_sensor_id IS NOT NULL THEN
    SELECT user_id INTO v_user_id FROM sensor WHERE id=v_sensor_id;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    IF p_track_id!=-1 AND v_user_type=1 THEN SET v_track_id=p_track_id; END IF;
    IF p_name!='' THEN SET v_name=p_name; END IF;
    IF p_description!='' THEN SET v_description=p_description; END IF;
    IF p_language!='' THEN SET v_language=p_language; END IF;

    UPDATE track_part SET track_id=v_track_id,
                          name=v_name,
                          description=v_description,
                          language=v_language
    WHERE id=p_part_id;

  ELSE
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_track_part`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_part_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_sensor_id INTEGER;
  DECLARE v_track_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT track_id INTO v_track_id FROM track_part WHERE id=p_part_id;

  IF v_track_id IS NOT NULL THEN
    SELECT sensor_id INTO v_sensor_id FROM track WHERE id=v_track_id;
    IF v_sensor_id IS NOT NULL THEN
      SELECT user_id INTO v_user_id FROM sensor WHERE id=v_sensor_id;
    END IF;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    DELETE FROM track_part WHERE id=p_part_id;

  ELSE
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_track_part_picture`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_part_id INTEGER,
  p_picture MEDIUMBLOB) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_sensor_id INTEGER;
  DECLARE v_track_id INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT track_id INTO v_track_id FROM track_part WHERE id=p_part_id;

  IF v_track_id IS NOT NULL THEN
    SELECT sensor_id INTO v_sensor_id FROM track WHERE id=v_track_id;
    IF v_sensor_id IS NOT NULL THEN
      SELECT user_id INTO v_user_id FROM sensor WHERE id=v_sensor_id;
    END IF;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_user_id=v_user_inner_id) THEN

    IF p_picture IS NOT NULL THEN
      UPDATE track_part SET picture=p_picture
      WHERE id=p_part_id;
    END IF;

  ELSE
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_message`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_recipient VARCHAR(64),
  p_message VARCHAR(256)) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_user_id INTEGER;
  DECLARE v_pos INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT LOCATE('+',p_recipient) INTO v_pos;
  IF v_pos!=0 THEN
    SELECT id INTO v_user_id FROM user WHERE phone=p_recipient LIMIT 0,1;
  ELSE
    SELECT LOCATE('@',p_recipient) INTO v_pos;
    IF v_pos!=0 THEN
      SELECT id INTO v_user_id FROM user WHERE email=p_recipient LIMIT 0,1;
      ELSE
        SELECT id INTO v_user_id FROM user WHERE call_name=p_recipient LIMIT 0,1; -- call_name is UNIQUE!
    END IF;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF (v_user='root' OR v_user='fa_admin' OR
      v_user='fa_client') AND v_user_id IS NOT NULL THEN

    INSERT INTO message (type,userA_id,userB_id,message)
    VALUES (1,v_user_inner_id,v_user_id,p_message);

    SET ret_val=LAST_INSERT_ID();

    INSERT INTO message (type,userA_id,userB_id,message)
    VALUES (2,v_user_inner_id,v_user_id,p_message);

  ELSE
    RETURN -5;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_message`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_message_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_inner_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  DECLARE v_userA_id INTEGER;
  DECLARE v_userB_id INTEGER;
  DECLARE v_type INTEGER;

  SELECT id,type INTO v_user_inner_id,v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT userA_id,userB_id,type
  INTO v_userA_id,v_userB_id,v_type
  FROM message WHERE id=p_message_id;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) OR
     (v_user='fa_client' AND v_userB_id=v_user_inner_id AND v_type=1) OR
     (v_user='fa_client' AND v_userA_id=v_user_inner_id AND v_type=2) THEN

    DELETE FROM message WHERE id=p_message_id;

  ELSE
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_geocode`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_geocode_address VARCHAR(128),
  p_formatted_address VARCHAR(128),
  p_latitude DOUBLE,
  p_longitude DOUBLE) RETURNS INTEGER
BEGIN
  DECLARE ret_val INTEGER;

  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_geocode',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR v_user='fa_client' THEN

    INSERT INTO geocode (geocode_address,formatted_address,latitude,longitude)
    VALUES (p_geocode_address,p_formatted_address,p_latitude,p_longitude);

    SET ret_val=LAST_INSERT_ID();

    CALL add_audit(p_user,'add_geocode',CONCAT('success: code=0 username=',p_user,' id=',ret_val));

  ELSE
    CALL add_audit(p_user,'add_geocode',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  -- RETURN 0;
  RETURN ret_val;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `add_settings`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_code VARCHAR(32),
  p_name VARCHAR(64),
  p_value VARCHAR(64)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'add_settings',CONCAT('error: code=-1 username=',p_user));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

	INSERT INTO settings (code,name,value)
	VALUES (p_code,p_name,p_value);
      
	CALL add_audit(p_user,'add_settings',CONCAT('success: code=0 username=',p_user,' settings_code=',p_code,' value=',p_value));

  ELSE
    CALL add_audit(p_user,'add_settings',CONCAT('error: code=-3 username=',p_user));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `remove_settings`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_code VARCHAR(32)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'remove_settings',CONCAT('error: code=-1 username=',p_user,' settings_code=',p_code));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    DELETE FROM settings WHERE code=p_code;
    
    CALL add_audit(p_user,'remove_settings',CONCAT('success: code=0 username=',p_user,' settings_code=',p_code));

  ELSE
    CALL add_audit(p_user,'remove_settings',CONCAT('error: code=-3 username=',p_user,' settings_code=',p_code));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `update_settings`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16),
  p_code VARCHAR(32),
  p_name VARCHAR(64),
  p_value VARCHAR(64)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_user VARCHAR(64);

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    CALL add_audit(p_user,'update_settings',CONCAT('error: code=-1 username=',p_user,' settings_code=',p_code));
    RETURN -1;
  END IF;

  #SELECT SUBSTRING_INDEX(USER(),"@",1);
  SELECT SUBSTRING_INDEX(USER(),"@",1) INTO v_user;

  IF v_user='root' OR v_user='fa_admin' OR
     (v_user='fa_client' AND v_user_type=1) THEN

    UPDATE settings SET name=p_name,
                        value=p_value
    WHERE code=p_code;

    CALL add_audit(p_user,'update_settings',CONCAT('success: code=0 username=',p_user,' settings_code=',p_code,' value=',p_value));

  ELSE
    CALL add_audit(p_user,'update_settings',CONCAT('error: code=-3 username=',p_user,' settings_code=',p_code));
    RETURN -3;
  END IF;

  RETURN 0;
END $$

DELIMITER ;


-- that's all right mama, that's all right to you


-- ... -------------

-- [is function]

-- ORDER_DRIVER

DELIMITER $$

CREATE FUNCTION `is_driver_of_order`(
  p_user VARCHAR(64),
  p_order_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_value INTEGER;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT s.user_id INTO v_value
  FROM order_AB o,transport t,sensor s
  WHERE o.id=p_order_id AND o.transport_id=t.id AND t.sensor_id=s.id;

  IF v_value=v_user_id THEN
    RETURN 1;
  END IF;

  RETURN -100;
END $$

DELIMITER ;

-- MY_ORDER

DELIMITER $$

CREATE FUNCTION `is_my_order`(
  p_user VARCHAR(64),
  p_order_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_value INTEGER;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT user_id INTO v_value
  FROM order_AB
  WHERE id=p_order_id;

  IF v_value=v_user_id THEN
    RETURN 1;
  END IF;

  RETURN -100;
END $$

DELIMITER ;

-- MY_PURCHASE

DELIMITER $$

CREATE FUNCTION `is_my_purchase`(
  p_user VARCHAR(64),
  p_purchase_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_value INTEGER;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT user_id INTO v_value
  FROM purchase
  WHERE id=p_purchase_id;

  IF v_value=v_user_id THEN
    RETURN 1;
  END IF;

  RETURN -100;
END $$

DELIMITER ;

-- MY_DELIVERY

DELIMITER $$

CREATE FUNCTION `is_my_delivery`(
  p_user VARCHAR(64),
  p_delivery_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_value INTEGER;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT p.user_id INTO v_value
  FROM purchase p,delivery d
  WHERE d.id=p_delivery_id AND d.id=p.delivery_id;

  IF v_value=v_user_id THEN
    RETURN 1;
  END IF;

  RETURN -100;
END $$

DELIMITER ;

-- MY_TRANSPORT

DELIMITER $$

CREATE FUNCTION `is_my_transport`(
  p_user VARCHAR(64),
  p_transport_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_sensor_id INTEGER;

  SELECT sensor_id INTO v_sensor_id
  FROM transport
  WHERE id=p_transport_id;

  RETURN is_my_sensor(p_user,v_sensor_id);
END $$

DELIMITER ;

-- IS_TRANSPORT_RESERVED

DELIMITER $$

CREATE FUNCTION `is_transport_reserved`(
  p_transport_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_count INTEGER;

  SELECT COUNT(transport_id) INTO v_count
  FROM order_AB
  WHERE transport_id=p_transport_id AND order_status_id<=5 AND order_status_id>0;

  RETURN v_count;
END $$

DELIMITER ;

-- MY_SENSOR

DELIMITER $$

CREATE FUNCTION `is_my_sensor`(
  p_user VARCHAR(64),
  p_sensor_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_value INTEGER;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT user_id INTO v_value
  FROM sensor
  WHERE id=p_sensor_id;

  IF v_value=v_user_id THEN
    RETURN 1;
  END IF;

  RETURN -100;
END $$

DELIMITER ;

-- MY_SENSOR_CIRCLE

DELIMITER $$

CREATE FUNCTION `is_my_sensor_circle`(
  p_user VARCHAR(64),
  p_sensor_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_value INTEGER;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT count(*) INTO v_value
  FROM sensor_circle
  WHERE sensorA_id=p_sensor_id AND sensorB_id IN (SELECT id FROM sensor WHERE user_id=v_user_id);

  IF v_value>0 THEN
    RETURN 1;
  END IF;

  RETURN -100;
END $$

DELIMITER ;

-- MY_SENSOR_OR_CIRCLE

DELIMITER $$

CREATE FUNCTION `is_my_sensor_or_circle`(
  p_user VARCHAR(64),
  p_sensor_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_value INTEGER;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT count(*) INTO v_value
  FROM sensor
  WHERE user_id=v_user_id AND (id=p_sensor_id OR id IN (SELECT sensorB_id FROM sensor_circle WHERE sensorA_id=p_sensor_id));

  IF v_value>0 THEN
    RETURN 1;
  END IF;

  RETURN -100;
END $$

DELIMITER ;

-- MY_SENSOR_PLACE

DELIMITER $$

CREATE FUNCTION `is_my_sensor_place`(
  p_user VARCHAR(64),
  p_place_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_user_id INTEGER;
  DECLARE v_user_type INTEGER;
  DECLARE v_sensor_id INTEGER;
  DECLARE v_value INTEGER;

  SELECT id,type INTO v_user_id,v_user_type
  FROM user
  WHERE username=p_user;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  SELECT sensor_id INTO v_sensor_id
  FROM sensor_place
  WHERE id=p_place_id;

  IF v_sensor_id IS NULL THEN
    RETURN -100;
  END IF;

  SELECT user_id INTO v_value
  FROM sensor
  WHERE id=v_sensor_id;

  IF v_value=v_user_id THEN
    RETURN 1;
  END IF;

  RETURN -100;
END $$

DELIMITER ;


-- [get function]

-- GET_SCHEMA_NAME

DELIMITER $$

CREATE FUNCTION `get_schema_name`(
  p_api_key VARCHAR(64)) RETURNS VARCHAR(64)
BEGIN
  DECLARE v_schema_name VARCHAR(64);

  SELECT schema_name INTO v_schema_name
  FROM project
  WHERE api_key=p_api_key AND active IS TRUE;

  RETURN v_schema_name;
END $$

DELIMITER ;

-- GET_TOKEN

DELIMITER $$

CREATE FUNCTION `get_token`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16)) RETURNS VARCHAR(64)
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_token VARCHAR(64) DEFAULT '';

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NOT NULL THEN

    -- SELECT BASE64(ENCODE(p_user,p_pass)) INTO v_token;
    -- SELECT PASSWORD(p_user) INTO v_token;
    SELECT CONCAT(PASSWORD(p_user),TO_BASE64(ENCODE(p_user,p_pass))) INTO v_token;

    UPDATE user SET token=v_token
    WHERE username=p_user AND password=p_pass;

  END IF;

  RETURN v_token;
END $$

DELIMITER ;

--

DELIMITER $$

CREATE FUNCTION `get_user_credentials`(
  p_token VARCHAR(64)) RETURNS VARCHAR(82)
BEGIN
  DECLARE v_user_type INTEGER;
  DECLARE v_username VARCHAR(64);
  DECLARE v_password VARCHAR(16);
  DECLARE v_ret_val VARCHAR(82) DEFAULT '';

  SELECT type,username,password INTO v_user_type,v_username,v_password
  FROM user
  WHERE token=p_token;

  IF v_user_type IS NOT NULL THEN

    SET v_ret_val=CONCAT(v_username,'\\\\',v_password);

  END IF;

  RETURN v_ret_val;
END $$

DELIMITER ;

-- GET_USER_TYPE

DELIMITER $$

CREATE FUNCTION `get_user_type`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  RETURN v_user_type;
END $$

DELIMITER ;

-- GET_USER_TYPE_IS_ACTIVE

DELIMITER $$

CREATE FUNCTION `get_user_type_is_active`(
  p_user VARCHAR(64),
  p_pass VARCHAR(16)) RETURNS INTEGER
BEGIN
  DECLARE v_user_type INTEGER;

  SELECT type INTO v_user_type
  FROM user
  WHERE username=p_user AND password=p_pass AND active IS TRUE;

  IF v_user_type IS NULL THEN
    RETURN -1;
  END IF;

  RETURN v_user_type;
END $$

DELIMITER ;

-- RESTORE_USER_PASS

DELIMITER $$

CREATE FUNCTION `restore_user_pass`(
  p_user VARCHAR(64)) RETURNS VARCHAR(16)
BEGIN
  DECLARE v_user_pass VARCHAR(16);

  SELECT password INTO v_user_pass
  FROM user
  WHERE username=p_user;

  CALL add_audit(p_user,'password_recovery',CONCAT('username=',p_user));

  RETURN v_user_pass;
END $$

DELIMITER ;

-- GET_ATTR_PART

DELIMITER $$

CREATE FUNCTION `get_attr_part`(
  p_object_id INTEGER,
  p_object_name VARCHAR(64),
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE v_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_value VARCHAR(256);

  DECLARE v_attr MEDIUMTEXT DEFAULT '';

  DECLARE c_attr CURSOR FOR
  SELECT id,
         replace_text(name),
         IF(value IS NULL,'',replace_text(value))
  FROM attr_part
  WHERE object_name=p_object_name AND object_id=p_object_id AND language LIKE p_language
  ORDER BY id;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_attr;
  get_data: LOOP

  FETCH c_attr INTO v_id,v_name,v_value;

  IF v_finished=1 THEN LEAVE get_data;
  END IF;

  -- json
  SET v_attr=CONCAT(v_attr,'{',
      CONCAT(CHAR(34),'attr_part_id',CHAR(34),':',v_id,',',
      CHAR(34),'attr_name',CHAR(34),':',CHAR(34),v_name,CHAR(34),',',
      CHAR(34),'attr_value',CHAR(34),':',CHAR(34),v_value,CHAR(34)),'},');

  END LOOP get_data;
  CLOSE c_attr;

  SET v_attr=SUBSTR(v_attr,1,CHAR_LENGTH(v_attr)-1);

  RETURN v_attr;
END $$

DELIMITER ;

-- GET_ATTR_PART_VALUE

DELIMITER $$

CREATE FUNCTION `get_attr_part_value`(
  p_object_id INTEGER,
  p_object_name VARCHAR(64),
  p_attr_name  VARCHAR(32),
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_attr_value MEDIUMTEXT DEFAULT '';
  
  SELECT value INTO v_attr_value FROM attr_part
  WHERE object_name=p_object_name AND object_id=p_object_id AND name=p_attr_name AND language LIKE p_language;

  RETURN v_attr_value;
END $$

DELIMITER ;

-- GET_PRODUCT

DELIMITER $$

CREATE FUNCTION `get_product`(
  p_product_id INTEGER,
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE v_id INTEGER;
  DECLARE v_manufacture_id INTEGER;
  DECLARE v_discount_code VARCHAR(8);
  DECLARE v_discount MEDIUMTEXT;
  DECLARE v_name VARCHAR(32);
  DECLARE v_description VARCHAR(128);
  DECLARE v_code VARCHAR(32);
  DECLARE v_price VARCHAR(16);
  DECLARE v_rate VARCHAR(10);
  DECLARE v_count INTEGER;

  DECLARE v_product MEDIUMTEXT DEFAULT '';

  DECLARE c_product CURSOR FOR
  SELECT p.id,p.manufacture_id,IFNULL(p.discount_code,''),
         IF(p.discount_code IS NULL,'',get_discount(d.id,p_language)),
         replace_text(p.name),
         replace_text(p.description),
         p.code,p.price,
         IF(p.review_count=0,0,ROUND(p.review_value/p.review_count,2)),
         get_stock_count(p.id)
  FROM product p LEFT JOIN discount d ON d.code=p.discount_code
  WHERE p.id=p_product_id;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_product;
  get_data: LOOP

  FETCH c_product INTO v_id,v_manufacture_id,v_discount_code,v_discount,v_name,v_description,v_code,v_price,v_rate,v_count;

  IF v_finished=1 THEN LEAVE get_data;
  END IF;

  -- json
  SET v_product=CONCAT(v_product,'{',
        CONCAT(CHAR(34),'product_id',CHAR(34),':',v_id,',',
        CHAR(34),'manufacture_id',CHAR(34),':',v_manufacture_id,',',
        CHAR(34),'discount_code',CHAR(34),':',CHAR(34),v_discount_code,CHAR(34),',',
        CHAR(34),'discount',CHAR(34),':[',v_discount,'],',
        CHAR(34),'product_name',CHAR(34),':',CHAR(34),v_name,CHAR(34),',',
        CHAR(34),'description',CHAR(34),':',CHAR(34),v_description,CHAR(34),',',
        CHAR(34),'product_code',CHAR(34),':',CHAR(34),v_code,CHAR(34),',',
        CHAR(34),'product_price',CHAR(34),':',v_price,',',
        CHAR(34),'product_rate',CHAR(34),':',v_rate,',',
        CHAR(34),'stock_count',CHAR(34),':',v_count),'},');

  END LOOP get_data;
  CLOSE c_product;

  SET v_product=SUBSTR(v_product,1,CHAR_LENGTH(v_product)-1);

  -- ONE PRODUCT RETURN (but code writing for cursor->WHERE id LIKE)
  RETURN v_product;
END $$

DELIMITER ;

-- GET_PRODUCT_TYPE_PART

DELIMITER $$

CREATE FUNCTION `get_product_type_part`(
  p_product_id INTEGER,
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE v_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_description VARCHAR(128);
  DECLARE v_type_attr MEDIUMTEXT;

  DECLARE v_product_type_part MEDIUMTEXT DEFAULT '';

  DECLARE c_product_type_part CURSOR FOR
  SELECT pt.id,
         replace_text(pt.name),
         replace_text(pt.description),
         get_attr_part(pt.id,'product_type',p_language)
  FROM product_type pt,product_type_part ptp
  WHERE ptp.product_id=p_product_id AND ptp.product_type_id=pt.id
  ORDER BY pt.id;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_product_type_part;
  get_data: LOOP

  FETCH c_product_type_part INTO v_id,v_name,v_description,v_type_attr;

  IF v_finished=1 THEN LEAVE get_data;
  END IF;

  -- json
  SET v_product_type_part=CONCAT(v_product_type_part,'{',
      CONCAT(CHAR(34),'type_id',CHAR(34),':',v_id,',',
      CHAR(34),'type_name',CHAR(34),':',CHAR(34),v_name,CHAR(34),',',
      CHAR(34),'description',CHAR(34),':',CHAR(34),v_description,CHAR(34),',',
      CHAR(34),'type_attr',CHAR(34),':[',v_type_attr,']'),'},');

  END LOOP get_data;
  CLOSE c_product_type_part;

  SET v_product_type_part=SUBSTR(v_product_type_part,1,CHAR_LENGTH(v_product_type_part)-1);

  RETURN v_product_type_part;
END $$

DELIMITER ;

-- GET_PRODUCT_PARAM_PART

DELIMITER $$

CREATE FUNCTION `get_product_param_part`(
  p_product_id INTEGER,
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE v_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_value VARCHAR(64);
  DECLARE v_part_value VARCHAR(64);
  DECLARE v_price VARCHAR(16);
  DECLARE v_param_attr MEDIUMTEXT;

  DECLARE v_product_param_part MEDIUMTEXT DEFAULT '';

  DECLARE c_product_param_part CURSOR FOR
  SELECT pp.id,
         replace_text(pp.name),
         IF(pp.value IS NULL,'',replace_text(pp.value)),
         IF(ppp.value IS NULL,'',replace_text(ppp.value)),
         ppp.price,get_attr_part(pp.id,'product_param',p_language)
  FROM product_param pp,product_param_part ppp
  WHERE ppp.product_id=p_product_id AND ppp.product_param_id=pp.id
  ORDER BY pp.id;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_product_param_part;
  get_data: LOOP

  FETCH c_product_param_part INTO v_id,v_name,v_value,v_part_value,v_price,v_param_attr;

  IF v_finished=1 THEN LEAVE get_data;
  END IF;

  -- json
  SET v_product_param_part=CONCAT(v_product_param_part,'{',
      CONCAT(CHAR(34),'param_id',CHAR(34),':',v_id,',',
      CHAR(34),'param_name',CHAR(34),':',CHAR(34),v_name,CHAR(34),',',
      CHAR(34),'param_value',CHAR(34),':',CHAR(34),v_value,CHAR(34),',',
      CHAR(34),'param_part_value',CHAR(34),':',CHAR(34),v_part_value,CHAR(34),',',
      CHAR(34),'param_part_price',CHAR(34),':',v_price,',',
      CHAR(34),'param_attr',CHAR(34),':[',v_param_attr,']'),'},');

  END LOOP get_data;
  CLOSE c_product_param_part;

  SET v_product_param_part=SUBSTR(v_product_param_part,1,CHAR_LENGTH(v_product_param_part)-1);

  RETURN v_product_param_part;
END $$

DELIMITER ;

-- GET_ORDER_AB_PRODUCT_PARAM_PART

DELIMITER $$

CREATE FUNCTION `get_order_AB_product_param_part`(
  p_order_id INTEGER,
  p_product_id INTEGER,
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE v_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_value VARCHAR(64);
  DECLARE v_part_value VARCHAR(64);
  DECLARE v_count VARCHAR(10);
  DECLARE v_price VARCHAR(16);
  DECLARE v_param_attr MEDIUMTEXT;

  DECLARE v_order_AB_product_param_part MEDIUMTEXT DEFAULT '';

  DECLARE c_order_AB_product_param_part CURSOR FOR
  SELECT pp.id,
         replace_text(pp.name),
         IF(pp.value IS NULL,'',replace_text(pp.value)),
         IF(ppp.value IS NULL,'',replace_text(ppp.value)),
         oppp.count,oppp.price,get_attr_part(pp.id,'product_param',p_language)
  FROM product_param pp,product_param_part ppp,order_AB_product_param_part oppp
  WHERE oppp.order_id=p_order_id AND oppp.product_id=p_product_id AND
        oppp.product_id=ppp.product_id AND oppp.product_param_id=ppp.product_param_id AND ppp.product_param_id=pp.id
  ORDER BY pp.id;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_order_AB_product_param_part;
  get_data: LOOP

  FETCH c_order_AB_product_param_part INTO v_id,v_name,v_value,v_part_value,v_count,v_price,v_param_attr;

  IF v_finished=1 THEN LEAVE get_data;
  END IF;

  -- json
  SET v_order_AB_product_param_part=CONCAT(v_order_AB_product_param_part,'{',
      CONCAT(CHAR(34),'param_id',CHAR(34),':',v_id,',',
      CHAR(34),'param_name',CHAR(34),':',CHAR(34),v_name,CHAR(34),',',
      CHAR(34),'param_value',CHAR(34),':',CHAR(34),v_value,CHAR(34),',',
      CHAR(34),'param_part_value',CHAR(34),':',CHAR(34),v_part_value,CHAR(34),',',
      CHAR(34),'param_part_count',CHAR(34),':',v_count,',',
      CHAR(34),'param_part_price',CHAR(34),':',v_price,',',
      CHAR(34),'param_attr',CHAR(34),':[',v_param_attr,']'),'},');

  END LOOP get_data;
  CLOSE c_order_AB_product_param_part;

  SET v_order_AB_product_param_part=SUBSTR(v_order_AB_product_param_part,1,CHAR_LENGTH(v_order_AB_product_param_part)-1);

  RETURN v_order_AB_product_param_part;
END $$

DELIMITER ;

-- GET_STORE_PART

DELIMITER $$

CREATE FUNCTION `get_store_part`(
  p_store_id INTEGER,
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE v_id INTEGER;
  DECLARE v_store_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_description VARCHAR(128);
  DECLARE v_latitude VARCHAR(20);
  DECLARE v_longitude VARCHAR(20);
  DECLARE v_email VARCHAR(64);
  DECLARE v_phone1 VARCHAR(20);
  DECLARE v_phone2 VARCHAR(20);
  DECLARE v_address VARCHAR(128);
  DECLARE v_city VARCHAR(64);
  DECLARE v_postcode VARCHAR(12);

  DECLARE v_store_part MEDIUMTEXT DEFAULT '';

  DECLARE c_store_part CURSOR FOR
  SELECT id,
         store_id,
         replace_text(name),
         replace_text(description),
         IFNULL(latitude,'null'),IFNULL(longitude,'null'),IFNULL(email,''),IFNULL(phone1,''),IFNULL(phone2,''),
         IF(address IS NULL,'',replace_text(address)),
         IF(city IS NULL,'',replace_text(city)),
         IFNULL(postcode,'')
  FROM store_part
  WHERE store_id=p_store_id AND language LIKE p_language
  ORDER BY id;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_store_part;
  get_data: LOOP

  FETCH c_store_part INTO v_id,v_store_id,v_name,v_description,v_latitude,v_longitude,v_email,v_phone1,v_phone2,v_address,v_city,v_postcode;

  IF v_finished=1 THEN LEAVE get_data;
  END IF;

  -- json
  SET v_store_part=CONCAT(v_store_part,'{',
      CONCAT(CHAR(34),'part_id',CHAR(34),':',v_id,',',
      CHAR(34),'store_id',CHAR(34),':',v_store_id,',',
      CHAR(34),'part_name',CHAR(34),':',CHAR(34),v_name,CHAR(34),',',
      CHAR(34),'description',CHAR(34),':',CHAR(34),v_description,CHAR(34),',',
      CHAR(34),'latitude',CHAR(34),':',v_latitude,',',
      CHAR(34),'longitude',CHAR(34),':',v_longitude,',',
      CHAR(34),'email',CHAR(34),':',CHAR(34),v_email,CHAR(34),',',
      CHAR(34),'phone1',CHAR(34),':',CHAR(34),v_phone1,CHAR(34),',',
      CHAR(34),'phone2',CHAR(34),':',CHAR(34),v_phone2,CHAR(34),',',
      CHAR(34),'address',CHAR(34),':',CHAR(34),v_address,CHAR(34),',',
      CHAR(34),'city',CHAR(34),':',CHAR(34),v_city,CHAR(34),',',
      CHAR(34),'postcode',CHAR(34),':',CHAR(34),v_postcode,CHAR(34)),'},');

  END LOOP get_data;
  CLOSE c_store_part;

  SET v_store_part=SUBSTR(v_store_part,1,CHAR_LENGTH(v_store_part)-1);

  RETURN v_store_part;
END $$

DELIMITER ;

-- GET_TRANSPORT

DELIMITER $$

CREATE FUNCTION `get_transport`(
  p_transport_id INTEGER,
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_id INTEGER;
  DECLARE v_user_id VARCHAR(10);
  DECLARE v_sensor_id VARCHAR(10);
  DECLARE v_sensor_active VARCHAR(5);
  DECLARE v_sensor MEDIUMTEXT;
  DECLARE v_type_part MEDIUMTEXT;
  DECLARE v_transport_name VARCHAR(128);
  DECLARE v_transport_color VARCHAR(32);
  DECLARE v_license_plate VARCHAR(12);
  DECLARE v_transport_rate VARCHAR(10);
  DECLARE v_transport_reserved INTEGER;

  DECLARE v_transport MEDIUMTEXT DEFAULT '';

  SELECT t.id,IFNULL(u.id,'null'),IFNULL(t.sensor_id,'null'),IF(s.active=1,'true','false'),
         IF(t.sensor_id IS NULL,'',get_sensor(t.sensor_id,p_language)),
         get_transport_type_part(t.id,p_language),
         replace_text(t.name),
         IF(t.color IS NULL,'',replace_text(t.color)),
         IF(t.license_plate IS NULL,'',replace_text(t.license_plate)),
         IF(t.review_count=0,0,ROUND(t.review_value/t.review_count,2)),
         is_transport_reserved(t.id)
  INTO v_id,v_user_id,v_sensor_id,v_sensor_active,v_sensor,v_type_part,v_transport_name,v_transport_color,v_license_plate,v_transport_rate,v_transport_reserved
  FROM transport t LEFT JOIN sensor s ON s.id=t.sensor_id LEFT JOIN user u ON u.id=s.user_id
  WHERE t.id=p_transport_id;

  -- json
  SET v_transport=CONCAT('{',
      CONCAT(CHAR(34),'transport_id',CHAR(34),':',v_id,',',
      CHAR(34),'user_id',CHAR(34),':',v_user_id,',',
      CHAR(34),'sensor_id',CHAR(34),':',v_sensor_id,',',
      CHAR(34),'sensor_active',CHAR(34),':',v_sensor_active,',',
      CHAR(34),'sensor',CHAR(34),':[',v_sensor,'],',
      CHAR(34),'type_part',CHAR(34),':[',v_type_part,'],',
      CHAR(34),'transport_name',CHAR(34),':',CHAR(34),v_transport_name,CHAR(34),',',
      CHAR(34),'transport_color',CHAR(34),':',CHAR(34),v_transport_color,CHAR(34),',',
      CHAR(34),'license_plate',CHAR(34),':',CHAR(34),v_license_plate,CHAR(34),',',
      CHAR(34),'transport_rate',CHAR(34),':',v_transport_rate,',',
      CHAR(34),'transport_reserved',CHAR(34),':',v_transport_reserved),'},');
  IF v_transport IS NULL THEN
    SET v_transport='';
  ELSE
    SET v_transport=SUBSTR(v_transport,1,CHAR_LENGTH(v_transport)-1);
  END IF;

  -- ONE TRANSPORT RETURN
  RETURN v_transport;
END $$

DELIMITER ;

-- GET_TRANSPORT_TYPE_PART

DELIMITER $$

CREATE FUNCTION `get_transport_type_part`(
  p_transport_id INTEGER,
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE v_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_description VARCHAR(128);
  DECLARE v_type_attr MEDIUMTEXT;

  DECLARE v_transport_type_part MEDIUMTEXT DEFAULT '';

  DECLARE c_transport_type_part CURSOR FOR
  SELECT tt.id, -- tt.parent_id
         replace_text(tt.name),
         replace_text(tt.description),
         get_attr_part(tt.id,'transport_type',p_language)
  FROM transport_type tt,transport_type_part ttp
  WHERE ttp.transport_id=p_transport_id AND ttp.transport_type_id=tt.id
  ORDER BY tt.id;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_transport_type_part;
  get_data: LOOP

  FETCH c_transport_type_part INTO v_id,v_name,v_description,v_type_attr;

  IF v_finished=1 THEN LEAVE get_data;
  END IF;

  -- json
  SET v_transport_type_part=CONCAT(v_transport_type_part,'{',
      CONCAT(CHAR(34),'type_id',CHAR(34),':',v_id,',',
      CHAR(34),'type_name',CHAR(34),':',CHAR(34),v_name,CHAR(34),',',
      CHAR(34),'description',CHAR(34),':',CHAR(34),v_description,CHAR(34),',',
      CHAR(34),'type_attr',CHAR(34),':[',v_type_attr,']'),'},');

  END LOOP get_data;
  CLOSE c_transport_type_part;

  SET v_transport_type_part=SUBSTR(v_transport_type_part,1,CHAR_LENGTH(v_transport_type_part)-1);

  RETURN v_transport_type_part;
END $$

DELIMITER ;

-- GET_SENSOR_GROUP_PART

DELIMITER $$

CREATE FUNCTION `get_sensor_group_part`(
  p_sensor_group_id INTEGER,
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE v_id INTEGER;
  DECLARE v_user_id INTEGER;
  DECLARE v_user MEDIUMTEXT;
  DECLARE v_name VARCHAR(32);
  DECLARE v_serial_number VARCHAR(64);
  DECLARE v_device_name VARCHAR(64);
  DECLARE v_phone VARCHAR(20);
  DECLARE v_active VARCHAR(5);

  DECLARE v_sensor_group_part MEDIUMTEXT DEFAULT '';

  DECLARE c_sensor_group_part CURSOR FOR
  SELECT s.id,s.user_id,get_user(s.user_id,p_language),
         replace_text(s.name),
         IFNULL(s.serial_number,''),IFNULL(s.device_name,''),IFNULL(s.phone,''),IF(s.active=1,'true','false')
  FROM sensor s,sensor_group_part sgp
  WHERE sgp.sensor_group_id=p_sensor_group_id AND sgp.sensor_id=s.id
  ORDER BY s.id;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_sensor_group_part;
  get_data: LOOP

  FETCH c_sensor_group_part INTO v_id,v_user_id,v_user,v_name,v_serial_number,v_device_name,v_phone,v_active;

  IF v_finished=1 THEN LEAVE get_data;
  END IF;

  -- json
  SET v_sensor_group_part=CONCAT(v_sensor_group_part,'{',
      CONCAT(CHAR(34),'sensor_id',CHAR(34),':',v_id,',',
      CHAR(34),'user_id',CHAR(34),':',v_user_id,',',
      CHAR(34),'user',CHAR(34),':[',v_user,'],',
      CHAR(34),'sensor_name',CHAR(34),':',CHAR(34),v_name,CHAR(34),',',
      CHAR(34),'serial_number',CHAR(34),':',CHAR(34),v_serial_number,CHAR(34),',',
      CHAR(34),'device_name',CHAR(34),':',CHAR(34),v_device_name,CHAR(34),',',
      CHAR(34),'phone',CHAR(34),':',CHAR(34),v_phone,CHAR(34),',',
      CHAR(34),'active',CHAR(34),':',v_active),'},');

  END LOOP get_data;
  CLOSE c_sensor_group_part;

  SET v_sensor_group_part=SUBSTR(v_sensor_group_part,1,CHAR_LENGTH(v_sensor_group_part)-1);

  RETURN v_sensor_group_part;
END $$

DELIMITER ;

-- GET_SENSOR_CIRCLE

DELIMITER $$

CREATE FUNCTION `get_sensor_circle`(
  p_sensor_id INTEGER,
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE v_id INTEGER;
  DECLARE v_user_id INTEGER;
  DECLARE v_user MEDIUMTEXT;
  DECLARE v_name VARCHAR(32);
  DECLARE v_serial_number VARCHAR(64);
  DECLARE v_device_name VARCHAR(64);
  DECLARE v_phone VARCHAR(20);
  DECLARE v_active VARCHAR(5);

  DECLARE v_sensor_circle MEDIUMTEXT DEFAULT '';

  DECLARE c_sensor_circle CURSOR FOR
  SELECT s.id,s.user_id,get_user(s.user_id,p_language),
         replace_text(s.name),
         IFNULL(s.serial_number,''),IFNULL(s.device_name,''),IFNULL(s.phone,''),IF(s.active=1,'true','false')
  FROM sensor s,sensor_circle sc
  WHERE sc.sensorA_id=p_sensor_id AND sc.sensorB_id=s.id
  ORDER BY s.id;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_sensor_circle;
  get_data: LOOP

  FETCH c_sensor_circle INTO v_id,v_user_id,v_user,v_name,v_serial_number,v_device_name,v_phone,v_active;

  IF v_finished=1 THEN LEAVE get_data;
  END IF;

  -- json
  SET v_sensor_circle=CONCAT(v_sensor_circle,'{',
      CONCAT(CHAR(34),'sensor_id',CHAR(34),':',v_id,',',
      CHAR(34),'user_id',CHAR(34),':',v_user_id,',',
      CHAR(34),'user',CHAR(34),':[',v_user,'],',
      CHAR(34),'sensor_name',CHAR(34),':',CHAR(34),v_name,CHAR(34),',',
      CHAR(34),'serial_number',CHAR(34),':',CHAR(34),v_serial_number,CHAR(34),',',
      CHAR(34),'device_name',CHAR(34),':',CHAR(34),v_device_name,CHAR(34),',',
      CHAR(34),'phone',CHAR(34),':',CHAR(34),v_phone,CHAR(34),',',
      CHAR(34),'active',CHAR(34),':',v_active),'},');

  END LOOP get_data;
  CLOSE c_sensor_circle;

  SET v_sensor_circle=SUBSTR(v_sensor_circle,1,CHAR_LENGTH(v_sensor_circle)-1);

  RETURN v_sensor_circle;
END $$

DELIMITER ;

-- GET_SENSOR

DELIMITER $$

CREATE FUNCTION `get_sensor`(
  p_sensor_id INTEGER,
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_id INTEGER;
  DECLARE v_user_id INTEGER;
  DECLARE v_user MEDIUMTEXT;
  DECLARE v_sensor_circle MEDIUMTEXT;
  DECLARE v_name VARCHAR(32);
  DECLARE v_serial_number VARCHAR(64);
  DECLARE v_device_name VARCHAR(64);
  DECLARE v_phone VARCHAR(20);
  DECLARE v_active VARCHAR(5);

  DECLARE v_sensor MEDIUMTEXT DEFAULT '';

  SELECT id,user_id,get_user(user_id,p_language),/*get_sensor_circle(id,p_language)*/'',
         replace_text(name),
         IFNULL(serial_number,''),IFNULL(device_name,''),IFNULL(phone,''),IF(active=1,'true','false')
  INTO v_id,v_user_id,v_user,v_sensor_circle,v_name,v_serial_number,v_device_name,v_phone,v_active
  FROM sensor
  WHERE id=p_sensor_id;

  -- json
  SET v_sensor=CONCAT('{',
      CONCAT(CHAR(34),'sensor_id',CHAR(34),':',v_id,',',
      CHAR(34),'user_id',CHAR(34),':',v_user_id,',',
      CHAR(34),'user',CHAR(34),':[',v_user,'],',
      CHAR(34),'sensor_circle',CHAR(34),':[',v_sensor_circle,'],',
      CHAR(34),'sensor_name',CHAR(34),':',CHAR(34),v_name,CHAR(34),',',
      CHAR(34),'serial_number',CHAR(34),':',CHAR(34),v_serial_number,CHAR(34),',',
      CHAR(34),'device_name',CHAR(34),':',CHAR(34),v_device_name,CHAR(34),',',
      CHAR(34),'phone',CHAR(34),':',CHAR(34),v_phone,CHAR(34),',',
      CHAR(34),'active',CHAR(34),':',v_active),'},');

  IF v_sensor IS NULL THEN
    SET v_sensor='';
  ELSE
    SET v_sensor=SUBSTR(v_sensor,1,CHAR_LENGTH(v_sensor)-1);
  END IF;

  -- ONE SENSOR RETURN
  RETURN v_sensor;
END $$

DELIMITER ;

-- GET_TRACK_PART

DELIMITER $$

CREATE FUNCTION `get_track_part`(
  p_track_id INTEGER,
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE v_id INTEGER;
  DECLARE v_name VARCHAR(32);
  DECLARE v_description VARCHAR(128);

  DECLARE v_track_part MEDIUMTEXT DEFAULT '';

  DECLARE c_track_part CURSOR FOR
  SELECT id,
         replace_text(name),
         replace_text(description)
  FROM track_part
  WHERE track_id=p_track_id AND language LIKE p_language
  ORDER BY id;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_track_part;
  get_data: LOOP

  FETCH c_track_part INTO v_id,v_name,v_description;

  IF v_finished=1 THEN LEAVE get_data;
  END IF;

  -- json
  SET v_track_part=CONCAT(v_track_part,'{',
      CONCAT(CHAR(34),'type_id',CHAR(34),':',v_id,',',
      CHAR(34),'type_name',CHAR(34),':',CHAR(34),v_name,CHAR(34),',',
      CHAR(34),'description',CHAR(34),':',CHAR(34),v_description,CHAR(34)),'},');

  END LOOP get_data;
  CLOSE c_track_part;

  SET v_track_part=SUBSTR(v_track_part,1,CHAR_LENGTH(v_track_part)-1);

  RETURN v_track_part;
END $$

DELIMITER ;

-- GET_STOCK_COUNT

DELIMITER $$

CREATE FUNCTION `get_stock_count`(
  p_product_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;
  DECLARE v_count INTEGER;
  DECLARE v_stock_count INTEGER DEFAULT 0;

  DECLARE c_stock CURSOR FOR
  SELECT count
  FROM product p,stock s
  WHERE p.id=p_product_id AND s.product_id=p.id;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_stock;
  get_data: LOOP

  FETCH c_stock INTO v_count;

  IF v_finished=1 THEN LEAVE get_data;
  END IF;

  IF v_count IS NULL THEN SET v_count=0; END IF;
  SET v_stock_count=v_stock_count+v_count;

  END LOOP get_data;
  CLOSE c_stock;

  RETURN v_stock_count;
END $$

DELIMITER ;

-- GET_STOCK_COUNT

DELIMITER $$

CREATE FUNCTION `get_stock_count_in_store`(
  p_product_id INTEGER,
  p_store_id INTEGER) RETURNS INTEGER
BEGIN
  DECLARE v_count INTEGER DEFAULT 0;

  SELECT IF(s.count IS NULL,0,count) INTO v_count
  FROM product p,stock s
  WHERE p.id=p_product_id AND s.product_id=p.id AND s.store_id=p_store_id;

  RETURN v_count;
END $$

DELIMITER ;

-- GET_USER

DELIMITER $$

CREATE FUNCTION `get_user`(
  p_user_id INTEGER,
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_id INTEGER;
  DECLARE v_type VARCHAR(2);
  DECLARE v_discount_code VARCHAR(8);
  DECLARE v_discount MEDIUMTEXT;
  DECLARE v_first_name VARCHAR(32);
  DECLARE v_last_name VARCHAR(32);
  DECLARE v_call_name VARCHAR(32);
  DECLARE v_email VARCHAR(64);
  DECLARE v_phone VARCHAR(20);
  DECLARE v_rate VARCHAR(10);
  DECLARE v_active VARCHAR(5);

  DECLARE v_user MEDIUMTEXT DEFAULT '';

  SELECT id,IFNULL(discount_code,''),IF(discount_code IS NULL,'',get_discount(discount_code,p_language)),type,
         replace_text(first_name),
         replace_text(last_name),
         replace_text(call_name),
         IFNULL(email,''),IFNULL(phone,''),IF(review_count=0,0,ROUND(review_value/review_count,2)),IF(active=1,'true','false')
  INTO v_id,v_discount_code,v_discount,v_type,v_first_name,v_last_name,v_call_name,v_email,v_phone,v_rate,v_active
  FROM user
  WHERE id=p_user_id;

  -- json
  SET v_user=CONCAT('{',
      CONCAT(CHAR(34),'user_id',CHAR(34),':',v_id,',',
      CHAR(34),'user_type',CHAR(34),':',v_type,',',
      CHAR(34),'discount_code',CHAR(34),':',CHAR(34),v_discount_code,CHAR(34),',',
      CHAR(34),'discount',CHAR(34),':[',v_discount,']',',',
      CHAR(34),'first_name',CHAR(34),':',CHAR(34),v_first_name,CHAR(34),',',
      CHAR(34),'last_name',CHAR(34),':',CHAR(34),v_last_name,CHAR(34),',',
      CHAR(34),'call_name',CHAR(34),':',CHAR(34),v_call_name,CHAR(34),',',
      CHAR(34),'email',CHAR(34),':',CHAR(34),v_email,CHAR(34),',',
      CHAR(34),'phone',CHAR(34),':',CHAR(34),v_phone,CHAR(34),',',
      CHAR(34),'user_rate',CHAR(34),':',v_rate,',',
      CHAR(34),'active',CHAR(34),':',v_active),'},');

  IF v_user IS NULL THEN
    SET v_user='';
  ELSE
    SET v_user=SUBSTR(v_user,1,CHAR_LENGTH(v_user)-1);
  END IF;

  -- ONE USER RETURN
  RETURN v_user;
END $$

DELIMITER ;

-- GET_DISCOUNT

DELIMITER $$

CREATE FUNCTION `get_discount`(
  p_discount_code VARCHAR(8),
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_id INTEGER;
  DECLARE v_product_type_id VARCHAR(10);
  DECLARE v_discount_attr MEDIUMTEXT;
  DECLARE v_type VARCHAR(2);
  DECLARE v_code VARCHAR(8);
  DECLARE v_name VARCHAR(32);
  DECLARE v_value VARCHAR(10);
  DECLARE v_language VARCHAR(32);
  DECLARE v_start_date VARCHAR(20);
  DECLARE v_finish_date VARCHAR(20);

  DECLARE v_discount MEDIUMTEXT DEFAULT '';

  SELECT id,IFNULL(product_type_id,'null'),get_attr_part(id,'discount',p_language),type,code,
         replace_text(name),
         value,IFNULL(language,''),IFNULL(start_date,''),IFNULL(finish_date,'')
  INTO v_id,v_product_type_id,v_discount_attr,v_type,v_code,v_name,v_value,v_language,v_start_date,v_finish_date
  FROM discount
  WHERE code=p_discount_code;

  -- json
  SET v_discount=CONCAT('{',
      CONCAT(CHAR(34),'discount_id',CHAR(34),':',v_id,',',
      CHAR(34),'product_type_id',CHAR(34),':',v_product_type_id,',',
      CHAR(34),'discount_attr',CHAR(34),':[',v_discount_attr,']',',',
      CHAR(34),'discount_type',CHAR(34),':',CHAR(34),v_type,CHAR(34),',',
      CHAR(34),'discount_code',CHAR(34),':',CHAR(34),v_code,CHAR(34),',',
      CHAR(34),'discount_name',CHAR(34),':',CHAR(34),v_name,CHAR(34),',',
      CHAR(34),'discount_value',CHAR(34),':',CHAR(34),v_value,CHAR(34),',',
      CHAR(34),'language',CHAR(34),':',CHAR(34),v_language,CHAR(34),',',
      CHAR(34),'start_date',CHAR(34),':',CHAR(34),v_start_date,CHAR(34),',',
      CHAR(34),'finish_date',CHAR(34),':',CHAR(34),v_finish_date,CHAR(34)),'},');

  IF v_discount IS NULL THEN
    SET v_discount='';
  ELSE
    SET v_discount=SUBSTR(v_discount,1,CHAR_LENGTH(v_discount)-1);
  END IF;

  -- ONE DISCOUNT RETURN
  RETURN v_discount;
END $$

DELIMITER ;

-- GET_PURCHASE

DELIMITER $$

CREATE FUNCTION `get_purchase`(
  p_order_id INTEGER,
  p_language VARCHAR(32)) RETURNS MEDIUMTEXT
BEGIN
  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE v_id INTEGER;
  DECLARE v_user_id INTEGER;
  DECLARE v_invoice_code VARCHAR(32);
  DECLARE v_invoice_date VARCHAR(20);
  DECLARE v_total_price VARCHAR(16);
  DECLARE v_total_tax VARCHAR(16);
  DECLARE v_order_id VARCHAR(10);
  DECLARE v_order_date VARCHAR(20);
  DECLARE v_order_info VARCHAR(128);
  DECLARE v_delivery_id VARCHAR(10);
  DECLARE v_delivery_code VARCHAR(32);
  DECLARE v_delivery_type_id VARCHAR(2);
  DECLARE v_delivery_type_name VARCHAR(32);
  DECLARE v_delivery_type_attr MEDIUMTEXT;
  DECLARE v_delivery_date VARCHAR(20);
  DECLARE v_delivery_price VARCHAR(16);
  DECLARE v_payment_date VARCHAR(20);
  DECLARE v_payment_info VARCHAR(128);
  DECLARE v_payment_amount VARCHAR(16);

  DECLARE v_purchase MEDIUMTEXT DEFAULT '';

  DECLARE c_purchase CURSOR FOR
  SELECT p.id,p.user_id,IF(p.invoice_code IS NULL,'',replace_text(p.invoice_code)),
         IFNULL(p.invoice_date,''),p.total_price,p.total_tax,IFNULL(p.order_id,'null'),IFNULL(p.order_date,''),
         replace_text(p.order_info),IFNULL(p.delivery_id,'null'),
         IF(p.delivery_code IS NULL,'',replace_text(p.delivery_code)),
         IFNULL(p.delivery_type_id,'null'),
         IF(p.delivery_type_id IS NULL,'',replace_text(dt.name)),
         IF(p.delivery_type_id IS NULL,'',get_attr_part(p.delivery_type_id,'delivery_type',p_language)),
         IFNULL(p.delivery_date,''),IFNULL(p.delivery_price,'null'),IFNULL(p.payment_date,''),
         replace_text(p.payment_info),IFNULL(p.payment_amount,'null')
  FROM purchase p LEFT JOIN delivery_type dt ON dt.id=p.delivery_type_id
  WHERE p.order_id=p_order_id
  ORDER BY p.id;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;

  OPEN c_purchase;
  get_data: LOOP

  FETCH c_purchase INTO v_id,v_user_id,v_invoice_code,v_invoice_date,v_total_price,v_total_tax,
        v_order_id,v_order_date,v_order_info,
        v_delivery_id,v_delivery_code,v_delivery_type_id,v_delivery_type_name,v_delivery_type_attr,
        v_delivery_date,v_delivery_price,v_payment_date,v_payment_info,v_payment_amount;

  IF v_finished=1 THEN LEAVE get_data;
  END IF;

  -- json
  SET v_purchase=CONCAT(v_purchase,'{',
      CHAR(34),'purchase_id',CHAR(34),':',v_id,',',
      CHAR(34),'user_id',CHAR(34),':',v_user_id,',',
      CHAR(34),'invoice_code',CHAR(34),':',CHAR(34),v_invoice_code,CHAR(34),',',
      CHAR(34),'invoice_date',CHAR(34),':',CHAR(34),v_invoice_date,CHAR(34),',',
      CHAR(34),'total_price',CHAR(34),':',v_total_price,',',
      CHAR(34),'total_tax',CHAR(34),':',v_total_tax,',',
      CHAR(34),'order_id',CHAR(34),':',v_order_id,',',
      CHAR(34),'order_date',CHAR(34),':',CHAR(34),v_order_date,CHAR(34),',',
      CHAR(34),'order_info',CHAR(34),':',CHAR(34),v_order_info,CHAR(34),',',
      CHAR(34),'delivery_id',CHAR(34),':',v_delivery_id,',',
      CHAR(34),'delivery_code',CHAR(34),':',CHAR(34),v_delivery_code,CHAR(34),',',
      CHAR(34),'delivery_type_id',CHAR(34),':',v_delivery_type_id,',',
      CHAR(34),'delivery_type_name',CHAR(34),':',CHAR(34),v_delivery_type_name,CHAR(34),',',
      CHAR(34),'delivery_type_attr',CHAR(34),':[',v_delivery_type_attr,'],',
      CHAR(34),'delivery_date',CHAR(34),':',CHAR(34),v_delivery_date,CHAR(34),',',
      CHAR(34),'delivery_price',CHAR(34),':',v_delivery_price,',',
      CHAR(34),'payment_date',CHAR(34),':',CHAR(34),v_payment_date,CHAR(34),',',
      CHAR(34),'payment_info',CHAR(34),':',CHAR(34),v_payment_info,CHAR(34),',',
      CHAR(34),'payment_amount',CHAR(34),':',v_payment_amount,'},');

  END LOOP get_data;
  CLOSE c_purchase;

  SET v_purchase=SUBSTR(v_purchase,1,CHAR_LENGTH(v_purchase)-1);

  RETURN v_purchase;
END $$

DELIMITER ;

-- [hext wishes here]
-- -------------------------------------------------------------------------------------------------------------

-- [end of objects]
-- /////////////////////////////////////////////////////////////////////////////////////////////////////////////

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;