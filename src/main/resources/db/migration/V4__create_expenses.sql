-- ========================================
-- V4: Create expenses table
-- ========================================

-- 1. Create price_list table
CREATE TABLE IF NOT EXISTS expenses (
    id                  BIGSERIAL PRIMARY KEY,
    reason_for_expense  VARCHAR(255) NOT NULL,
    amount              INTEGER NOT NULL CHECK (amount >= 10)
);