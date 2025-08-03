package org.example.cinema_reservation_system.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.auth.ChangePasswordDto;
import org.example.cinema_reservation_system.dto.user.UserResponseDto;
import org.example.cinema_reservation_system.dto.user.UserUpdateDto;
import org.example.cinema_reservation_system.repository.useraccount.UserAccountRepository;
import org.example.cinema_reservation_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    // ✅ /api/user/all
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

//    // ✅ /api/user/update
//    @PutMapping("/update")
//    public ResponseEntity<UserResponseDto> updateProfile(@Valid @RequestBody UserUpdateDto dto) {
//        return ResponseEntity.ok(userService.updateCurrentUser(dto));
//    }

//    @PutMapping("/update-current-user")
//    public ResponseEntity<String> updateCurrentUser(@Valid @RequestBody UserUpdateDto dto) {
//        userService.updateCurrentUser(dto);
//        return ResponseEntity.ok("Cập nhật thông tin thành công");
//    }

    @PutMapping("/update-current-user")
    public ResponseEntity<UserResponseDto> updateCurrentUser(@Valid @RequestBody UserUpdateDto dto) {
        UserResponseDto response = userService.updateCurrentUser(dto);
        return ResponseEntity.ok(response);
    }

    // ✅ /api/user/change-password
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        userService.changePassword(dto);
        return ResponseEntity.ok("Đổi mật khẩu thành công!");
    }

    // ✅ /api/user/{id}
    @DeleteMapping("/{tenDangNhap}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String tenDangNhap) {
        userService.deleteByTenDangNhap(tenDangNhap);
        return ResponseEntity.ok("Xoá tài khoản thành công!");
    }

    @PostMapping("/api/auth/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        boolean exists = userAccountRepository.existsByTenDangNhap(username);
        return ResponseEntity.ok(Collections.singletonMap("exists", exists));
    }

}

