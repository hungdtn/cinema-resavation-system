package org.example.cinema_reservation_system.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeRegisterDto {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String tenDangNhap;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String matKhau;

    @NotNull(message = "Id vai trò không được để trống")
    private Integer idVaiTro;

    @NotNull(message = "Id nhân viên không được để trống")
    private Integer idNhanVien;
}
