# Spring Security 7: ví dụ 1, 2, 3

Dự án Spring Boot 4.1.1, Spring Security 7, Thymeleaf, MapStruct. Ba ví dụ được chọn bằng `APP_EXAMPLE=1`, `2` hoặc `3`. Mặc định là ví dụ 3. Các chức năng được xây dựng dựa trên bốn PDF bài học đã cung cấp.

| Ví dụ | Nội dung |
|---|---|
| 1 | Đăng nhập bằng email; hiển thị email, họ tên và role ở header bằng Thymeleaf thuần. |
| 2 | Đăng nhập bằng username hoặc email; principal riêng mang họ tên, ảnh và role; header dùng Spring Security dialect. |
| 3 | Đăng ký và kích hoạt OTP, đăng nhập session, quên mật khẩu bằng OTP, CRUD/search/pagination Users và Products, đếm users/products và số products của mỗi user, upload ảnh Cloudinary. |

## Chạy nhanh

Yêu cầu JDK 25 và Maven 3.9+. Dự án dùng H2 trong bộ nhớ theo mặc định; không cần cài SQL Server để thử.

```powershell
cd spring-security-vd123
.\mvnw.cmd spring-boot:run
```

Mở `http://localhost:8088/login`. Tài khoản mẫu: `admin@example.com` / `ChangeMe123!` và `user@example.com` / `ChangeMe123!`. Với ví dụ 2/3, cũng có thể nhập `admin` hoặc `user01`. Đổi mật khẩu mẫu trước khi đưa lên server công khai.

Chọn ví dụ khác trong PowerShell:

```powershell
$env:APP_EXAMPLE='1'
.\mvnw.cmd spring-boot:run
```

## SQL Server, mail và ảnh

Sao chép `.env.example` thành `.env`, tạo database `spring_security_vd123`, rồi sửa các giá trị phù hợp. `.env` đã được bỏ qua bởi Git. Để dùng H2, không tạo `.env`. `DDL_AUTO=update` dành cho bài học; cân nhắc migration khi triển khai thực tế.

Ở chế độ thử, OTP được ghi vào log ứng dụng (`DEMO MAIL`). Đặt `MAIL_ENABLED=true` và điền SMTP để gửi email thật. Cloudinary chỉ cần khi upload ảnh sản phẩm; điền ba biến `CLOUDINARY_*`. Tài khoản đăng ký chưa được kích hoạt trước khi xác thực OTP.

Đăng xuất dùng POST và CSRF. User thường chỉ sửa/xóa sản phẩm của mình; admin quản lý mọi sản phẩm và user. Dữ liệu H2 sẽ mất khi tắt ứng dụng.

## Kiểm thử

```powershell
.\mvnw.cmd test
```

Các bài test kiểm tra đăng nhập theo từng ví dụ và quyền truy cập của user/admin.
