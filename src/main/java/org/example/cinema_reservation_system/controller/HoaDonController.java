package org.example.cinema_reservation_system.controller;

import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.HoaDonDto;
import org.example.cinema_reservation_system.entity.HoaDon;
import org.example.cinema_reservation_system.service.HoaDonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pay")
public class HoaDonController {
    private final HoaDonService hoaDonService;

    @GetMapping
    public ResponseEntity<List<HoaDonDto>> getAllHoaDon() {
        return ResponseEntity.ok(hoaDonService.getAllHoaDon());
    }

    @PostMapping("/payments")
    public ResponseEntity<HoaDon> create(@RequestBody HoaDon hoaDon) {
        HoaDon result = hoaDonService.createHoaDon(hoaDon);
        return ResponseEntity.ok(result);
    }
}
