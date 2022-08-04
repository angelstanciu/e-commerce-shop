CREATE DATABASE coffee_shop;

USE coffee_shop;

CREATE TABLE `users` (
                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                         `first_name` VARCHAR(40) NOT NULL,
                         `last_name` VARCHAR(40) NOT NULL,
                         `email` VARCHAR(100) NOT NULL,
                         `password` VARCHAR(64) NOT NULL,
                         `address` VARCHAR(200) NOT NULL,
                         `phone_number` VARCHAR(15) NOT NULL,
                         `birth_date` DATE NOT NULL,
                         `status` VARCHAR(10) NOT NULL DEFAULT 'PENDING',
                         PRIMARY KEY (`id`),
                         UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);

CREATE TABLE `roles` (
                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                         `name` VARCHAR(15) NOT NULL,
                         PRIMARY KEY (`id`));

CREATE TABLE `users_roles` (
                               `user_id` BIGINT NOT NULL,
                               `role_id` BIGINT NOT NULL,
                               KEY `FK_role_id` (`role_id`),
                               KEY `FK_user_idx` (`user_id`),
                               CONSTRAINT `FK_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
                               CONSTRAINT `FK_user_idx` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`));

CREATE TABLE `categories` (
                              `id` BIGINT NOT NULL AUTO_INCREMENT,
                              `name` VARCHAR(100) NOT NULL,
                              `alias` VARCHAR(100) NOT NULL,
                              `parent_id` BIGINT DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `alias_UNIQUE` (`alias`),
                              KEY `FK_parent_id` (`parent_id`),
                              CONSTRAINT `FK_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`));

CREATE TABLE `products` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT,
                            `name` VARCHAR(200) NOT NULL,
                            `alias` VARCHAR(200) NOT NULL,
                            `description` VARCHAR(3000) NOT NULL,
                            `brand` VARCHAR(80) NOT NULL,
                            `price` FLOAT NOT NULL,
                            `stock` INT NOT NULL,
                            `enabled` BIT(1) NOT NULL,
                            `category_id` BIGINT DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `name_UNIQUE` (`name`),
                            UNIQUE KEY `alias_UNIQUE` (`alias`),
                            KEY `FK_category_id` (`category_id`),
                            CONSTRAINT `FK_category_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE);

CREATE TABLE `technical_details` (
                                     `id` BIGINT NOT NULL AUTO_INCREMENT,
                                     `name` VARCHAR(250) NOT NULL,
                                     `value` VARCHAR(250) NOT NULL,
                                     `product_id` BIGINT NOT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `FK_product_id_idx` (`product_id`),
                                     CONSTRAINT `FK_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`));

CREATE TABLE `orders` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `total` FLOAT NOT NULL,
                          `date` DATE NOT NULL,
                          `user_id` BIGINT NOT NULL,
                          `status` VARCHAR(10) NOT NULL DEFAULT 'PENDING',
                          PRIMARY KEY (`id`),
                          KEY `FK_user_id_idx` (`user_id`),
                          CONSTRAINT `FK_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`));

CREATE TABLE `cart_items` (
                              `id` BIGINT NOT NULL AUTO_INCREMENT,
                              `quantity` TINYINT NOT NULL,
                              `user_id` BIGINT NOT NULL,
                              `product_id` BIGINT NOT NULL,
                              PRIMARY KEY (`id`),
                              KEY `FK_user_id_idx` (`user_id`),
                              KEY `FK_product_id_idx` (`product_id`),
                              CONSTRAINT `FK_user_id_cart` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
                              CONSTRAINT `FK_product_id_cart` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE);

CREATE TABLE `order_details` (
                                 `id` BIGINT NOT NULL AUTO_INCREMENT,
                                 `quantity` TINYINT NOT NULL,
                                 `unit_price` FLOAT NOT NULL,
                                 `subtotal` FLOAT AS (unit_price * quantity),
                                 `product_id` BIGINT NOT NULL,
                                 `order_id` BIGINT NOT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `FK_product_id_order_idx` (`product_id`),
                                 KEY `FK_order_id_idx` (`order_id`) ,
                                 CONSTRAINT `FK_product_id_order` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
                                 CONSTRAINT `FK_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE);