CREATE SEQUENCE IF NOT EXISTS inventories_id_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE inventories (
    id BIGINT PRIMARY KEY DEFAULT nextval('inventories_id_seq'),
    brand_or_company TEXT,
    state SMALLINT NOT NULL,
    item TEXT,
    num_of_boxes INTEGER NOT NULL,
    num_of_cartons INTEGER NOT NULL,
    price_per_carton INTEGER NOT NULL,
    sub_total INTEGER NOT NULL,
    CONSTRAINT chk_inventories_state CHECK (state IN (1, 2, 3)),
    CONSTRAINT chk_inventories_num_of_boxes CHECK (num_of_boxes > 0),
    CONSTRAINT chk_inventories_num_of_cartons CHECK (num_of_cartons > 0),
    CONSTRAINT chk_inventories_price_per_carton CHECK (price_per_carton > 0),
    CONSTRAINT chk_inventories_sub_total CHECK (sub_total >= 0)
);

ALTER SEQUENCE inventories_id_seq OWNED BY inventories.id;

CREATE INDEX idx_inventories_item ON inventories(item);
CREATE INDEX idx_inventories_state ON inventories(state);