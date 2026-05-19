-- ============================================================================
-- SQL SCRIPT: CẬP NHẬT THÔNG TIN NHÀ KHO VÀ THỐNG KÊ CHI TIẾT SẢN PHẨM & DANH MỤC & SỐ LƯỢNG & ĐÁNH GIÁ & VẬN ĐƠN
-- ============================================================================

-- 1. THÊM CỘT NHÀ KHO (STORAGE LOCATION) VÀO BẢNG CHI TIẾT ĐƠN HÀNG (ORDER VARIANT)
-- Sử dụng Stored Procedure để tránh lỗi trùng lặp cột (Error 1060) khi chạy lại file nhiều lần
DROP PROCEDURE IF EXISTS AddStorageLocationColumn;
DELIMITER //
CREATE PROCEDURE AddStorageLocationColumn()
BEGIN
    DECLARE col_count INT;
    SELECT COUNT(*) INTO col_count
    FROM information_schema.columns 
    WHERE table_schema = DATABASE()
      AND table_name = 'order_variant' 
      AND column_name = 'storage_location_id';
      
    IF col_count = 0 THEN
        ALTER TABLE `order_variant`
          ADD COLUMN `storage_location_id` BIGINT NULL AFTER `variant_id`,
          ADD CONSTRAINT `FK_order_variant_storage_location`
              FOREIGN KEY (`storage_location_id`) REFERENCES `storage_location`(`id`);
    END IF;
END //
DELIMITER ;
CALL AddStorageLocationColumn();
DROP PROCEDURE IF EXISTS AddStorageLocationColumn;

-- 2. CẬP NHẬT DỮ LIỆU BAN ĐẦU CHO CỘT MỚI THÊM TỪ BẢNG INVENTORY ITEM
-- Tạm thời tắt Safe Updates để chạy lệnh cập nhật hàng loạt không có WHERE key
SET SQL_SAFE_UPDATES = 0;

UPDATE `order_variant` ov
JOIN `inventory_item` ii
  ON ii.`variant_id` = ov.`variant_id`
SET ov.`storage_location_id` = ii.`storage_location_id`;

SET SQL_SAFE_UPDATES = 1;


-- ============================================================================
-- 3. BÁO CÁO THỐNG KÊ ĐƠN HÀNG HÔM NAY (TODAY'S ORDERS REPORT)
-- ============================================================================

-- 3.1. Xem chi tiết sản phẩm, danh mục, số lượng, điểm đánh giá, nhà kho và vận đơn của đơn đặt hôm nay
SELECT 
    o.`code` AS order_code,
    o.`created_at` AS order_date,
    w.`code` AS waybill_code,
    w.`status` AS waybill_status,
    p.`name` AS product_name,
    v.`sku` AS variant_sku,
    c.`name` AS category_name,
    ov.`quantity` AS sold_quantity,
    ov.`price` AS price,
    ov.`amount` AS total_amount,
    COALESCE(pr.avg_rating, 0) AS avg_product_rating,
    COALESCE(pr.total_reviews, 0) AS total_reviews_count,
    wh.`name` AS warehouse_name,
    wh.`code` AS warehouse_code,
    sl.`aisle` AS warehouse_aisle,
    sl.`shelf` AS warehouse_shelf
FROM `order` o
JOIN `order_variant` ov ON ov.`order_id` = o.`id`
JOIN `variant` v ON v.`id` = ov.`variant_id`
JOIN `product` p ON p.`id` = v.`product_id`
LEFT JOIN `category` c ON c.`id` = p.`category_id`
LEFT JOIN `storage_location` sl ON sl.`id` = ov.`storage_location_id`
LEFT JOIN `warehouse` wh ON wh.`id` = sl.`warehouse_id`
LEFT JOIN `waybill` w ON w.`order_id` = o.`id`
LEFT JOIN (
    -- Lấy điểm đánh giá trung bình và số lượng đánh giá của mỗi sản phẩm (đã duyệt, status = 1)
    SELECT `product_id`, AVG(`rating_score`) AS avg_rating, COUNT(`id`) AS total_reviews
    FROM `review`
    WHERE `status` = 1
    GROUP BY `product_id`
) pr ON pr.`product_id` = p.`id`
WHERE DATE(o.`created_at`) = CURDATE();

-- 3.2. Tổng hợp số lượng bán (SUM) & Điểm đánh giá trung bình sản phẩm đặt ngày hôm nay
SELECT 
    p.`name` AS product_name,
    v.`sku` AS variant_sku,
    c.`name` AS category_name,
    SUM(ov.`quantity`) AS total_quantity_sold,
    SUM(ov.`amount`) AS total_revenue,
    COALESCE(pr.avg_rating, 0) AS avg_product_rating,
    COALESCE(pr.total_reviews, 0) AS total_reviews_count
FROM `order` o
JOIN `order_variant` ov ON ov.`order_id` = o.`id`
JOIN `variant` v ON v.`id` = ov.`variant_id`
JOIN `product` p ON p.`id` = v.`product_id`
LEFT JOIN `category` c ON c.`id` = p.`category_id`
LEFT JOIN (
    SELECT `product_id`, AVG(`rating_score`) AS avg_rating, COUNT(`id`) AS total_reviews
    FROM `review`
    WHERE `status` = 1
    GROUP BY `product_id`
) pr ON pr.`product_id` = p.`id`
WHERE DATE(o.`created_at`) = CURDATE()
GROUP BY p.`id`, v.`id`, c.`id`, pr.avg_rating, pr.total_reviews;

-- 3.3. Xem danh sách đơn hàng CHƯA GIAO trong ngày hôm nay (Chưa tạo vận đơn hoặc vận đơn chưa giao thành công/bị hủy)
SELECT 
    o.`code` AS order_code,
    o.`created_at` AS order_date,
    o.`status` AS order_status_code,
    CASE 
        WHEN o.`status` = 1 THEN 'Đơn hàng mới'
        WHEN o.`status` = 2 THEN 'Đang xử lý'
        WHEN o.`status` = 3 THEN 'Đang giao hàng'
        WHEN o.`status` = 4 THEN 'Hoàn thành'
        WHEN o.`status` = 5 THEN 'Đã hủy'
        ELSE 'Không xác định'
    END AS order_status_text,
    w.`code` AS waybill_code,
    COALESCE(w.`status`, 0) AS waybill_status_code,
    CASE 
        WHEN w.`id` IS NULL THEN 'Chưa tạo vận đơn'
        WHEN w.`status` = 1 THEN 'Đang đợi lấy hàng'
        WHEN w.`status` = 2 THEN 'Đang giao hàng'
        WHEN w.`status` = 4 THEN 'Đã hủy vận đơn'
        ELSE 'Khác'
    END AS waybill_status_text,
    p.`name` AS product_name,
    ov.`quantity` AS sold_quantity,
    wh.`name` AS warehouse_name
FROM `order` o
JOIN `order_variant` ov ON ov.`order_id` = o.`id`
JOIN `variant` v ON v.`id` = ov.`variant_id`
JOIN `product` p ON p.`id` = v.`product_id`
LEFT JOIN `storage_location` sl ON sl.`id` = ov.`storage_location_id`
LEFT JOIN `warehouse` wh ON wh.`id` = sl.`warehouse_id`
LEFT JOIN `waybill` w ON w.`order_id` = o.`id`
WHERE DATE(o.`created_at`) = CURDATE()
  AND (w.`id` IS NULL OR w.`status` NOT IN (3, 4)); -- 3: Giao thành công, 4: Hủy vận đơn



-- ============================================================================
-- 4. BÁO CÁO ĐƠN HÀNG TUẦN TRƯỚC ĐÃ KẾT THÚC VẬN ĐƠN (LAST WEEK FINISHED WAYBILLS REPORT)
-- ============================================================================

-- 4.1. Xem chi tiết sản phẩm, danh mục, số lượng, điểm đánh giá, nhà kho và vận đơn của các đơn đặt tuần trước đã giao thành công (waybill status = 3)
SELECT 
    o.`code` AS order_code,
    o.`created_at` AS order_date,
    w.`code` AS waybill_code,
    w.`status` AS waybill_status,
    p.`name` AS product_name,
    v.`sku` AS variant_sku,
    c.`name` AS category_name,
    ov.`quantity` AS sold_quantity,
    ov.`price` AS price,
    ov.`amount` AS total_amount,
    COALESCE(pr.avg_rating, 0) AS avg_product_rating,
    COALESCE(pr.total_reviews, 0) AS total_reviews_count,
    wh.`name` AS warehouse_name,
    wh.`code` AS warehouse_code,
    sl.`aisle` AS warehouse_aisle,
    sl.`shelf` AS warehouse_shelf
FROM `order` o
JOIN `waybill` w ON w.`order_id` = o.`id`
JOIN `order_variant` ov ON ov.`order_id` = o.`id`
JOIN `variant` v ON v.`id` = ov.`variant_id`
JOIN `product` p ON p.`id` = v.`product_id`
LEFT JOIN `category` c ON c.`id` = p.`category_id`
LEFT JOIN `storage_location` sl ON sl.`id` = ov.`storage_location_id`
LEFT JOIN `warehouse` wh ON wh.`id` = sl.`warehouse_id`
LEFT JOIN (
    SELECT `product_id`, AVG(`rating_score`) AS avg_rating, COUNT(`id`) AS total_reviews
    FROM `review`
    WHERE `status` = 1
    GROUP BY `product_id`
) pr ON pr.`product_id` = p.`id`
WHERE w.`status` = 3
  AND o.`created_at` >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
  AND o.`created_at` < CURDATE();

-- 4.2. Tổng hợp số lượng bán (SUM) & Điểm đánh giá trung bình sản phẩm đặt tuần trước đã kết thúc vận đơn
SELECT 
    p.`name` AS product_name,
    v.`sku` AS variant_sku,
    c.`name` AS category_name,
    SUM(ov.`quantity`) AS total_quantity_sold,
    SUM(ov.`amount`) AS total_revenue,
    COALESCE(pr.avg_rating, 0) AS avg_product_rating,
    COALESCE(pr.total_reviews, 0) AS total_reviews_count
FROM `order` o
JOIN `waybill` w ON w.`order_id` = o.`id`
JOIN `order_variant` ov ON ov.`order_id` = o.`id`
JOIN `variant` v ON v.`id` = ov.`variant_id`
JOIN `product` p ON p.`id` = v.`product_id`
LEFT JOIN `category` c ON c.`id` = p.`category_id`
LEFT JOIN (
    SELECT `product_id`, AVG(`rating_score`) AS avg_rating, COUNT(`id`) AS total_reviews
    FROM `review`
    WHERE `status` = 1
    GROUP BY `product_id`
) pr ON pr.`product_id` = p.`id`
WHERE w.`status` = 3
  AND o.`created_at` >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
  AND o.`created_at` < CURDATE()
GROUP BY p.`id`, v.`id`, c.`id`, pr.avg_rating, pr.total_reviews;


-- ============================================================================
-- 5. BÁO CÁO CHI TIẾT CÁC ĐÁNH GIÁ (REVIEWS) CỦA SẢN PHẨM TRONG ĐƠN HÀNG ĐÃ BÁN
-- ============================================================================
SELECT 
    p.`name` AS product_name,
    u.`username` AS reviewer_name,
    r.`rating_score` AS rating,
    r.`content` AS review_content,
    r.`reply` AS admin_reply,
    r.`created_at` AS review_date
FROM `review` r
JOIN `product` p ON p.`id` = r.`product_id`
JOIN `user` u ON u.`id` = r.`user_id`
ORDER BY r.`created_at` DESC;
