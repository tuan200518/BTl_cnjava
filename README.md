# BTl_java - Quản lý nhân sự

## Chạy bằng Visual Studio Code / Maven

Yêu cầu: JDK 21, Maven, MySQL Server.

1. Import `database/dulieumau.sql` vào MySQL.
2. Kiểm tra tài khoản/mật khẩu MySQL trong `src/config/DBConnection.java`.
3. Mở đúng thư mục có `pom.xml` bằng VS Code.
4. Terminal:

```powershell
mvn clean compile
mvn exec:java
```

Maven sẽ tự tải MySQL Connector/J từ `pom.xml`. Không cần đặt JAR thủ công trong `lib`.

Nếu muốn chạy bằng nút **Run** trên `Main.java`, hãy chờ VS Code hoàn tất Java/Maven project import rồi chọn Run.

## Lưu ý

Project dùng cấu trúc nguồn `src/...` thay vì Maven mặc định `src/main/java`, nên `pom.xml` đã khai báo `src` là source directory.
