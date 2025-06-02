package org.example.cinema_reservation_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank
    private String tenDangNhap;

    @NotBlank
    private String matKhau;
}