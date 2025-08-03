package org.example.cinema_reservation_system.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.auth.LoginDto;
import org.example.cinema_reservation_system.dto.user.CustomerRegisterDto;
import org.example.cinema_reservation_system.dto.user.EmployeeRegisterDto;
import org.example.cinema_reservation_system.entity.UserAccount;
import org.example.cinema_reservation_system.repository.useraccount.UserAccountRepository;
import org.example.cinema_reservation_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody @Valid CustomerRegisterDto dto) {
        userService.registerCustomer(dto);
        return ResponseEntity.ok("Đăng ký khách hàng thành công");
    }

    @PostMapping("/register-employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerEmployee(@Valid @RequestBody EmployeeRegisterDto employeeRegisterDto) {
        return ResponseEntity.ok(userService.registerEmployee(employeeRegisterDto));
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginDto dto) {
//        UserAccount user = userService.login(dto);
//        return ResponseEntity.ok("Đăng nhập thành công với vai trò: " + user.getVaiTro().getTenVaiTro());
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        UserAccount user = userService.login(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Đăng nhập thành công");
        response.put("role", user.getVaiTro().getTenVaiTro());

        return ResponseEntity.ok(response);
    }


//    @PostMapping("/login")
//    public String login(@RequestParam String username) {
//        // Không cần password cho demo
//        return userService.generateToken(username);
//    }

}