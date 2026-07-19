-- ========================================
-- V2: Create price_list table + Update routes
-- ========================================

-- 1. Create price_list table
CREATE TABLE IF NOT EXISTS price_list (
    id      BIGSERIAL PRIMARY KEY,
    item    VARCHAR(255) NOT NULL,
    price   INTEGER NOT NULL CHECK (price >= 0)
);

-- Add comment for clarity
COMMENT ON TABLE price_list IS 'Stores items and their integer prices';

-- 2. Clean and reset routes table
-- Remove all existing data
DELETE FROM routes;

-- Reset the sequence to start from 1 (original state)
ALTER SEQUENCE IF EXISTS routes_id_seq RESTART WITH 1;

-- 3. Insert the new ordered routes
INSERT INTO routes (route, heading, role)
VALUES
    ('/billing',   'Billing',    1),
    ('/priceList', 'Price List', 1),
    ('/',          'Logout',     1)
    ON CONFLICT DO NOTHING;

-- Reset sequence again after inserts (good practice)
SELECT setval('routes_id_seq', (SELECT MAX(id) FROM routes), true);

-- 4. Insert dummy fireworks data
INSERT INTO price_list (item, price) VALUES
 ('Anar (Pack of 10)', 150),
 ('Sparkler (Pack of 10)', 80),
 ('Chocolate Bomb (Single)', 25),
 ('Ground Spinner (Pack of 12)', 120),
 ('Rocket (Single)', 40),
 ('Rocket (Pack of 5)', 180),
 ('Flower Pot (Pack of 6)', 220),
 ('Roman Candle (Single)', 90),
 ('Fountain - Small', 150),
 ('Fountain - Medium', 350),
 ('Fountain - Large', 650),
 ('Aerial Shell (Single Shot)', 450),
 ('Bijli Bomb (Pack of 10)', 100),
 ('Laxmi Bomb (Single)', 35),
 ('1000 Wala (Full Box)', 1250),
 ('5000 Wala (Full Box)', 4500),
 ('Electric Sparklers (Pack)', 200),
 ('Sparklers - Golden (Pack)', 60),
 ('Sky Shot - 10 pcs', 380),
 ('Green Bamboo Rocket', 280)
ON CONFLICT DO NOTHING;