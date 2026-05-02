-- 1. Thêm Role SHIPPER vào bảng role
INSERT INTO role (code, name, status, created_at, updated_at) 
VALUES ('SHIPPER', 'Người giao hàng', 1, NOW(), NOW());

-- 2. Thêm cột tọa độ vào bảng address
ALTER TABLE address ADD COLUMN latitude DOUBLE;
ALTER TABLE address ADD COLUMN longitude DOUBLE;

-- 3. Thêm cột shipper_id vào bảng waybill
ALTER TABLE waybill ADD COLUMN shipper_id BIGINT;
ALTER TABLE waybill ADD CONSTRAINT fk_waybill_shipper FOREIGN KEY (shipper_id) REFERENCES user(id);
