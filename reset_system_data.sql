SET FOREIGN_KEY_CHECKS = 0;

-- Clean user-related data

SET FOREIGN_KEY_CHECKS = 1;

-- Seed Roles
INSERT INTO role (id, code, name, status, created_at, updated_at) VALUES 
(1, 'ADMIN', 'Quản trị viên hệ thống', 1, NOW(), NOW()),
(2, 'MANAGER', 'Quản lý (Sản phẩm & Kho)', 1, NOW(), NOW()),
(3, 'OPERATOR', 'Nhân viên vận hành (Đơn hàng)', 1, NOW(), NOW()),
(4, 'CUSTOMER', 'Khách hàng', 1, NOW(), NOW());

-- Seed Default Admin Address (IDs 1,1,1 for administrative units are assumed to exist)
INSERT INTO address (id, line, province_id, district_id, ward_id, created_at, updated_at) 
VALUES (1, 'System Admin HQ', 1, 1, 1, NOW(), NOW());

-- Seed Default Admin User
-- Credentials: admin / admin
INSERT INTO user (id, username, password, fullname, email, phone, gender, status, address_id, created_at, updated_at) VALUES 
(1, 'admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2', 'System Admin', 'admin@hnh.com', '0123456789', 'M', 1, 1, NOW(), NOW());

-- Assign Admin Role
INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
