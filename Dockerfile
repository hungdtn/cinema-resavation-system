# Dùng image JDK 21 chính thức từ Eclipse Temurin
FROM eclipse-temurin:21-jdk

# Đặt thư mục làm việc trong container
WORKDIR /app

# Sao chép toàn bộ mã nguồn vào container
COPY . .

# Cấp quyền thực thi cho script Maven wrapper
RUN chmod +x ./mvnw

# Build ứng dụng, bỏ qua test để build nhanh hơn
RUN ./mvnw clean package -DskipTests

# Chạy file JAR (nhớ kiểm tra đúng tên file trong thư mục target)
CMD ["java", "-jar", "target/Cinema_Reservation_System-0.0.1-SNAPSHOT.jar"]
