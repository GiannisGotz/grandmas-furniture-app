-- Grandma's Furniture App Database Schema
-- MySQL Database Schema

CREATE DATABASE IF NOT EXISTS grandmas_furniture_app
CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;
USE grandmas_furniture_app;

-- Drop tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS ads;
DROP TABLE IF EXISTS attachments;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS categories;

-- Categories table (static data)
CREATE TABLE categories (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Cities table (static data)
CREATE TABLE cities (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    city_name VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Users table
CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Attachments table
CREATE TABLE attachments (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    filename VARCHAR(255),
    saved_name VARCHAR(255),
    file_path VARCHAR(500),
    content_type VARCHAR(100),
    extension VARCHAR(10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Ads table
CREATE TABLE ads (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    category_id BIGINT NOT NULL,
    city_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    condition ENUM('EXCELLENT', 'GOOD', 'AGE_WORN', 'DAMAGED'),
    price DECIMAL(10,2),
    is_available BOOLEAN DEFAULT TRUE,
    description TEXT,
    attachment_id BIGINT UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_ads_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_ads_city FOREIGN KEY (city_id) REFERENCES cities(id),
    CONSTRAINT fk_ads_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_ads_attachment FOREIGN KEY (attachment_id) REFERENCES attachments(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Insert sample categories
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

-- Insert sample cities
INSERT INTO cities (city_name) VALUES 
('Athens'),
('Piraeus'),
('Marousi'),
('Kifisia'),
('Nea Smyrni'),
('Glyfada'),
('Chalandri'),
('Thessaloniki'),
('Patras'),
('Heraklion');

-- Create indexes for better performance
CREATE INDEX idx_ads_category ON ads(category_id);
CREATE INDEX idx_ads_city ON ads(city_id);
CREATE INDEX idx_ads_user ON ads(user_id);
CREATE INDEX idx_ads_available ON ads(is_available);
CREATE INDEX idx_ads_attachment ON ads(attachment_id);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);