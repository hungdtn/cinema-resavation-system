package org.example.cinema_reservation_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateDto {
    @NotBlank
    private String tenDangNhap;

    // Có thể cho phép đổi vai trò nếu là ADMIN
    private Integer idVaiTro;
}

