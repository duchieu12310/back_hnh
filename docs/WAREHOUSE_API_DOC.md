# Tài liệu API - Phân hệ Warehouse (Quản lý Kho)

Hệ thống quản lý kho tổng, vị trí lưu trữ và luân chuyển hàng hóa.

## 📋 Mục lục
1. [Chương 1: Warehouse (Kho tổng)](#chương-1-warehouse-kho-tổng)
2. [Chương 2: Inbound & Storage (Vị trí lưu trữ)](#chương-2-inbound--storage-vị-trí-lưu-trữ)
3. [Chương 3: TransitWarehouse (Trạm trung chuyển)](#chương-3-transitwarehouse-trạm-trung-chuyển)
4. [Chương 4: Transit (Luân chuyển hàng hóa)](#chương-4-transit-luân-chuyển-hàng-hóa)

---

## Chương 1: Warehouse (Kho tổng)
Quản lý hạ tầng lưu trữ tĩnh.

### 1.1. Lấy danh sách kho tổng
- **Endpoint**: `GET /api/warehouses`
- **Query Params**: `page`, `size`, `sort`, `search`

### 1.2. Tạo kho tổng mới
- **Endpoint**: `POST /api/warehouses`
- **Body**:
```json
{
  "name": "Kho trung tâm Miền Bắc",
  "code": "WH-MB-01",
  "status": 1, 
  "addressId": 123,
  "categoryIds": [1, 2, 5]
}
```
*Ghi chú: status (1: Hoạt động, 2: Bảo trì, 3: Đóng cửa)*

---

## Chương 2: Inbound & Storage (Vị trí lưu trữ)
Quản lý vị trí chính xác và thay đổi tồn kho trực tiếp trên `Variant`.

### 2.1. Tạo vị trí lưu trữ
- **Endpoint**: `POST /api/storage-locations`
- **Body**:
```json
{
  "aisle": "A1",
  "shelf": "S10",
  "bin": "B01",
  "variantId": 456
}
```

### 2.2. Điều chỉnh số lượng tồn kho (Hành động trực tiếp)
Tăng hoặc giảm số lượng `quantity` của Variant đang nằm tại vị trí này.
- **Endpoint**: `PUT /api/storage-locations/{id}/adjust-quantity`
- **Body**:
```json
{
  "adjustment": 10
}
```
*Ghi chú: `adjustment` có thể âm (-) để bớt hàng hoặc dương (+) để thêm hàng.*

### 2.3. Xóa tất cả tồn kho tại vị trí (Clear Stock)
Đặt số lượng của Variant tại vị trí này về 0.
- **Endpoint**: `PUT /api/storage-locations/{id}/clear-quantity`
- **Body**: *Trống*

---

## Chương 3: TransitWarehouse (Trạm trung chuyển)
Các kho "động" phục vụ vận chuyển.

### 3.1. Tạo kho trung chuyển
- **Endpoint**: `POST /api/transit-warehouses`
- **Body**:
```json
{
  "name": "Bưu cục Quận 1",
  "addressId": 789,
  "warehouseId": 1 // Link về kho tổng quản lý
}
```

---

## Chương 4: Transit (Luân chuyển hàng hóa)
Quy trình chuyển hàng từ kho tổng sang kho vận chuyển.

### 4.1. Khởi tạo luân chuyển
- **Endpoint**: `POST /api/transit-items`
- **Body**:
```json
{
  "transitWarehouseId": 5,
  "variantId": 456,
  "quantity": 20
}
```

### 🛠 Quy tắc nghiệp vụ (Validation & Logic):
1. **Kiểm tra tồn kho**: Trước khi luân chuyển, hệ thống kiểm tra `quantity_input <= Variant.quantity`.
2. **Trừ tồn kho tổng**: Ngay khi tạo `TransitItem`, số lượng `Variant.quantity` tại kho tổng sẽ bị trừ đi tương ứng.
3. **Hiển thị**: Luồng hiển thị dữ liệu tuân thủ Category -> Product -> Variant.

---
*Tài liệu được cập nhật ngày: 2026-04-11*
