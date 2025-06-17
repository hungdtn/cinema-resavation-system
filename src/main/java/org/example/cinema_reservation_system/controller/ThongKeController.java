package org.example.cinema_reservation_system.controller;

import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.service.ThongKeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ThongKeController {
    private final ThongKeService thongKeService;

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getDoanhThu() {
        Double tongDoanhThu = thongKeService.getTongDoanhThu();
        Map<String, Object> response = new HashMap<>();
        response.put("tongDoanhThu", tongDoanhThu);
        return ResponseEntity.ok(response);
    }
}

