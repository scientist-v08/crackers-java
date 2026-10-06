-- =============================================
-- V6: Fix sequence increment size to match allocationSize = 50
-- =============================================

ALTER SEQUENCE users_id_seq INCREMENT BY 50;
ALTER SEQUENCE roles_id_seq INCREMENT BY 50;
ALTER SEQUENCE routes_id_seq INCREMENT BY 50;