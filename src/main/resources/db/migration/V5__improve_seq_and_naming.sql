-- =============================================
-- V5: Use smaller integer types for roles & routes
-- =============================================

-- 1. Drop foreign key constraints first
ALTER TABLE users DROP CONSTRAINT IF EXISTS fk_users_role;
ALTER TABLE routes DROP CONSTRAINT IF EXISTS fk_routes_role;

-- 2. Change roles.id to SMALLINT
ALTER TABLE roles
ALTER COLUMN id TYPE SMALLINT;

-- 3. Change users.role_id to SMALLINT
ALTER TABLE users
ALTER COLUMN role_id TYPE SMALLINT;

-- 4. Change routes.id and routes.role_id to SMALLINT
ALTER TABLE routes RENAME COLUMN role TO role_id;
ALTER TABLE routes
ALTER COLUMN id TYPE SMALLINT,
    ALTER COLUMN role_id TYPE SMALLINT;

-- 5. Recreate the foreign keys
ALTER TABLE users
    ADD CONSTRAINT fk_users_role
        FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT;

ALTER TABLE routes
    ADD CONSTRAINT fk_routes_role
        FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT;

-- 6. (Optional but recommended) Update sequences to match
-- Sequences can stay as-is, but we can restart them cleanly
SELECT setval('roles_id_seq', (SELECT COALESCE(MAX(id), 1) FROM roles));
SELECT setval('routes_id_seq', (SELECT COALESCE(MAX(id), 1) FROM routes));

-- 7. Improve sequence performance (safe)
ALTER SEQUENCE roles_id_seq CACHE 50;
ALTER SEQUENCE users_id_seq CACHE 50;
ALTER SEQUENCE routes_id_seq CACHE 50;