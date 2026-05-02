-- 1. Thêm cột user_id và is_default vào bảng address
ALTER TABLE address ADD COLUMN user_id BIGINT;
ALTER TABLE address ADD COLUMN is_default BOOLEAN DEFAULT FALSE;

-- 2. Di chuyển liên kết từ bảng user sang bảng address
-- Thiết lập user_id cho các địa chỉ hiện tại dựa trên liên kết cũ ở bảng user
UPDATE address a
JOIN user u ON u.address_id = a.id
SET a.user_id = u.id, a.is_default = TRUE;

-- 3. Tạo khóa ngoại cho user_id trong bảng address
ALTER TABLE address ADD CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES user(id);

-- 4. Xóa cột address_id trong bảng user (vì bây giờ quan hệ là 1-N từ user sang address)
-- Trước khi xóa cần xóa constraint cũ
-- Bạn có thể kiểm tra tên constraint bằng: SHOW CREATE TABLE user;
-- Thường Hibernate đặt tên dạng FK...
-- ALTER TABLE user DROP FOREIGN KEY <tên_khóa_ngoại>;
-- ALTER TABLE user DROP COLUMN address_id;

-- Tạm thời tôi comment lại lệnh xóa cột để bạn kiểm tra dữ liệu trước
-- ALTER TABLE user DROP COLUMN address_id;
