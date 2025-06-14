package org.example.cinema_reservation_system.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank
    private String tenDangNhap;

    @NotBlank
    private String matKhau;
}