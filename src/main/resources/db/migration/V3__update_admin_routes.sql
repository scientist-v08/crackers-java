-- ========================================
-- V3: Create price_list table + Update routes
-- ========================================

-- 1. Clean and reset routes table
-- Remove all existing data
DELETE FROM routes;

-- Reset the sequence to start from 1 (original state)
ALTER SEQUENCE IF EXISTS routes_id_seq RESTART WITH 1;

-- 3. Insert the new ordered routes
INSERT INTO routes (route, heading, role)
VALUES
    ('/billing',   'Billing',    1),
    ('/priceList', 'Price List', 1),
    ('/',          'Logout',     1),
    ('/billing',   'Billing',    2),
    ('/priceList', 'Price List', 2),
    ('/expenses', 'Expenses', 2),
    ('/',          'Logout',     2)
    ON CONFLICT DO NOTHING;

-- Reset sequence again after inserts (good practice)
SELECT setval('routes_id_seq', (SELECT MAX(id) FROM routes), true);