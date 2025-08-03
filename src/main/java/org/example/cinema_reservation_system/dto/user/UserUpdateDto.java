package org.example.cinema_reservation_system.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateDto {
    @NotBlank(message = "Tên không được để trống")
    private String tenDangNhap;

//    // Có thể cho phép đổi vai trò nếu là ADMIN
//    private Integer idVaiTro;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String matKhau;
}

