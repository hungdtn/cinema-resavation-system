package org.example.cinema_reservation_system.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.auth.ChangePasswordDto;
import org.example.cinema_reservation_system.dto.user.UserResponseDto;
import org.example.cinema_reservation_system.dto.user.UserUpdateDto;
import org.example.cinema_reservation_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ /api/user/profile
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getCurrentUserProfile() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    // ✅ /api/user/update
    @PutMapping("/update")
    public ResponseEntity<UserResponseDto> updateProfile(@Valid @RequestBody UserUpdateDto dto) {
        return ResponseEntity.ok(userService.updateCurrentUser(dto));
    }

    // ✅ /api/user/change-password
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        userService.changePassword(dto);
        return ResponseEntity.ok("Đổi mật khẩu thành công!");
    }
}

