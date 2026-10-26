-- Truncate and reset the sequence
TRUNCATE TABLE routes RESTART IDENTITY;

INSERT INTO routes (route, heading, role_id)
VALUES
    ('/billing',    'Billing',    2),
    ('/price-list', 'Price List', 2),
    ('/expenses',   'Expenses',   2),
    ('/inventory',  'Inventory',  2),
    ('/',           'Logout',     2),
    ('/billing',    'Billing',    1),
    ('/price-list', 'Price List', 1),
    ('/',           'Logout',     1);


CREATE SEQUENCE IF NOT EXISTS billings_id_seq
    START WITH 1
    INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS purchases_id_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE billings (
  id            BIGINT PRIMARY KEY DEFAULT nextval('billings_id_seq'),
  "user"        TEXT,
  mobile        TEXT,
  grand_total   NUMERIC(12,2),
  finalized_amt INTEGER
);

CREATE TABLE purchases (
   id         BIGINT PRIMARY KEY DEFAULT nextval('purchases_id_seq'),
   billing_id BIGINT NOT NULL,
   mobile     TEXT,
   mrp_or_net INTEGER,
   item       TEXT,
   quantity   INTEGER,
   discount   TEXT,
   sub_total  NUMERIC(12,2),

   CONSTRAINT fk_purchases_billing FOREIGN KEY (billing_id) REFERENCES billings(id) ON DELETE RESTRICT
);

ALTER SEQUENCE billings_id_seq
    OWNED BY billings.id;

ALTER SEQUENCE purchases_id_seq
    OWNED BY purchases.id;

CREATE INDEX idx_purchases_billing_id
    ON purchases(billing_id);
