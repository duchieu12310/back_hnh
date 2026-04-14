-- 1. Tạm thời tắt kiểm tra khóa ngoại để xóa dữ liệu
SET FOREIGN_KEY_CHECKS = 0;

-- 2. Xóa sạch dữ liệu cũ trong bảng liên kết và bảng role
TRUNCATE TABLE user_role;
TRUNCATE TABLE role;

-- 3. Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 1;

-- 4. Chèn 4 Role mới theo đúng các code đã cấu hình trong Java
INSERT INTO role (id, code, name, status, created_at, updated_at) VALUES 
(1, 'ADMIN', 'Quản trị viên hệ thống', 1, NOW(), NOW()),
(2, 'MANAGER', 'Quản lý (Sản phẩm & Kho)', 1, NOW(), NOW()),
(3, 'OPERATOR', 'Nhân viên vận hành (Đơn hàng)', 1, NOW(), NOW()),
(4, 'CUSTOMER', 'Khách hàng', 1, NOW(), NOW());

-- 5. Gán quyền ADMIN cho tài khoản admin (Thường là user đầu tiên)
-- Lưu ý: Bạn nên kiểm tra lại username chính xác của mình
INSERT INTO user_role (user_id, role_id) 
SELECT id, 1 FROM user WHERE username = 'admin' LIMIT 1;

-- 6. Gán quyền CUSTOMER cho tất cả các user khác
INSERT INTO user_role (user_id, role_id)
SELECT id, 4 FROM user WHERE username != 'admin';
