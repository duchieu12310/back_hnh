-- ============================================================================
-- SQL SCRIPT: KHỞI TẠO DỮ LIỆU GIẢ LẬP ĐỂ KIỂM TRA BÁO CÁO (SEED TEST DATA)
-- Cập nhật đầy đủ thông tin sách (variant_id) từ 1 đến 29
-- ============================================================================

-- Tắt kiểm tra khóa ngoại tạm thời để tránh lỗi ràng buộc khi làm sạch dữ liệu cũ
SET FOREIGN_KEY_CHECKS = 0;

-- Làm sạch dữ liệu cũ trong các bảng liên quan
TRUNCATE TABLE `review`;
TRUNCATE TABLE `waybill`;
TRUNCATE TABLE `order_variant`;
TRUNCATE TABLE `order`;
TRUNCATE TABLE `inventory_item`;
TRUNCATE TABLE `storage_location`;
TRUNCATE TABLE `warehouse`;


-- ============================================================================
-- 0. ĐẢM BẢO SẢN PHẨM VÀ PHIÊN BẢN SẢN PHẨM (1-29) TỒN TẠI TRONG CƠ SỞ DỮ LIỆU
-- Nếu đã có sẵn từ sach.sql, câu lệnh này sẽ bỏ qua để giữ nguyên dữ liệu gốc của bạn.
-- Nếu chưa có, câu lệnh sẽ tự động khởi tạo dữ liệu mẫu để tránh lỗi khóa ngoại (Error 1452).
-- ============================================================================
DROP PROCEDURE IF EXISTS EnsureProductsAndVariantsExist;
DELIMITER //
CREATE PROCEDURE EnsureProductsAndVariantsExist()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 29 DO
        -- Chèn product nếu chưa tồn tại
        INSERT IGNORE INTO `product` (`id`, `created_at`, `updated_at`, `code`, `name`, `slug`, `status`, `weight`)
        VALUES (i, NOW(), NOW(), CONCAT('PROD', LPAD(i, 3, '0')), CONCAT('Sách Thử Nghiệm ', i), CONCAT('sach-thu-nghiem-', i), 1, 0.3);
        
        -- Chèn variant nếu chưa tồn tại
        INSERT IGNORE INTO `variant` (`id`, `created_at`, `updated_at`, `price`, `quantity`, `status`, `sku`, `product_id`)
        VALUES (i, NOW(), NOW(), 100000 + i * 5000, 100, 1, CONCAT('SKU-', i), i);
        
        SET i = i + 1;
    END WHILE;
END //
DELIMITER ;
CALL EnsureProductsAndVariantsExist();
DROP PROCEDURE IF EXISTS EnsureProductsAndVariantsExist;


-- ============================================================================
-- 1. THÊM DỮ LIỆU NHÀ KHO (WAREHOUSE)
-- ============================================================================
INSERT INTO `warehouse` (`id`, `created_at`, `updated_at`, `code`, `name`, `status`) VALUES
(1, NOW(), NOW(), 'WH-HN-01', 'Kho Hà Nội Quận Cầu Giấy', 1),
(2, NOW(), NOW(), 'WH-HCM-02', 'Kho TP.HCM Quận 1', 1);


-- ============================================================================
-- 2. THÊM VỊ TRÍ LƯU TRỮ (STORAGE LOCATION) TRONG KHO
-- ============================================================================
INSERT INTO `storage_location` (`id`, `created_at`, `updated_at`, `aisle`, `bin`, `shelf`, `warehouse_id`) VALUES
(1, NOW(), NOW(), 'Khu A - Sách Văn Học', 'Thùng B1', 'Kệ số 1', 1),
(2, NOW(), NOW(), 'Khu B - Sách Kỹ Năng', 'Thùng B2', 'Kệ số 2', 1),
(3, NOW(), NOW(), 'Khu C - Sách Khoa Học', 'Thùng B3', 'Kệ số 3', 2);


-- ============================================================================
-- 3. THÊM TỒN KHO CHO ĐẦY ĐỦ CÁC BẢN PHIÊN BẢN SÁCH TỪ 1 ĐẾN 29 (INVENTORY ITEM)
-- ============================================================================
INSERT INTO `inventory_item` (`created_at`, `updated_at`, `quantity`, `storage_location_id`, `variant_id`) VALUES
-- Lưu ở vị trí Kho 1 - Khu A (Variant 1 -> 10)
(NOW(), NOW(), 100, 1, 1),
(NOW(), NOW(), 120, 1, 2),
(NOW(), NOW(), 150, 1, 3),
(NOW(), NOW(), 90, 1, 4),
(NOW(), NOW(), 110, 1, 5),
(NOW(), NOW(), 80, 1, 6),
(NOW(), NOW(), 200, 1, 7),
(NOW(), NOW(), 130, 1, 8),
(NOW(), NOW(), 140, 1, 9),
(NOW(), NOW(), 170, 1, 10),
-- Lưu ở vị trí Kho 1 - Khu B (Variant 11 -> 20)
(NOW(), NOW(), 95, 2, 11),
(NOW(), NOW(), 105, 2, 12),
(NOW(), NOW(), 115, 2, 13),
(NOW(), NOW(), 125, 2, 14),
(NOW(), NOW(), 85, 2, 15),
(NOW(), NOW(), 75, 2, 16),
(NOW(), NOW(), 180, 2, 17),
(NOW(), NOW(), 220, 2, 18),
(NOW(), NOW(), 160, 2, 19),
(NOW(), NOW(), 145, 2, 20),
-- Lưu ở vị trí Kho 2 - Khu C (Variant 21 -> 29)
(NOW(), NOW(), 135, 3, 21),
(NOW(), NOW(), 115, 3, 22),
(NOW(), NOW(), 125, 3, 23),
(NOW(), NOW(), 90, 3, 24),
(NOW(), NOW(), 100, 3, 25),
(NOW(), NOW(), 110, 3, 26),
(NOW(), NOW(), 120, 3, 27),
(NOW(), NOW(), 130, 3, 28),
(NOW(), NOW(), 140, 3, 29);


-- ============================================================================
-- 4. THÊM 5 ĐƠN HÀNG ĐẶT HÔM NAY (TODAY'S ORDERS - 5 ORDERS)
-- ============================================================================
INSERT INTO `order` 
(`id`, `created_at`, `updated_at`, `code`, `note`, `payment_method_type`, `payment_status`, `shipping_cost`, `status`, `tax`, `to_address`, `to_district_name`, `to_name`, `to_phone`, `to_province_name`, `to_ward_name`, `total_amount`, `total_pay`, `order_resource_id`, `user_id`)
VALUES
(1, NOW(), NOW(), 'ORD-TODAY-001', 'Giao giờ hành chính', 'CASH', 1, 30000.00000, 1, 0.10000, '123 Cầu Giấy', 'Cầu Giấy', 'Nguyễn Văn A', '0912345678', 'Hà Nội', 'Dịch Vọng', 215000.00000, 266500.00000, 1, 6),
(2, NOW(), NOW(), 'ORD-TODAY-002', 'Gọi trước khi giao', 'VNPAY', 2, 30000.00000, 2, 0.10000, '456 Nguyễn Trãi', 'Thanh Xuân', 'Trần Thị B', '0912345678', 'Hà Nội', 'Thượng Đình', 235000.00000, 288500.00000, 1, 6),
(3, NOW(), NOW(), 'ORD-TODAY-003', '', 'PAYPAL', 2, 30000.00000, 2, 0.10000, '789 Lê Lợi', 'Quận 1', 'Lê Văn C', '0912345678', 'TP.HCM', 'Bến Nghé', 255000.00000, 310500.00000, 1, 6),
(4, NOW(), NOW(), 'ORD-TODAY-004', 'Giao gấp', 'CASH', 1, 30000.00000, 1, 0.10000, '101 Hoàng Hoa Thám', 'Ba Đình', 'Phạm Văn D', '0912345678', 'Hà Nội', 'Liễu Giai', 275000.00000, 332500.00000, 1, 6),
(5, NOW(), NOW(), 'ORD-TODAY-005', '', 'CASH', 1, 30000.00000, 1, 0.10000, '202 Lý Tự Trọng', 'Quận 1', 'Đỗ Thị E', '0912345678', 'TP.HCM', 'Bến Thành', 295000.00000, 354500.00000, 1, 6);

-- Thêm chi tiết cho 5 đơn hàng hôm nay (sử dụng sách từ 1 -> 10)
INSERT INTO `order_variant` (`order_id`, `variant_id`, `quantity`, `price`, `amount`, `storage_location_id`) VALUES
-- Đơn 1 mua sách 1 và 2
(1, 1, 1, 105000.00000, 105000.00000, 1),
(1, 2, 1, 110000.00000, 110000.00000, 1),
-- Đơn 2 mua sách 3 and 4
(2, 3, 1, 115000.00000, 115000.00000, 2),
(2, 4, 1, 120000.00000, 120000.00000, 2),
-- Đơn 3 mua sách 5 and 6
(3, 5, 1, 125000.00000, 125000.00000, 3),
(3, 6, 1, 130000.00000, 130000.00000, 1),
-- Đơn 4 mua sách 7 and 8
(4, 7, 1, 135000.00000, 135000.00000, 1),
(4, 8, 1, 140000.00000, 140000.00000, 1),
-- Đơn 5 mua sách 9 and 10
(5, 9, 1, 145000.00000, 145000.00000, 1),
(5, 10, 1, 150000.00000, 150000.00000, 1);


-- ============================================================================
-- 5. THÊM 10 ĐƠN HÀNG ĐẶT TUẦN TRƯỚC (LAST WEEK ORDERS - 10 ORDERS) VÀ ĐÃ KẾT THÚC VẬN ĐƠN
-- Sử dụng đầy đủ các cuốn sách từ variant_id 11 đến 29
-- ============================================================================
INSERT INTO `order` 
(`id`, `created_at`, `updated_at`, `code`, `note`, `payment_method_type`, `payment_status`, `shipping_cost`, `status`, `tax`, `to_address`, `to_district_name`, `to_name`, `to_phone`, `to_province_name`, `to_ward_name`, `total_amount`, `total_pay`, `order_resource_id`, `user_id`)
VALUES
(11, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 'ORD-LWK-011', '', 'CASH', 2, 30000.00000, 4, 0.10000, 'Địa chỉ A', 'Quận A', 'Khách hàng 11', '0912345678', 'Hà Nội', 'Phường A', 320000.00000, 382000.00000, 1, 6),
(12, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 'ORD-LWK-012', '', 'CASH', 2, 30000.00000, 4, 0.10000, 'Địa chỉ B', 'Quận B', 'Khách hàng 12', '0912345678', 'Hà Nội', 'Phường B', 340000.00000, 404000.00000, 1, 6),
(13, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 'ORD-LWK-013', '', 'PAYPAL', 2, 30000.00000, 4, 0.10000, 'Địa chỉ C', 'Quận C', 'Khách hàng 13', '0912345678', 'TP.HCM', 'Phường C', 360000.00000, 426000.00000, 1, 6),
(14, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 'ORD-LWK-014', '', 'VNPAY', 2, 30000.00000, 4, 0.10000, 'Địa chỉ D', 'Quận D', 'Khách hàng 14', '0912345678', 'Hà Nội', 'Phường D', 380000.00000, 448000.00000, 1, 6),
(15, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 'ORD-LWK-015', '', 'CASH', 2, 30000.00000, 4, 0.10000, 'Địa chỉ E', 'Quận E', 'Khách hàng 15', '0912345678', 'TP.HCM', 'Phường E', 400000.00000, 470000.00000, 1, 6),
(16, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 'ORD-LWK-016', '', 'CASH', 2, 30000.00000, 4, 0.10000, 'Địa chỉ F', 'Quận F', 'Khách hàng 16', '0912345678', 'Hà Nội', 'Phường F', 420000.00000, 492000.00000, 1, 6),
(17, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 'ORD-LWK-017', '', 'CASH', 2, 30000.00000, 4, 0.10000, 'Địa chỉ G', 'Quận G', 'Khách hàng 17', '0912345678', 'Hà Nội', 'Phường G', 440000.00000, 514000.00000, 1, 6),
(18, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 'ORD-LWK-018', '', 'PAYPAL', 2, 30000.00000, 4, 0.10000, 'Địa chỉ H', 'Quận H', 'Khách hàng 18', '0912345678', 'TP.HCM', 'Phường H', 460000.00000, 536000.00000, 1, 6),
(19, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 'ORD-LWK-019', '', 'VNPAY', 2, 30000.00000, 4, 0.10000, 'Địa chỉ I', 'Quận I', 'Khách hàng 19', '0912345678', 'Hà Nội', 'Phường I', 480000.00000, 558000.00000, 1, 6),
(20, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 'ORD-LWK-020', '', 'CASH', 2, 30000.00000, 4, 0.10000, 'Địa chỉ K', 'Quận K', 'Khách hàng 20', '0912345678', 'TP.HCM', 'Phường K', 500000.00000, 580000.00000, 1, 6);

-- Thêm chi tiết cho 10 đơn hàng tuần trước (phân bổ sách từ variant_id 11 -> 29)
INSERT INTO `order_variant` (`order_id`, `variant_id`, `quantity`, `price`, `amount`, `storage_location_id`) VALUES
-- Đơn 11 mua sách 11 và 12
(11, 11, 1, 155000.00000, 155000.00000, 2),
(11, 12, 1, 160000.00000, 160000.00000, 2),
-- Đơn 12 mua sách 13 và 14
(12, 13, 1, 165000.00000, 165000.00000, 2),
(12, 14, 1, 175000.00000, 175000.00000, 2),
-- Đơn 13 mua sách 15 và 16
(13, 15, 1, 180000.00000, 180000.00000, 2),
(13, 16, 1, 180000.00000, 180000.00000, 2),
-- Đơn 14 mua sách 17 và 18
(14, 17, 1, 185000.00000, 185000.00000, 2),
(14, 18, 1, 195000.00000, 195000.00000, 2),
-- Đơn 15 mua sách 19 và 20
(15, 19, 1, 200000.00000, 200000.00000, 2),
(15, 20, 1, 200000.00000, 200000.00000, 2),
-- Đơn 16 mua sách 21 và 22
(16, 21, 1, 205000.00000, 205000.00000, 3),
(16, 22, 1, 215000.00000, 215000.00000, 3),
-- Đơn 17 mua sách 23 và 24
(17, 23, 1, 220000.00000, 220000.00000, 3),
(17, 24, 1, 220000.00000, 220000.00000, 3),
-- Đơn 18 mua sách 25 và 26
(18, 25, 1, 225000.00000, 225000.00000, 3),
(18, 26, 1, 235000.00000, 235000.00000, 3),
-- Đơn 19 mua sách 27 và 28
(19, 27, 1, 240000.00000, 240000.00000, 3),
(19, 28, 1, 240000.00000, 240000.00000, 3),
-- Đơn 20 mua sách 29
(20, 29, 2, 250000.00000, 500000.00000, 3);

-- Thêm vận đơn (Waybill) cho 10 đơn hàng tuần trước, trạng thái = 3 (SUCCESS - Đã giao hàng thành công)
INSERT INTO `waybill` 
(`id`, `created_at`, `updated_at`, `cod_amount`, `code`, `expected_delivery_time`, `ghn_payment_type_id`, `ghn_required_note`, `height`, `length`, `note`, `shipping_date`, `shipping_fee`, `status`, `weight`, `width`, `from_warehouse_id`, `order_id`, `shipper_id`)
VALUES
(11, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 'WAYBILL-LWK-011', DATE_SUB(NOW(), INTERVAL 2 DAY), 1, 'CHOTHUHANG', 10, 10, 'Giao thành công', DATE_SUB(NOW(), INTERVAL 5 DAY), 30000, 3, 300, 10, 1, 11, NULL),
(12, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 'WAYBILL-LWK-012', DATE_SUB(NOW(), INTERVAL 2 DAY), 1, 'CHOTHUHANG', 10, 10, 'Giao thành công', DATE_SUB(NOW(), INTERVAL 5 DAY), 30000, 3, 300, 10, 1, 12, NULL),
(13, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 'WAYBILL-LWK-013', DATE_SUB(NOW(), INTERVAL 2 DAY), 1, 'CHOTHUHANG', 10, 10, 'Giao thành công', DATE_SUB(NOW(), INTERVAL 5 DAY), 30000, 3, 300, 10, 1, 13, NULL),
(14, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 'WAYBILL-LWK-014', DATE_SUB(NOW(), INTERVAL 2 DAY), 1, 'CHOTHUHANG', 10, 10, 'Giao thành công', DATE_SUB(NOW(), INTERVAL 5 DAY), 30000, 3, 300, 10, 1, 14, NULL),
(15, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 'WAYBILL-LWK-015', DATE_SUB(NOW(), INTERVAL 2 DAY), 1, 'CHOTHUHANG', 10, 10, 'Giao thành công', DATE_SUB(NOW(), INTERVAL 5 DAY), 30000, 3, 300, 10, 2, 15, NULL),
(16, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 'WAYBILL-LWK-016', DATE_SUB(NOW(), INTERVAL 2 DAY), 1, 'CHOTHUHANG', 10, 10, 'Giao thành công', DATE_SUB(NOW(), INTERVAL 5 DAY), 30000, 3, 300, 10, 2, 16, NULL),
(17, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 'WAYBILL-LWK-017', DATE_SUB(NOW(), INTERVAL 2 DAY), 1, 'CHOTHUHANG', 10, 10, 'Giao thành công', DATE_SUB(NOW(), INTERVAL 5 DAY), 30000, 3, 300, 10, 2, 17, NULL),
(18, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 'WAYBILL-LWK-018', DATE_SUB(NOW(), INTERVAL 2 DAY), 1, 'CHOTHUHANG', 10, 10, 'Giao thành công', DATE_SUB(NOW(), INTERVAL 5 DAY), 30000, 3, 300, 10, 2, 18, NULL),
(19, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 'WAYBILL-LWK-019', DATE_SUB(NOW(), INTERVAL 2 DAY), 1, 'CHOTHUHANG', 10, 10, 'Giao thành công', DATE_SUB(NOW(), INTERVAL 5 DAY), 30000, 3, 300, 10, 2, 19, NULL),
(20, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), 0, 'WAYBILL-LWK-020', DATE_SUB(NOW(), INTERVAL 2 DAY), 1, 'CHOTHUHANG', 10, 10, 'Giao thành công', DATE_SUB(NOW(), INTERVAL 5 DAY), 30000, 3, 300, 10, 2, 20, NULL),
-- Thêm vận đơn chưa giao trong ngày hôm nay cho Đơn 2 và Đơn 3
(2, NOW(), NOW(), 0, 'WAYBILL-TODAY-002', DATE_ADD(NOW(), INTERVAL 3 DAY), 1, 'CHOTHUHANG', 10, 10, 'Chờ lấy hàng', NOW(), 30000, 1, 300, 10, 1, 2, NULL),
(3, NOW(), NOW(), 0, 'WAYBILL-TODAY-003', DATE_ADD(NOW(), INTERVAL 3 DAY), 1, 'CHOTHUHANG', 10, 10, 'Đang giao hàng', NOW(), 30000, 2, 300, 10, 2, 3, NULL);


-- ============================================================================
-- 6. THÊM ĐÁNH GIÁ (REVIEWS) GIẢ LẬP CHO CÁC SẢN PHẨM TỪ 1 ĐẾN 29
-- ============================================================================
INSERT INTO `review` (`id`, `created_at`, `updated_at`, `content`, `rating_score`, `reply`, `status`, `product_id`, `user_id`) VALUES
(1, NOW(), NOW(), 'Sách hay tuyệt vời, bọc sách rất cẩn thận!', 5, 'Cảm ơn quý khách đã mua hàng!', 1, 1, 6),
(2, NOW(), NOW(), 'Nội dung ý nghĩa, giao hàng siêu nhanh.', 5, 'Dạ cảm ơn bạn nhé!', 1, 2, 7),
(3, NOW(), NOW(), 'Sách hơi cũ tí nhưng nội dung ổn.', 4, '', 1, 3, 9),
(4, NOW(), NOW(), 'Rất đáng đọc, khuyên mọi người nên mua.', 5, '', 1, 4, 7),
(5, NOW(), NOW(), 'Sách viết rất lôi cuốn, dễ hiểu.', 5, '', 1, 5, 6),
(6, NOW(), NOW(), 'Chất lượng giấy tốt, đóng gói kỹ.', 4, '', 1, 6, 7),
(7, NOW(), NOW(), 'Sách hỗ trợ tốt cho công việc.', 5, '', 1, 7, 8),
(8, NOW(), NOW(), 'Khá thực tế, giao hàng đúng hẹn.', 4, '', 1, 8, 9),
(9, NOW(), NOW(), 'Bình thường, không quá đặc sắc.', 3, '', 1, 9, 6),
(10, NOW(), NOW(), 'Rất bổ ích, mua tặng bạn bè đều thích.', 5, '', 1, 10, 7),
(11, NOW(), NOW(), 'Nội dung chi tiết, trình bày đẹp.', 5, '', 1, 11, 8),
(12, NOW(), NOW(), 'Đọc rất thấm thĩa.', 5, '', 1, 12, 9),
(13, NOW(), NOW(), 'Có ích cho việc rèn luyện bản thân.', 4, '', 1, 13, 6),
(14, NOW(), NOW(), 'Nhiều ví dụ hay.', 5, '', 1, 14, 7),
(15, NOW(), NOW(), 'Giao hơi chậm một chút nhưng chất lượng OK.', 4, '', 1, 15, 8),
(16, NOW(), NOW(), 'Sách hay nhất từng đọc.', 5, '', 1, 16, 9),
(17, NOW(), NOW(), 'Lý thuyết hay và thực tế.', 5, '', 1, 17, 6),
(18, NOW(), NOW(), 'Rất hài lòng về cuốn sách này.', 5, '', 1, 18, 7),
(19, NOW(), NOW(), 'Sách đẹp, kiến thức tuyệt vời.', 5, '', 1, 19, 8),
(20, NOW(), NOW(), 'Tuyệt phẩm!', 5, '', 1, 20, 9),
(21, NOW(), NOW(), 'Nội dung sách chất lượng.', 4, '', 1, 21, 6),
(22, NOW(), NOW(), 'Giao hàng nhanh, đóng gói đẹp.', 5, '', 1, 22, 7),
(23, NOW(), NOW(), 'Khuyên mọi người nên đọc.', 5, '', 1, 23, 8),
(24, NOW(), NOW(), 'Kiến thức bổ ích.', 4, '', 1, 24, 9),
(25, NOW(), NOW(), 'Giấy hơi mỏng tí nhưng nội dung cực chất.', 4, '', 1, 25, 6),
(26, NOW(), NOW(), 'Tuyệt vời ông mặt trời.', 5, '', 1, 26, 7),
(27, NOW(), NOW(), 'Phù hợp mọi lứa tuổi.', 5, '', 1, 27, 8),
(28, NOW(), NOW(), 'Khá thực tế và dễ áp dụng.', 4, '', 1, 28, 9),
(29, NOW(), NOW(), 'Một cuốn sách tuyệt vời.', 5, '', 1, 29, 6);

-- Bật lại kiểm tra khóa ngoại sau khi đã chạy xong
SET FOREIGN_KEY_CHECKS = 1;
