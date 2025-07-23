## Data Initialization (Optional)

### data.sql (in src/main/resources)
```sql
-- Insert test roles
-- Insert default roles
INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER') ON DUPLICATE KEY UPDATE name='ROLE_USER';
INSERT INTO roles (id, name) VALUES (2, 'ROLE_ADMIN') ON DUPLICATE KEY UPDATE name='ROLE_ADMIN';

-- Insert sample products
INSERT INTO products (name, description, price, created_at)
VALUES ('Laptop', 'High-performance laptop', 999.99, CURRENT_TIMESTAMP);

INSERT INTO products (name, description, price, created_at)
VALUES ('Mouse', 'Wireless mouse', 29.99, CURRENT_TIMESTAMP);

INSERT INTO products (name, description, price, created_at)
VALUES ('Keyboard', 'Mechanical keyboard', 79.99, CURRENT_TIMESTAMP);