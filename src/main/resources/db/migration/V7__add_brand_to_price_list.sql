-- =============================================
-- V7: Add brand column to price list table and remove all existing data
-- =============================================

-- Truncate and reset the sequence
TRUNCATE TABLE price_list RESTART IDENTITY;

-- 2. Drop the old sequence that BIGSERIAL created
DROP SEQUENCE IF EXISTS price_list_id_seq CASCADE;

-- Change column type
ALTER TABLE price_list
    ALTER COLUMN id DROP DEFAULT,
    ALTER COLUMN id TYPE BIGINT;

-- Make sure the sequence exists and is linked
CREATE SEQUENCE IF NOT EXISTS price_list_id_seq INCREMENT BY 50 START WITH 1 OWNED BY price_list.id;
ALTER TABLE price_list ALTER COLUMN id SET DEFAULT nextval('price_list_id_seq');

-- Add new column
ALTER TABLE price_list ADD COLUMN brand VARCHAR(255);

INSERT INTO price_list (item, price, brand) VALUES
    ('CCB', 350, 'Standard - Fancy'),
    ('Golden Whistle Small', 260, 'Standard - Fancy'),
    ('Golden Whistle Big', 540, 'Standard - Fancy'),
    ('Gold Rush', 510, 'Standard - Fancy'),
    ('Aerial Outs', 516, 'Standard - Fancy'),
    ('7 Shots', 307, 'Standard - Fancy'),
    ('Golden Drops', 620, 'Standard - Fancy'),
    ('Magic Whip', 155, 'Standard - Fancy'),
    ('Stones', 155, 'Standard - Fancy'),
    ('Olympic Torch', 550, 'Standard - Fancy'),
    ('Jet Mix - 12 Shots (8 variants)', 440, 'Standard'),
    ('Fly All - 12 Shots (5 variants)', 270, 'Standard'),
    ('Touch Sky - 25 Shots (3 variants)', 1000, 'Standard'),
    ('Grand Finale - 40 Shots (5 variants)', 1360, 'Standard'),
    ('Fly Boss - 60 Shots (4 variants)', 1800, 'Standard'),
    ('80s HITS - 80 Shots (3 variants)', 2400, 'Standard'),
    ('Sensation - 100 Shots', 4400, 'Standard'),
    ('Sun Flower - 100 Shots', 4400, 'Standard'),
    ('Moon Light - 100 Shots (4 variants)', 2600, 'Standard'),
    ('Crushers - 125 Shots', 5850, 'Standard'),
    ('Rainbow Dance - 240 Shots', 6000, 'Standard'),
    ('1000 Wala', 930, 'Standard'),
    ('2000 Wala', 1750, 'Standard'),
    ('5000 Wala', 4100, 'Standard'),
    ('10000 Wala', 8300, 'Standard'),
    ('1" Comet - Mix Masala (10 variants)', 100, 'Standard'),
    ('1.5" Comet - Friendly Fire (12 variants)', 190, 'Standard'),
    ('1.75" Comet - Team Captain (12 variants)', 230, 'Standard'),
    ('1.75" Comet - I Comet', 690, 'Standard'),
    ('1.75" Comet - Space Race (12 variants)', 690, 'Standard'),
    ('1.75" Comet - Thunder Bolt & Jasmine Drops', 750, 'Standard'),
    ('2" Asmaan Guru', 290, 'Standard'),
    ('2.5" Peanut (Double Shot)', 540, 'Standard'),
    ('3" Moon Modi & Kya Baath Hai (13 variants)', 535, 'Standard'),
    ('3" Twitter Glitter (7 variants)', 765, 'Standard'),
    ('1 1/2" Comet', 150, 'Supreme'),
    ('30 Shots', 900, 'Supreme'),
    ('100 Shots', 4000, 'Supreme'),
    ('Peacock', 500, 'Supreme'),
    ('102 Shots', 3600, 'ARD'),
    ('20 Shots (2" Comet)', 1600, 'ARD'),
    ('3000 Lar', 3000, 'ARD'),
    ('1000 Lar', 900, 'ARD'),
    ('10000 Lar', 13000, 'Ayyan'),
    ('1000 Lar', 1500, 'Ayyan'),
    ('600 Lar', 1200, 'Ayyan'),
    ('Megapeacock', 750, 'Ayyan'),
    ('2" Comet', 300, 'Ayyan'),
    ('3" Comet', 600, 'Ayyan'),
    ('3" Premium Comet', 700, 'Ayyan'),
    ('12 Shots', 400, 'Ayyan'),
    ('25 Shots', 900, 'Ayyan'),
    ('30 Shots', 1300, 'Ayyan'),
    ('60 Shots', 2500, 'Ayyan'),
    ('120 Shots', 4500, 'Ayyan');