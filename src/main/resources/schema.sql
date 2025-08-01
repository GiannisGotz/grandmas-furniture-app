-- Grandma's Furniture App Database Schema
-- MySQL Database Schema

CREATE DATABASE IF NOT EXISTS grandmas_furniture_app;
USE grandmas_furniture_app;

-- Drop tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS ads;
DROP TABLE IF EXISTS attachment;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS categories;

-- Categories table (static data)
CREATE TABLE categories (
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(255) UNIQUE NOT NULL
);

-- Cities table (static data)
CREATE TABLE cities (
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON  UPDATE CURRENT_TIMESTAMP,
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    city_name VARCHAR(255) UNIQUE NOT NULL
    
);

-- Users table
CREATE TABLE users (    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    is_active BOOLEAN DEFAULT TRUE
);

-- Attachment table (created first due to foreign key dependency)
CREATE TABLE attachment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    filename VARCHAR(255),
    saved_name VARCHAR(255),
    file_path VARCHAR(500),
    content_type VARCHAR(100),
    extension VARCHAR(10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Ads table
CREATE TABLE ads (
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    title VARCHAR(255),
    category_id BIGINT,
    city_id BIGINT,
    condition ENUM('EXCELLENT', 'GOOD', 'AGE_WORN', 'DAMAGED'),
    price DECIMAL(10,2),
    is_available BOOLEAN DEFAULT TRUE,
    description TEXT,
    attachment_id BIGINT UNIQUE,
    
    CONSTRAINT fk_ads_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_ads_city FOREIGN KEY (city_id) REFERENCES cities(id),
    CONSTRAINT fk_ads_attachment FOREIGN KEY (attachment_id) REFERENCES attachment(id)
);


INSERT INTO categories (category) VALUES
('Chairs'),
('Tables'),
('Cabinets'),
('Sofas'),
('Desks'),
('Dressers'),
('Beds'),
('Sideboards'),
('Wardrobes'),
('Bookcases'),
('Antique Clocks'),
('Decorative Items');

INSERT INTO cities (city_name) VALUES
('Athens'),
('Piraeus'),
('Marousi'),
('Kifisia'),
('Nea Smyrni'),
('Glyfada'),
('Chalandri'),
('Peristeri'),
('Pallini'),
('Voula'),
('Elefsina'),
('Kalamata'),
('Patras'),
('Thessaloniki'),
('Larissa'),
('Volos'),
('Ioannina'),
('Chania'),
('Heraklion'),
('Rhodes'),
('Irakleio'),
('Corfu'),
('Agrinio'),
('Serres'),
('Kavala'),
('Alexandroupoli'),
('Tripoli'),
('Trikala'),
('Katerini'),
('Rethymno'),
('Nafplio'),
('Sparta'),
('Kalambaka'),
('Loutraki'),
('Edessa'),
('Preveza'),
('Zakynthos'),
('Mykonos'),
('Santorini'),
('Naxos');

-- Create indexes for better performance
CREATE INDEX idx_ads_category ON ads(category_id);
CREATE INDEX idx_ads_city ON ads(city_id);
CREATE INDEX idx_ads_available ON ads(is_available);
CREATE INDEX idx_ads_attachment ON ads(attachment_id);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);