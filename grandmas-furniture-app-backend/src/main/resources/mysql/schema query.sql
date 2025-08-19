-- Grandma's Furniture App Database Schema
-- MySQL Database Schema

CREATE DATABASE IF NOT EXISTS grandmasfurnitureappdb
CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;
USE grandmasfurnitureappdb;

--  Create a new user
CREATE USER 'Giannis'@'localhost' IDENTIFIED BY '12345';

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
    `condition` ENUM('EXCELLENT', 'GOOD', 'AGE_WORN', 'DAMAGED'),
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
('Kolonaki'),
('Exarchia'),
('Plaka'),
('Monastiraki'),
('Psyrri'),
('Gazi'),
('Metaxourgeio'),
('Koukaki'),
('Pangrati'),
('Kypseli'),
('Patissia'),
('Ampelokipoi'),
('Zografou'),
('Ilisia'),
('Goudi'),
('Kaisariani'),
('Vyronas'),
('Dafni'),
('Ymittos'),
('Glyfada'),
('Voula'),
('Vouliagmeni'),
('Varkiza'),
('Alimos'),
('Palaio Faliro'),
('Nea Smyrni'),
('Kallithea'),
('Moschato'),
('Tavros'),
('Piraeus'),
('Nikaia'),
('Korydallos'),
('Perama'),
('Marousi'),
('Kifisia'),
('Nea Erythraia'),
('Ekali'),
('Dionysos'),
('Penteli'),
('Thessaloniki'),
('Patras'),
('Heraklion'),
('Larissa'),
('Volos'),
('Ioannina'),
('Kavala'),
('Chania'),
('Rhodes'),
('Serres');

-- Create indexes for better performance
CREATE INDEX idx_ads_category ON ads(category_id);
CREATE INDEX idx_ads_city ON ads(city_id);
CREATE INDEX idx_ads_user ON ads(user_id);
CREATE INDEX idx_ads_available ON ads(is_available);
CREATE INDEX idx_ads_attachment ON ads(attachment_id);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

-- Insert 20 users (2 admin, 18 regular users)
-- Password: hashed version of Cosmote1@
ALTER TABLE users AUTO_INCREMENT = 1

INSERT INTO users (username, password, first_name, last_name, email, phone, role) VALUES 
('admin1', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Maria', 'Papadopoulos', 'admin1@grandmasfurniture.com', '6932123456', 'ADMIN'), 
('admin2', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Yannis', 'Kostas', 'admin2@grandmasfurniture.com', '6945987654', 'ADMIN'),
('user1', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Elena', 'Dimitriou', 'elena.dimitriou@email.com', '6976543210', 'USER'),
('user2', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Nikos', 'Stavros', 'nikos.stavros@email.com', '6987654321', 'USER'),
('user3', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Anna', 'Georgiou', 'anna.georgiou@email.com', '6998765432', 'USER'),
('user4', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Petros', 'Michalis', 'petros.michalis@email.com', '6944321098', 'USER'),
('user5', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Sofia', 'Vasiliki', 'sofia.vasiliki@email.com', '6955432109', 'USER'),
('user6', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Dimitris', 'Andreas', 'dimitris.andreas@email.com', '6966543210', 'USER'),
('user7', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Ioanna', 'Christina', 'ioanna.christina@email.com', '6977654321', 'USER'),
('user8', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Kostas', 'Spyros', 'kostas.spyros@email.com', '6988765432', 'USER'),
('user9', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Katerina', 'Eleni', 'katerina.eleni@email.com', '6999876543', 'USER'),
('user10', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Alexandros', 'Thanasis', 'alexandros.thanasis@email.com', '6932098765', 'USER'),
('user11', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Despina', 'Fotini', 'despina.fotini@email.com', '6943109876', 'USER'),
('user12', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Giorgos', 'Manolis', 'giorgos.manolis@email.com', '6954210987', 'USER'),
('user13', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Vasiliki', 'Panagiota', 'vasiliki.panagiota@email.com', '6965321098', 'USER'),
('user14', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Christos', 'Apostolos', 'christos.apostolos@email.com', '6976432109', 'USER'),
('user15', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Athina', 'Irini', 'athina.irini@email.com', '6987543210', 'USER'),
('user16', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Stavros', 'Panagiotis', 'stavros.panagiotis@email.com', '6998654321', 'USER'),
('user17', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Margarita', 'Sophia', 'margarita.sophia@email.com', '6932765432', 'USER'),
('user18', '$2a$12$hH5kWM8zaex6qwTCQ6eBD.NspjNSLAZ3WglwkYuxJRL5Q4c9ozbSu', 'Vasilis', 'Ioannis', 'vasilis.ioannis@email.com', '6943876543', 'USER');

-- Insert 100 ads without images
INSERT INTO ads (title, category_id, city_id, user_id, `condition`, price, description) VALUES 
('Vintage Chair', 1, 1, 3, 'GOOD', 45.00, 'Comfortable wooden chair from the 60s'),
('Oak Table', 2, 2, 4, 'EXCELLENT', 120.00, 'Solid oak dining table'),
('Antique Cabinet', 3, 3, 5, 'AGE_WORN', 200.00, 'Beautiful vintage storage cabinet'),
('Red Sofa', 4, 4, 6, 'GOOD', 300.00, 'Three-seater leather sofa'),
('Study Desk', 5, 5, 7, 'EXCELLENT', 80.00, 'Perfect desk for home office'),
('Pine Dresser', 6, 6, 8, 'GOOD', 150.00, 'Six-drawer pine dresser'),
('Double Bed', 7, 7, 9, 'EXCELLENT', 250.00, 'Queen size wooden bed frame'),
('Mahogany Sideboard', 8, 8, 10, 'AGE_WORN', 180.00, 'Classic mahogany serving table'),
('Large Wardrobe', 9, 9, 11, 'GOOD', 220.00, 'Spacious three-door wardrobe'),
('Bookcase', 10, 10, 12, 'EXCELLENT', 90.00, 'Five-shelf wooden bookcase'),
('Wall Clock', 11, 11, 13, 'GOOD', 35.00, 'Antique pendulum wall clock'),
('Crystal Vase', 12, 12, 14, 'EXCELLENT', 25.00, 'Hand-blown decorative crystal vase'),
('Leather Chair', 1, 13, 15, 'GOOD', 60.00, 'Brown leather armchair'),
('Coffee Table', 2, 14, 16, 'EXCELLENT', 75.00, 'Modern glass coffee table'),
('Kitchen Cabinet', 3, 15, 17, 'GOOD', 130.00, 'White kitchen storage cabinet'),
('Blue Sofa', 4, 16, 18, 'AGE_WORN', 180.00, 'Two-seater fabric sofa'),
('Corner Desk', 5, 17, 19, 'EXCELLENT', 95.00, 'L-shaped computer desk'),
('Antique Dresser', 6, 18, 20, 'GOOD', 175.00, 'Victorian-style dresser with mirror'),
('Single Bed', 7, 19, 3, 'EXCELLENT', 120.00, 'Twin size metal bed frame'),
('Small Sideboard', 8, 20, 4, 'GOOD', 100.00, 'Compact dining room sideboard'),
('Tall Wardrobe', 9, 21, 5, 'EXCELLENT', 280.00, 'Two-door tall wardrobe'),
('Corner Bookcase', 10, 22, 6, 'GOOD', 110.00, 'Space-saving corner bookshelf'),
('Mantle Clock', 11, 23, 7, 'AGE_WORN', 50.00, 'Brass mantle clock'),
('Ceramic Figurine', 12, 24, 8, 'EXCELLENT', 15.00, 'Handpainted decorative figurine'),
('Rocking Chair', 1, 25, 9, 'GOOD', 85.00, 'Traditional wooden rocking chair'),
('Round Table', 2, 26, 10, 'EXCELLENT', 140.00, 'Round pedestal dining table'),
('TV Cabinet', 3, 27, 11, 'GOOD', 110.00, 'Entertainment center with storage'),
('Green Sofa', 4, 28, 12, 'GOOD', 250.00, 'Three-seater velvet sofa'),
('Writing Desk', 5, 29, 13, 'EXCELLENT', 70.00, 'Vintage writing desk with drawers'),
('Tall Dresser', 6, 30, 14, 'GOOD', 160.00, 'Seven-drawer tall dresser'),
('Bunk Bed', 7, 31, 15, 'EXCELLENT', 200.00, 'Metal twin bunk bed'),
('Buffet Sideboard', 8, 32, 16, 'GOOD', 190.00, 'Long buffet with cabinet doors'),
('Built-in Wardrobe', 9, 33, 17, 'AGE_WORN', 300.00, 'Custom built-in wardrobe system'),
('Library Bookcase', 10, 34, 18, 'EXCELLENT', 180.00, 'Large library-style bookcase'),
('Cuckoo Clock', 11, 35, 19, 'GOOD', 65.00, 'German cuckoo wall clock'),
('Silver Candlesticks', 12, 36, 20, 'EXCELLENT', 30.00, 'Pair of silver candlestick holders'),
('Office Chair', 1, 37, 3, 'EXCELLENT', 55.00, 'Ergonomic swivel office chair'),
('Dining Table', 2, 38, 4, 'GOOD', 200.00, 'Rectangular dining table for six'),
('Bathroom Cabinet', 3, 39, 5, 'EXCELLENT', 80.00, 'Wall-mounted bathroom storage'),
('Yellow Sofa', 4, 40, 6, 'GOOD', 220.00, 'Bright yellow two-seater sofa'),
('Kids Desk', 5, 41, 7, 'EXCELLENT', 45.00, 'Small desk perfect for children'),
('Vanity Dresser', 6, 42, 8, 'GOOD', 140.00, 'Bedroom vanity with three mirrors'),
('Platform Bed', 7, 43, 9, 'EXCELLENT', 180.00, 'Modern platform bed frame'),
('Console Sideboard', 8, 44, 10, 'GOOD', 120.00, 'Narrow console table'),
('Sliding Wardrobe', 9, 45, 11, 'EXCELLENT', 350.00, 'Modern sliding door wardrobe'),
('Rotating Bookcase', 10, 46, 12, 'GOOD', 130.00, 'Unique rotating book display'),
('Grandfather Clock', 11, 47, 13, 'AGE_WORN', 400.00, 'Tall standing grandfather clock'),
('Porcelain Plates', 12, 48, 14, 'EXCELLENT', 40.00, 'Set of vintage porcelain plates'),
('Folding Chair', 1, 49, 15, 'GOOD', 20.00, 'Portable wooden folding chair'),
('Side Table', 2, 50, 16, 'EXCELLENT', 35.00, 'Small round side table'),
('Medicine Cabinet', 3, 1, 17, 'GOOD', 60.00, 'Vintage bathroom medicine cabinet'),
('Sectional Sofa', 4, 2, 18, 'EXCELLENT', 450.00, 'Large L-shaped sectional sofa'),
('Standing Desk', 5, 3, 19, 'EXCELLENT', 150.00, 'Adjustable height standing desk'),
('Chest Dresser', 6, 4, 20, 'GOOD', 110.00, 'Four-drawer chest of drawers'),
('Canopy Bed', 7, 5, 3, 'AGE_WORN', 320.00, 'Four-poster canopy bed frame'),
('Serving Sideboard', 8, 6, 4, 'GOOD', 170.00, 'Formal dining room sideboard'),
('Corner Wardrobe', 9, 7, 5, 'EXCELLENT', 240.00, 'Space-efficient corner wardrobe'),
('Ladder Bookcase', 10, 8, 6, 'GOOD', 85.00, 'Leaning ladder-style bookshelf'),
('Digital Clock', 11, 9, 7, 'EXCELLENT', 25.00, 'Modern LED digital wall clock'),
('Glass Bowl', 12, 10, 8, 'EXCELLENT', 18.00, 'Large decorative glass fruit bowl'),
('Bar Stool', 1, 11, 9, 'GOOD', 40.00, 'Adjustable height bar stool'),
('Picnic Table', 2, 12, 10, 'EXCELLENT', 160.00, 'Outdoor wooden picnic table'),
('Garage Cabinet', 3, 13, 11, 'GOOD', 90.00, 'Metal storage cabinet for garage'),
('Futon Sofa', 4, 14, 12, 'EXCELLENT', 180.00, 'Convertible futon sofa bed'),
('Computer Desk', 5, 15, 13, 'GOOD', 85.00, 'Modern computer workstation'),
('Jewelry Dresser', 6, 16, 14, 'EXCELLENT', 200.00, 'Specialty dresser for jewelry storage'),
('Trundle Bed', 7, 17, 15, 'GOOD', 190.00, 'Single bed with pull-out trundle'),
('Bar Sideboard', 8, 18, 16, 'EXCELLENT', 210.00, 'Home bar cabinet with wine storage'),
('Armoire Wardrobe', 9, 19, 17, 'GOOD', 280.00, 'Traditional armoire wardrobe'),
('Cube Bookcase', 10, 20, 18, 'EXCELLENT', 70.00, 'Modern cube storage bookcase'),
('Alarm Clock', 11, 21, 19, 'GOOD', 12.00, 'Vintage wind-up alarm clock'),
('Picture Frame', 12, 22, 20, 'EXCELLENT', 8.00, 'Ornate golden picture frame'),
('Gaming Chair', 1, 23, 3, 'EXCELLENT', 120.00, 'Ergonomic gaming chair with RGB'),
('Patio Table', 2, 24, 4, 'GOOD', 80.00, 'Weather-resistant patio table'),
('Linen Cabinet', 3, 25, 5, 'EXCELLENT', 100.00, 'Tall cabinet for bathroom linens'),
('Loveseat Sofa', 4, 26, 6, 'GOOD', 150.00, 'Compact two-person loveseat'),
('Drafting Desk', 5, 27, 7, 'EXCELLENT', 110.00, 'Tilting top drafting table'),
('Mirror Dresser', 6, 28, 8, 'GOOD', 130.00, 'Dresser with large attached mirror'),
('Murphy Bed', 7, 29, 9, 'EXCELLENT', 400.00, 'Wall-mounted fold-down bed'),
('Wine Sideboard', 8, 30, 10, 'GOOD', 220.00, 'Sideboard with built-in wine rack'),
('Walk-in Wardrobe', 9, 31, 11, 'EXCELLENT', 500.00, 'Complete walk-in closet system'),
('Reading Bookcase', 10, 32, 12, 'GOOD', 95.00, 'Bookcase with built-in reading light'),
('Atomic Clock', 11, 33, 13, 'EXCELLENT', 45.00, 'Radio-controlled atomic wall clock'),
('Tea Set', 12, 34, 14, 'EXCELLENT', 55.00, 'Complete porcelain tea service set'),
('Accent Chair', 1, 35, 15, 'GOOD', 75.00, 'Colorful accent chair for living room'),
('Console Table', 2, 36, 16, 'EXCELLENT', 90.00, 'Narrow hallway console table'),
('Pantry Cabinet', 3, 37, 17, 'GOOD', 140.00, 'Tall kitchen pantry storage cabinet'),
('Chaise Sofa', 4, 38, 18, 'EXCELLENT', 280.00, 'Sectional sofa with chaise lounge'),
('Reception Desk', 5, 39, 19, 'GOOD', 200.00, 'Large L-shaped reception desk'),
('Armoire Dresser', 6, 40, 20, 'EXCELLENT', 250.00, 'Combination dresser and armoire'),
('Daybed', 7, 41, 3, 'GOOD', 160.00, 'Twin daybed with storage drawers'),
('Credenza Sideboard', 8, 42, 4, 'EXCELLENT', 180.00, 'Mid-century modern credenza'),
('Closet Wardrobe', 9, 43, 5, 'GOOD', 190.00, 'Portable closet wardrobe system'),
('Magazine Bookcase', 10, 44, 6, 'EXCELLENT', 60.00, 'Low bookcase for magazines'),
('Station Clock', 11, 45, 7, 'AGE_WORN', 80.00, 'Vintage railway station clock'),
('Decorative Mirror', 12, 46, 8, 'EXCELLENT', 35.00, 'Large ornate decorative wall mirror'),
('Swivel Chair', 1, 47, 9, 'GOOD', 65.00, 'Comfortable swivel desk chair'),
('Folding Table', 2, 48, 10, 'EXCELLENT', 50.00, 'Portable folding table'),
('Display Cabinet', 3, 49, 11, 'GOOD', 160.00, 'Glass-front display cabinet'),
('Recliner Sofa', 4, 50, 12, 'EXCELLENT', 350.00, 'Electric reclining sofa'),
('Treadmill Desk', 5, 1, 13, 'GOOD', 300.00, 'Walking treadmill desk combination'),
('Lingerie Dresser', 6, 2, 14, 'EXCELLENT', 120.00, 'Specialized dresser with small drawers'),
('Loft Bed', 7, 3, 15, 'GOOD', 220.00, 'High loft bed with desk space below'),
('Hutch Sideboard', 8, 4, 16, 'EXCELLENT', 240.00, 'Sideboard with top hutch display'),
('Modular Wardrobe', 9, 5, 17, 'GOOD', 320.00, 'Expandable modular wardrobe system'),
('Wall Bookcase', 10, 6, 18, 'EXCELLENT', 140.00, 'Wall-mounted floating bookcase');