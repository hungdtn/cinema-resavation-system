package org.example.cinema_reservation_system.dto.user;

import lombok.Data;

@Data
public class UserResponseDto {
    private Integer idUserAccount;
    private String tenDangNhap;
    private String vaiTro;
    private String trangThai;
}