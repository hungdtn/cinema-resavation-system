package org.example.cinema_reservation_system.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerRegisterDto {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String tenDangNhap;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String matKhau;

    @NotBlank(message = "Email không được để trống")
    private String email;

    private String soDienThoai;

    private String tenKhachHang;
}

