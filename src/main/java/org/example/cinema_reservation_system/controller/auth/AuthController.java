package org.example.cinema_reservation_system.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.auth.LoginDto;
import org.example.cinema_reservation_system.dto.user.UserRegisterDto;
import org.example.cinema_reservation_system.entity.UserAccount;
import org.example.cinema_reservation_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDto dto) {
        return ResponseEntity.ok(userService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto dto) {
        UserAccount user = userService.login(dto);
        return ResponseEntity.ok("Đăng nhập thành công với vai trò: " + user.getVaiTro().getTenVaiTro());
    }
}