-- Tắt kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 0;

-- 1. XÓA DỮ LIỆU CŨ
TRUNCATE TABLE review;
TRUNCATE TABLE waybill_log;
TRUNCATE TABLE waybill;
TRUNCATE TABLE order_variant;
TRUNCATE TABLE `order`;
TRUNCATE TABLE order_resource;
TRUNCATE TABLE order_cancellation_reason;
TRUNCATE TABLE customer_resource;
TRUNCATE TABLE promotion_product;
TRUNCATE TABLE promotion;
TRUNCATE TABLE inventory_item;
TRUNCATE TABLE storage_location;
TRUNCATE TABLE warehouse_category;
TRUNCATE TABLE warehouse_product;
TRUNCATE TABLE warehouse;
TRUNCATE TABLE variant;
TRUNCATE TABLE product_category;
TRUNCATE TABLE product_tag;
TRUNCATE TABLE product;
TRUNCATE TABLE category;
TRUNCATE TABLE brand;
TRUNCATE TABLE supplier;
TRUNCATE TABLE unit;
TRUNCATE TABLE tag;
TRUNCATE TABLE property;
TRUNCATE TABLE specification;
TRUNCATE TABLE guarantee;
TRUNCATE TABLE wish;

-- 2. THÊM DỮ LIỆU MỚI

-- Thể loại (3 cấp)
INSERT INTO category (id, name, slug, level, parent_id, status, created_at, updated_at) VALUES
(1, 'Sách Văn Học', 'sach-van-hoc', 1, NULL, 1, NOW(), NOW()),
(2, 'Sách Kinh Tế', 'sach-kinh-te', 1, NULL, 1, NOW(), NOW()),
(3, 'Sách Kỹ Năng Sống', 'sach-ky-nang-song', 1, NULL, 1, NOW(), NOW()),
(4, 'Văn Học Việt Nam', 'van-hoc-viet-nam', 2, 1, 1, NOW(), NOW()),
(5, 'Văn Học Nước Ngoài', 'van-hoc-nuoc-ngoai', 2, 1, 1, NOW(), NOW()),
(6, 'Quản Trị - Lãnh Đạo', 'quan-tri-lanh-dao', 2, 2, 1, NOW(), NOW()),
(7, 'Tài Chính - Đầu Tư', 'tai-chinh-dau-tu', 2, 2, 1, NOW(), NOW()),
(8, 'Tiểu Thuyết Việt Nam', 'tieu-thuyet-viet-nam', 3, 4, 1, NOW(), NOW()),
(9, 'Truyện Ngắn Việt Nam', 'truyen-ngan-viet-nam', 3, 4, 1, NOW(), NOW()),
(10, 'Kinh Điển Thế Giới', 'kinh-dien-the-gioi', 3, 5, 1, NOW(), NOW()),
(11, 'Khởi Nghiệp', 'khoi-nghiep', 3, 6, 1, NOW(), NOW()),
(12, 'Chứng Khoán', 'chung-khoan', 3, 7, 1, NOW(), NOW());

-- Tác giả (Brand)
INSERT INTO brand (id, name, code, status, created_at, updated_at) VALUES
(1, 'Nguyễn Nhật Ánh', 'NNA', 1, NOW(), NOW()),
(2, 'Nam Cao', 'NC', 1, NOW(), NOW()),
(3, 'Haruki Murakami', 'HM', 1, NOW(), NOW()),
(4, 'Dale Carnegie', 'DC', 1, NOW(), NOW()),
(5, 'Robert Kiyosaki', 'RK', 1, NOW(), NOW()),
(6, 'Tony Buổi Sáng', 'TBS', 1, NOW(), NOW());

-- Nhà xuất bản (Supplier)
INSERT INTO supplier (id, display_name, code, status, created_at, updated_at) VALUES
(1, 'NXB Trẻ', 'NXBTRE', 1, NOW(), NOW()),
(2, 'NXB Kim Đồng', 'NXBKD', 1, NOW(), NOW()),
(3, 'NXB Hội Nhà Văn', 'NXBHNV', 1, NOW(), NOW()),
(4, 'Nhã Nam', 'NHANAM', 1, NOW(), NOW()),
(5, 'Alphabooks', 'ALPHA', 1, NOW(), NOW()),
(6, 'First News', 'FN', 1, NOW(), NOW());

-- Đơn vị tính
INSERT INTO unit (id, name, status, created_at, updated_at) VALUES
(1, 'Cuốn', 1, NOW(), NOW()),
(2, 'Bộ', 1, NOW(), NOW()),
(3, 'Tờ', 1, NOW(), NOW()),
(4, 'Hộp', 1, NOW(), NOW()),
(5, 'Cái', 1, NOW(), NOW());

-- Tag
INSERT INTO tag (id, name, slug, status, created_at, updated_at) VALUES
(1, 'Bán chạy', 'ban-chay', 1, NOW(), NOW()),
(2, 'Mới về', 'moi-ve', 1, NOW(), NOW()),
(3, 'Khuyên đọc', 'khuyen-doc', 1, NOW(), NOW()),
(4, 'Giảm giá', 'giam-gia', 1, NOW(), NOW()),
(5, 'Kinh điển', 'kinh-dien', 1, NOW(), NOW());

-- Thuộc tính sách (Property)
INSERT INTO property (id, name, code, status, created_at, updated_at) VALUES
(1, 'Loại bìa', 'LOAIBIA', 1, NOW(), NOW()),
(2, 'Ngôn ngữ', 'NGONNGU', 1, NOW(), NOW()),
(3, 'Chất liệu giấy', 'CHATLIEU', 1, NOW(), NOW()),
(4, 'Màu sắc', 'MAUSAC', 1, NOW(), NOW()),
(5, 'Kèm quà tặng', 'QUATANG', 1, NOW(), NOW());

-- Thông số sách (Specification)
INSERT INTO specification (id, name, code, status, created_at, updated_at) VALUES
(1, 'Số trang', 'SOTRANG', 1, NOW(), NOW()),
(2, 'Kích thước', 'KICHTHUOC', 1, NOW(), NOW()),
(3, 'Trọng lượng', 'TRONGLUONG', 1, NOW(), NOW()),
(4, 'Năm xuất bản', 'NAMXB', 1, NOW(), NOW()),
(5, 'Định dạng', 'DINHDANG', 1, NOW(), NOW());

-- Bảo hành
INSERT INTO guarantee (id, name, description, status, created_at, updated_at) VALUES
(1, 'Không bảo hành', 'Sản phẩm không áp dụng chính sách bảo hành', 1, NOW(), NOW()),
(2, 'Đổi trả trong 7 ngày', 'Cho phép đổi trả nếu có lỗi nhà sản xuất trong 7 ngày', 1, NOW(), NOW()),
(3, 'Đổi trả trong 30 ngày', 'Cho phép đổi trả trong vòng 30 ngày', 1, NOW(), NOW()),
(4, 'Bảo hành 6 tháng', 'Bảo hành kỹ thuật 6 tháng', 1, NOW(), NOW()),
(5, 'Bảo hành 1 năm', 'Bảo hành chính hãng 1 năm', 1, NOW(), NOW());

-- Nguồn khách hàng (Customer Resource)
INSERT INTO customer_resource (id, code, name, description, color, status, created_at, updated_at) VALUES
(1, 'RETAIL', 'Khách lẻ', 'Khách mua tại cửa hàng hoặc website', '#3498db', 1, NOW(), NOW()),
(2, 'WHOLESALE', 'Khách sỉ', 'Khách mua số lượng lớn', '#2ecc71', 1, NOW(), NOW());

-- Nguồn đơn hàng (Order Resource)
INSERT INTO order_resource (id, code, name, color, status, customer_resource_id, created_at, updated_at) VALUES
(1, 'WEBSITE', 'Website', '#3498db', 1, 1, NOW(), NOW()),
(2, 'MOBILE_APP', 'Mobile App', '#9b59b6', 1, 1, NOW(), NOW()),
(3, 'POS', 'Tại quầy', '#f1c40f', 1, 1, NOW(), NOW());

-- Lý do hủy đơn (Order Cancellation Reason)
INSERT INTO order_cancellation_reason (id, name, note, status, created_at, updated_at) VALUES
(1, 'Đổi ý không mua nữa', 'Khách hàng thay đổi quyết định', 1, NOW(), NOW()),
(2, 'Tìm thấy giá rẻ hơn', 'Khách hàng tìm thấy nơi khác rẻ hơn', 1, NOW(), NOW()),
(3, 'Thời gian giao hàng quá lâu', 'Thời gian vận chuyển không đáp ứng', 1, NOW(), NOW()),
(4, 'Sai thông tin đặt hàng', 'Khách hàng nhập sai thông tin', 1, NOW(), NOW());

-- 30 Cuốn sách (Product)
INSERT INTO product (id, name, code, slug, short_description, description, status, brand_id, supplier_id, unit_id, guarantee_id, weight, specifications, properties, created_at, updated_at) VALUES
(1, 'Mắt Biếc', 'PROD001', 'mat-biec', 'Truyện dài nổi tiếng của Nguyễn Nhật Ánh', 'Nội dung kể về tình yêu đơn phương của Ngạn dành cho Hà Lan...', 1, 1, 1, 1, 2, 0.3, '{"1": "300", "4": "2019"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(2, 'Cho Tôi Xin Một Vé Đi Tuổi Thơ', 'PROD002', 'cho-toi-xin-mot-ve-di-tuoi-tho', 'Tác phẩm dành cho thiếu nhi và người lớn', 'Câu chuyện về những trò nghịch ngợm của trẻ con...', 1, 1, 1, 1, 2, 0.25, '{"1": "200", "4": "2018"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(3, 'Chí Phèo', 'PROD003', 'chi-pheo', 'Tác phẩm kinh điển của Nam Cao', 'Hình ảnh người nông dân bị tha hóa trong xã hội cũ...', 1, 2, 3, 1, 1, 0.2, '{"1": "150", "4": "2020"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(4, 'Rừng Na Uy', 'PROD004', 'rung-na-uy', 'Tiểu thuyết hiện đại Nhật Bản', 'Câu chuyện về những người trẻ cô đơn...', 1, 3, 4, 1, 2, 0.4, '{"1": "500", "4": "2015"}', '{"1": "Bìa cứng"}', NOW(), NOW()),
(5, 'Đắc Nhân Tâm', 'PROD005', 'dac-nhan-tam', 'Sách kỹ năng sống bán chạy nhất mọi thời đại', 'Nghệ thuật thu phục lòng người...', 1, 4, 6, 1, 3, 0.35, '{"1": "320", "4": "2021"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(6, 'Cha Giàu Cha Nghèo', 'PROD006', 'cha-giau-cha-nghèo', 'Sách dạy về tư duy tài chính', 'Sự khác biệt giữa người giàu và người nghèo...', 1, 5, 5, 1, 3, 0.3, '{"1": "400", "4": "2017"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(7, 'Trên Đường Băng', 'PROD007', 'tren-duong-bang', 'Cảm hứng khởi nghiệp cho giới trẻ', 'Những bài viết truyền cảm hứng của Dượng Tony...', 1, 6, 1, 1, 2, 0.28, '{"1": "300", "4": "2016"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(8, 'Tôi Thấy Hoa Vàng Trên Cỏ Xanh', 'PROD008', 'toi-thay-hoa-vang-tren-co-xanh', 'Truyện dài của Nguyễn Nhật Ánh', 'Ký ức tuổi thơ ở làng quê nghèo...', 1, 1, 1, 1, 2, 0.3, '{"1": "280", "4": "2015"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(9, 'Lão Hạc', 'PROD009', 'lao-hac', 'Truyện ngắn của Nam Cao', 'Nỗi đau của người nông dân...', 1, 2, 3, 1, 1, 0.15, '{"1": "100", "4": "2020"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(10, 'Kafka Bên Bờ Biển', 'PROD010', 'kafka-ben-bo-bien', 'Kiệt tác của Murakami', 'Hành trình kỳ ảo của cậu bé Kafka...', 1, 3, 4, 1, 2, 0.5, '{"1": "600", "4": "2018"}', '{"1": "Bìa cứng"}', NOW(), NOW()),
(11, 'Quẳng Gánh Lo Đi Và Vui Sống', 'PROD011', 'quang-ganh-lo-di-va-vui-song', 'Sách giúp giải tỏa áp lực cuộc sống', 'Làm sao để bớt lo lắng...', 1, 4, 6, 1, 2, 0.32, '{"1": "250", "4": "2021"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(12, 'Dạy Con Làm Giàu - Tập 2', 'PROD012', 'day-con-lam-giau-tap-2', 'Hướng dẫn đầu tư', 'Kim tứ đồ và tự do tài chính...', 1, 5, 5, 1, 3, 0.35, '{"1": "450", "4": "2017"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(13, 'Cà Phê Cùng Tony', 'PROD013', 'ca-phe-cung-tony', 'Tập hợp các bài viết của Tony Buổi Sáng', 'Chuyện làm ăn, chuyện đời...', 1, 6, 1, 1, 2, 0.25, '{"1": "260", "4": "2016"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(14, 'Cô Gái Đến Từ Hôm Qua', 'PROD014', 'co-gai-den-tu-hom-qua', 'Truyện tình cảm tuổi học trò', 'Câu chuyện về Anh Thư và Tiểu Li...', 1, 1, 1, 1, 2, 0.28, '{"1": "240", "4": "2014"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(15, 'Sống Mòn', 'PROD015', 'song-mon', 'Tiểu thuyết của Nam Cao', 'Cuộc sống bế tắc của trí thức nghèo...', 1, 2, 3, 1, 1, 0.35, '{"1": "350", "4": "2020"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(16, 'Phía Sau Nghi Can X', 'PROD016', 'phia-sau-nghi-can-x', 'Trinh thám Nhật Bản', 'Vụ án hoàn hảo của Ishigami...', 1, 3, 4, 1, 3, 0.45, '{"1": "400", "4": "2019"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(17, 'Bảy Bước Tới Mùa Hè', 'PROD017', 'bay-buoc-toi-mua-he', 'Truyện dài Nguyễn Nhật Ánh', 'Mùa hè rực rỡ và những kỷ niệm...', 1, 1, 1, 1, 2, 0.3, '{"1": "290", "4": "2015"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(18, 'Nhà Giả Kim', 'PROD018', 'nha-gia-kim', 'Cuốn sách bán chạy chỉ sau Kinh Thánh', 'Hành trình theo đuổi ước mơ...', 1, 3, 4, 1, 3, 0.2, '{"1": "220", "4": "2020"}', '{"1": "Bìa cứng"}', NOW(), NOW()),
(19, 'Sức Mạnh Của Thói Quen', 'PROD019', 'suc-manh-cua-thoi-quen', 'Dành cho ai muốn thay đổi bản thân', 'Cơ chế hoạt động của thói quen...', 1, 4, 5, 1, 3, 0.42, '{"1": "480", "4": "2018"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(20, 'Đừng Bao Giờ Đi Ăn Một Mình', 'PROD020', 'dung-bao-gio-di-an-mot-minh', 'Kỹ năng giao tiếp và kết nối', 'Xây dựng mạng lưới quan hệ...', 1, 4, 6, 1, 3, 0.38, '{"1": "380", "4": "2019"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(21, 'Tư Duy Nhanh Và Chậm', 'PROD021', 'tu-duy-nhanh-va-cham', 'Phân tích về tâm lý học hành vi', 'Hai hệ thống tư duy của con người...', 1, 4, 5, 1, 3, 0.6, '{"1": "700", "4": "2021"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(22, 'Chiến Tranh Và Hòa Bình', 'PROD022', 'chien-tranh-va-hoa-binh', 'Kiệt tác của Lev Tolstoy', 'Bức tranh xã hội Nga thời Napoléon...', 1, 3, 3, 1, 5, 1.5, '{"1": "1500", "4": "2010"}', '{"1": "Bìa cứng"}', NOW(), NOW()),
(23, 'Những Người Khốn Khổ', 'PROD023', 'nhung-nguoi-khon-kho', 'Tác phẩm của Victor Hugo', 'Cuộc đời của Jean Valjean...', 1, 3, 4, 1, 5, 1.2, '{"1": "1200", "4": "2012"}', '{"1": "Bìa cứng"}', NOW(), NOW()),
(24, 'Bàn Về Tự Do', 'PROD024', 'ban-ve-tu-do', 'Triết học kinh điển', 'Quyền tự do của cá nhân...', 1, 3, 5, 1, 3, 0.25, '{"1": "200", "4": "2015"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(25, 'Tiếng Chim Hót Trong Bụi Mận Gai', 'PROD025', 'tieng-chim-hot-trong-bui-man-gai', 'Tiểu thuyết lãng mạn', 'Chuyện tình giữa Meggie và cha Ralph...', 1, 3, 4, 1, 3, 0.7, '{"1": "800", "4": "2013"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(26, 'Hoàng Tử Bé', 'PROD026', 'hoang-tu-be', 'Cuốn sách của mọi lứa tuổi', 'Chuyến du hành qua các tiểu hành tinh...', 1, 3, 4, 1, 1, 0.15, '{"1": "100", "4": "2021"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(27, 'Bố Già', 'PROD027', 'bo-gia', 'Tiểu thuyết về mafia Mỹ', 'Gia đình Corleone...', 1, 3, 6, 1, 5, 0.5, '{"1": "550", "4": "2018"}', '{"1": "Bìa cứng"}', NOW(), NOW()),
(28, 'Không Gia Đình', 'PROD028', 'khong-gia-dinh', 'Hành trình của cậu bé Rémi', 'Nghị lực và lòng nhân ái...', 1, 3, 2, 1, 2, 0.6, '{"1": "600", "4": "2019"}', '{"1": "Bìa mềm"}', NOW(), NOW()),
(29, 'Tội Ác Và Hình Phạt', 'PROD029', 'toi-ac-va-hinh-phat', 'Kiệt tác tâm lý của Dostoevsky', 'Sự dằn vặt của Raskolnikov...', 1, 3, 3, 1, 5, 0.8, '{"1": "750", "4": "2020"}', '{"1": "Bìa cứng"}', NOW(), NOW()),
(30, 'Điểm Đến Của Cuộc Đời', 'PROD030', 'diem-den-cua-cuoc-doi', 'Câu chuyện về ranh giới sinh tử', 'Góc nhìn về cái chết để trân trọng sự sống...', 1, 2, 4, 1, 2, 0.28, '{"1": "300", "4": "2017"}', '{"1": "Bìa mềm"}', NOW(), NOW());

-- Liên kết Sách - Thể loại
INSERT INTO product_category (product_id, category_id) VALUES
(1, 8), (2, 9), (3, 9), (4, 10), (5, 3), (6, 7), (7, 11), (8, 8), (9, 9), (10, 10),
(11, 3), (12, 12), (13, 11), (14, 8), (15, 8), (16, 10), (17, 8), (18, 10), (19, 3), (20, 6),
(21, 3), (22, 10), (23, 10), (24, 6), (25, 10), (26, 10), (27, 10), (28, 10), (29, 10), (30, 3);

-- Biến thể sách (Số lượng tổng set về 0 và KHÔNG CÓ nhà kho)
INSERT INTO variant (id, product_id, sku, price, cost, quantity, status, created_at, updated_at)
SELECT id, id, CONCAT('SKU-', id), 100000 + (id * 5000), 50000 + (id * 2000), 0, 1, NOW(), NOW() FROM product;

-- Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 1;
