package org.example.cinema_reservation_system.controller;

import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.invoicedto.InvoiceDto;
import org.example.cinema_reservation_system.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pay")
public class InvoiceController {
    private final InvoiceService hoaDonService;

    @GetMapping
    public ResponseEntity<List<InvoiceDto>> getAllHoaDon() {
        return ResponseEntity.ok(hoaDonService.getAllHoaDon());
    }

//    @PostMapping("/payments")
//    public ResponseEntity<HoaDon> create(@RequestBody HoaDon hoaDon) {
//        HoaDon result = hoaDonService.createHoaDon(hoaDon);
//        return ResponseEntity.ok(result);
//    }

}
