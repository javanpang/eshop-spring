CREATE TABLE IF NOT EXISTS products
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2) NOT NULL,
    category    VARCHAR(255)   NOT NULL,
    quantity    INT            NOT NULL,
    created_at  TIMESTAMP      NOT NULL,
    updated_at  TIMESTAMP      NOT NULL
);

INSERT INTO products (id, name, description, price, category, quantity, created_at, updated_at)
SELECT '550e8400-e29b-41d4-a716-446655440001',
       'Mechanical Keyboard',
       'RGB mechanical keyboard with blue switches',
       129.99,
       'Electronics',
       50,
       '2021-01-01 12:00:00',
       '2021-01-01 12:00:00'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE id = '550e8400-e29b-41d4-a716-446655440001');

INSERT INTO products (id, name, description, price, category, quantity, created_at, updated_at)
SELECT '550e8400-e29b-41d4-a716-446655440002',
       'Wireless Mouse',
       'Ergonomic wireless mouse with adjustable DPI',
       49.99,
       'Electronics',
       100,
       '2021-02-01 10:00:00',
       '2021-02-01 10:00:00'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE id = '550e8400-e29b-41d4-a716-446655440002');

INSERT INTO products (id, name, description, price, category, quantity, created_at, updated_at)
SELECT '550e8400-e29b-41d4-a716-446655440003',
       'Laptop Stand',
       'Aluminium adjustable laptop stand for desk',
       39.99,
       'Accessories',
       70,
       '2021-03-01 11:00:00',
       '2021-03-01 11:00:00'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE id = '550e8400-e29b-41d4-a716-446655440003');

INSERT INTO products (id, name, description, price, category, quantity, created_at, updated_at)
SELECT '550e8400-e29b-41d4-a716-446655440004',
       'USB-C Hub',
       'Multiport USB-C hub with HDMI and USB 3.0 ports',
       59.99,
       'Electronics',
       80,
       '2021-04-01 09:30:00',
       '2021-04-01 09:30:00'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE id = '550e8400-e29b-41d4-a716-446655440004');

INSERT INTO products (id, name, description, price, category, quantity, created_at, updated_at)
SELECT '550e8400-e29b-41d4-a716-446655440005',
       'Gaming Chair',
       'Comfortable ergonomic gaming chair with lumbar support',
       249.99,
       'Furniture',
       25,
       '2021-05-01 14:00:00',
       '2021-05-01 14:00:00'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE id = '550e8400-e29b-41d4-a716-446655440005');

INSERT INTO products (id, name, description, price, category, quantity, created_at, updated_at)
SELECT '550e8400-e29b-41d4-a716-446655440006',
       'Bluetooth Speaker',
       'Portable Bluetooth speaker with deep bass',
       79.99,
       'Electronics',
       60,
       '2021-06-01 12:30:00',
       '2021-06-01 12:30:00'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE id = '550e8400-e29b-41d4-a716-446655440006');

INSERT INTO products (id, name, description, price, category, quantity, created_at, updated_at)
SELECT '550e8400-e29b-41d4-a716-446655440007',
       'Smartwatch',
       'Waterproof smartwatch with heart rate monitor',
       199.99,
       'Wearables',
       40,
       '2021-07-01 08:00:00',
       '2021-07-01 08:00:00'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE id = '550e8400-e29b-41d4-a716-446655440007');

INSERT INTO products (id, name, description, price, category, quantity, created_at, updated_at)
SELECT '550e8400-e29b-41d4-a716-446655440008',
       'Noise Cancelling Headphones',
       'Over-ear headphones with active noise cancellation',
       299.99,
       'Electronics',
       30,
       '2021-08-01 16:00:00',
       '2021-08-01 16:00:00'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE id = '550e8400-e29b-41d4-a716-446655440008');

INSERT INTO products (id, name, description, price, category, quantity, created_at, updated_at)
SELECT '550e8400-e29b-41d4-a716-446655440009',
       'External SSD',
       '1TB portable external SSD with USB-C connectivity',
       149.99,
       'Electronics',
       55,
       '2021-09-01 13:00:00',
       '2021-09-01 13:00:00'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE id = '550e8400-e29b-41d4-a716-446655440009');

INSERT INTO products (id, name, description, price, category, quantity, created_at, updated_at)
SELECT '550e8400-e29b-41d4-a716-446655440010',
       'Desk Lamp',
       'LED desk lamp with adjustable brightness',
       29.99,
       'Furniture',
       90,
       '2021-10-01 07:30:00',
       '2021-10-01 07:30:00'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE id = '550e8400-e29b-41d4-a716-446655440010');

INSERT INTO products (id, name, description, price, category, quantity, created_at, updated_at)
SELECT '550e8400-e29b-41d4-a716-446655440011',
       'Webcam',
       '1080p HD webcam with built-in microphone',
       59.99,
       'Electronics',
       65,
       '2021-11-01 11:30:00',
       '2021-11-01 11:30:00'
WHERE NOT EXISTS (SELECT 1 FROM products WHERE id = '550e8400-e29b-41d4-a716-446655440011');