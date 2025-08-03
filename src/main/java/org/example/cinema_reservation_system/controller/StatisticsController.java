package org.example.cinema_reservation_system.controller;

import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.statistics.StatisticsByMovie;
import org.example.cinema_reservation_system.dto.statistics.StatisticsByTheater;
import org.example.cinema_reservation_system.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService thongKeService;

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getDoanhThu() {
        Double tongDoanhThu = thongKeService.getTongDoanhThu();
        Map<String, Object> response = new HashMap<>();
        response.put("tongDoanhThu", tongDoanhThu);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/statistics/month")
    public ResponseEntity<BigDecimal> getDoanhThu(
            @RequestParam int month,
            @RequestParam int year) {
        BigDecimal doanhThu = thongKeService.tongdoanhThuThangNam(month, year);
        return ResponseEntity.ok(doanhThu);
    }

    @GetMapping("/statistics/theater")
    public ResponseEntity<List<StatisticsByTheater>> getDoanhThuTheoRap() {
        return ResponseEntity.ok(thongKeService.getDoanhThuTheoRap());
    }

    @GetMapping("/statistics/movie")
    public ResponseEntity<List<StatisticsByMovie>> getDoanhThuTheoPhim() {
        return ResponseEntity.ok(thongKeService.getDoanhThuTheoPhim());
    }
}

