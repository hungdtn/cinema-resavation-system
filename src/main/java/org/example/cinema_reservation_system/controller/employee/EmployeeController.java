package org.example.cinema_reservation_system.controller.employee;

import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.repository.employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/api/auth/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        boolean exists = employeeRepository.existsByEmail(email);
        return ResponseEntity.ok(Collections.singletonMap("exists", exists));
    }

    @PostMapping("/api/auth/check-phone")
    public ResponseEntity<Map<String, Boolean>> checkPhone(@RequestBody Map<String, String> body) {
        String phone = body.get("phone");
        boolean exists = employeeRepository.existsBySoDienThoai(phone);
        return ResponseEntity.ok(Collections.singletonMap("exists", exists));
    }
}
