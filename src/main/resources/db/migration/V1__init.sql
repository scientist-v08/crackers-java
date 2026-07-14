-- =============================================
-- Flyway V1__init.sql
-- Initializes core tables: roles, users, routes
-- =============================================

-- =============================================
-- 1. ROLES TABLE
-- =============================================
CREATE SEQUENCE IF NOT EXISTS roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS roles (
    id          BIGINT PRIMARY KEY DEFAULT nextval('roles_id_seq'),
    name        VARCHAR(50) NOT NULL UNIQUE
);

-- Insert initial roles
INSERT INTO roles (id, name)
VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN')
    ON CONFLICT (id) DO NOTHING;

-- Reset sequence to highest value
SELECT setval('roles_id_seq', (SELECT MAX(id) FROM roles), true);

-- =============================================
-- 2. USERS TABLE
-- =============================================
CREATE SEQUENCE IF NOT EXISTS users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS users (
    id            BIGINT PRIMARY KEY DEFAULT nextval('users_id_seq'),
    email         VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    full_name     VARCHAR(255),
    role_id       BIGINT NOT NULL,
    enabled       BOOLEAN NOT NULL DEFAULT true,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_users_role FOREIGN KEY (role_id)
    REFERENCES roles(id) ON DELETE RESTRICT
);

-- Create index on email
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- =============================================
-- 3. ROUTES TABLE
-- =============================================
CREATE SEQUENCE IF NOT EXISTS routes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS routes (
    id       BIGINT PRIMARY KEY DEFAULT nextval('routes_id_seq'),
    route    VARCHAR(255) NOT NULL,
    heading  VARCHAR(255) NOT NULL,
    role     BIGINT NOT NULL,

    CONSTRAINT fk_routes_role FOREIGN KEY (role)
    REFERENCES roles(id) ON DELETE RESTRICT
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_routes_role ON routes(role);
CREATE INDEX IF NOT EXISTS idx_routes_route ON routes(route);

-- Insert initial routes
INSERT INTO routes (route, heading, role)
VALUES
    ('/billing', 'Billing', 1),
    ('/', 'Logout', 1)
    ON CONFLICT DO NOTHING;

-- Reset sequence
SELECT setval('routes_id_seq', (SELECT MAX(id) FROM routes), true);

-- =============================================
-- Additional useful indexes
-- =============================================
CREATE INDEX IF NOT EXISTS idx_users_role_id ON users(role_id);