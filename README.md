# Hệ thống Lấy ý kiến Phản hồi của Người học về Dịch vụ Đào tạo

## Trường Đại học Nguyễn Trãi
**Nghiên cứu Khoa học - K23CNT1**  
**Sinh viên: Nguyễn Quang Tâm**

---

## 📋 Mô tả Dự án

Hệ thống web cho phép người học gửi phản hồi về các dịch vụ đào tạo của trường đại học. Hệ thống hỗ trợ quản lý, duyệt và thống kê các phản hồi từ người học.

## ✨ Tính năng

- ✅ **Gửi phản hồi**: Người học có thể gửi phản hồi về dịch vụ đào tạo
- ✅ **Quản lý phản hồi**: Admin có thể duyệt/xóa phản hồi
- ✅ **Danh sách phản hồi**: Hiển thị các phản hồi đã được duyệt
- ✅ **Thống kê**: Xem thống kê về các dịch vụ đào tạo
- ✅ **Dark/Light Mode**: Chuyển đổi giao diện sáng/tối
- ✅ **Responsive Design**: Tối ưu cho mọi thiết bị (mobile, tablet, desktop)

## 🛠️ Công nghệ sử dụng

- **Backend**: 
  - Spring Boot 4.0.1
  - Spring Data JPA
  - MySQL
  - Java 21

- **Frontend**:
  - Tailwind CSS
  - Thymeleaf
  - Font Awesome Icons
  - JavaScript (ES6+)

## 📦 Cài đặt

### Yêu cầu
- Java 21+
- Maven 3.6+
- MySQL 8.0+

### Các bước cài đặt

1. **Clone repository**
```bash
git clone https://github.com/nqtam6666/k23cnt1.nqt.ykiennguoihoc.nckh.git
cd k23cnt1.nqt.ykiennguoihoc.nckh
```

2. **Cấu hình Database**
- Tạo database: `NCKH_FeedBack_NguoiHoc_db`
  ```sql
  CREATE DATABASE NCKH_FeedBack_NguoiHoc_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  ```
- **(Tùy chọn)** Tạo bảng thủ công bằng schema SQL:
  ```bash
  mysql -u root -p NCKH_FeedBack_NguoiHoc_db < sql/schema.sql
  ```
  Nếu dùng JPA với `ddl-auto=update` (mặc định), ứng dụng sẽ tự tạo/cập nhật bảng khi chạy; file `sql/schema.sql` dùng để tham khảo hoặc khởi tạo DB tay.
- Cập nhật thông tin kết nối trong `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3308/NCKH_FeedBack_NguoiHoc_db?useUnicode=true&characterEncoding=utf8&useSSL=false
spring.datasource.username=root
spring.datasource.password=your_password
```

3. **Chạy ứng dụng**
```bash
mvn spring-boot:run
```

4. **Truy cập ứng dụng**
- URL: http://localhost:8080
- Trang chủ: http://localhost:8080/
- Gửi phản hồi: http://localhost:8080/phan-hoi/form
- Admin: http://localhost:8080/admin/phan-hoi/cho-duyet

## 📁 Cấu trúc Dự án

```
src/
├── main/
│   ├── java/
│   │   └── k23cnt1/nguyenquangtam/nckh/
│   │       ├── Application.java
│   │       ├── config/
│   │       │   └── DataInitializer.java
│   │       ├── controller/
│   │       │   ├── AdminController.java
│   │       │   ├── HomeController.java
│   │       │   └── PhanHoiController.java
│   │       ├── dto/
│   │       │   └── PhanHoiDTO.java
│   │       ├── entity/
│   │       │   ├── DichVuDaoTao.java
│   │       │   ├── LoaiPhanHoi.java
│   │       │   ├── NguoiHoc.java
│   │       │   └── PhanHoi.java
│   │       ├── repository/
│   │       │   ├── DichVuDaoTaoRepository.java
│   │       │   ├── LoaiPhanHoiRepository.java
│   │       │   ├── NguoiHocRepository.java
│   │       │   └── PhanHoiRepository.java
│   │       └── service/
│   │           ├── DichVuDaoTaoService.java
│   │           ├── LoaiPhanHoiService.java
│   │           └── PhanHoiService.java
│   └── resources/
│       ├── application.properties
│       ├── static/
│       │   └── js/
│       │       └── dark-mode.js
│       └── templates/
│           ├── index.html
│           ├── thong-ke.html
│           ├── admin/
│           │   └── phan-hoi-cho-duyet.html
│           └── phan-hoi/
│               ├── form.html
│               ├── danh-sach.html
│               └── chi-tiet.html
└── test/
    └── java/
        └── k23cnt1/nguyenquangtam/nckh/
            └── ApplicationTests.java
```

## 🗄️ Database Schema

Schema SQL đầy đủ: **`sql/schema.sql`** (MySQL, utf8mb4).

### Các bảng chính:
- `nguoi_dung`: Tài khoản đăng nhập (admin, giang_vien, nguoi_hoc)
- `khoa`: Khoa
- `nhom_dich_vu`: Nhóm dịch vụ đào tạo
- `nguoi_hoc`: Thông tin người học (liên kết `nguoi_dung`, `khoa`)
- `giang_vien`: Giảng viên (liên kết `nguoi_dung`, `khoa`)
- `quan_tri`: Quản trị (liên kết `nguoi_dung`)
- `hoc_phan`: Học phần (liên kết `khoa`)
- `phan_cong`: Phân công giảng viên – học phần
- `khao_sat`: Khảo sát
- `cau_hoi`: Câu hỏi khảo sát (liên kết `khao_sat`, `nhom_dich_vu`)
- `phan_hoi`: Phản hồi của người học (liên kết `nguoi_hoc`, `khao_sat`)
- `chi_tiet_phan_hoi`: Chi tiết phản hồi từng câu hỏi (liên kết `phan_hoi`, `cau_hoi`)

## 🎨 Giao diện

- **Modern UI**: Sử dụng Tailwind CSS với thiết kế hiện đại
- **Dark Mode**: Hỗ trợ chế độ sáng/tối
- **Responsive**: Tối ưu cho mọi kích thước màn hình
- **User-friendly**: Giao diện thân thiện, dễ sử dụng

## 📝 License

Dự án nghiên cứu khoa học - Trường Đại học Nguyễn Trãi

## 👤 Tác giả

**Nguyễn Quang Tâm**  
- Lớp: K23CNT1
- Trường: Đại học Nguyễn Trãi

---

⭐ Nếu bạn thấy dự án này hữu ích, hãy cho một star!

