package org.example.cinema_reservation_system.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tenDangNhap;
    private String vaiTro;
}
