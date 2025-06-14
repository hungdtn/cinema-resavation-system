package org.example.cinema_reservation_system.controller;

import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.VePhimDto;
import org.example.cinema_reservation_system.dto.request.VePhimRequest;
import org.example.cinema_reservation_system.entity.VePhim;
import org.example.cinema_reservation_system.repository.*;
import org.example.cinema_reservation_system.service.VePhimService;
import org.example.cinema_reservation_system.utils.enums.TrangThaiVePhim;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class VePhimController {
    private final VePhimService vePhimService;
    private final KhachHangRepo khachHangRepo;
    private final PhimRepo phimRepo;
    private final NhanVienRepo nhanVienRepo;
    private final HoaDonRepo hoaDonRepo;
    private final SuatChieuRepo suatChieuRepo;
    private final GheNgoiRepo gheNgoiRepo;

    @GetMapping("/history")
    public ResponseEntity<List<VePhimDto>> findAll() {
            return ResponseEntity.ok(vePhimService.getAllVePhim());
    }

    @GetMapping("/only")
    public ResponseEntity<List<VePhim>> getVePhim() {
        return ResponseEntity.ok(vePhimService.getVePhim());
    }

    @PostMapping("/news")
    public ResponseEntity<VePhim> createVePhim(@RequestBody VePhimRequest request) {
        VePhim vePhim = new VePhim();

        vePhim.setGiaVe(request.getGiaVe());
        vePhim.setNgayDat(request.getNgayDat());

        vePhim.setSuatChieu(suatChieuRepo.findById(request.getIdSuatChieu()).orElse(null));
        vePhim.setKhachHang(khachHangRepo.findById(request.getIdKhachHang()).orElse(null));
        vePhim.setGheNgoi(gheNgoiRepo.findById(request.getIdGheNgoi()).orElse(null));
        vePhim.setHoaDon(hoaDonRepo.findById(request.getIdHoaDon()).orElse(null));
        vePhim.setPhim(phimRepo.findById(request.getIdPhim()).orElse(null));
        vePhim.setNhanVien(nhanVienRepo.findById(request.getIdNhanVien()).orElse(null));

        // Convert String -> Enum safely
        try {
            vePhim.setTrangThai(TrangThaiVePhim.valueOf(request.getTrangThai().toLowerCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Giá trị trạng thái không hợp lệ: " + request.getTrangThai());
        }

        VePhim saved = vePhimService.createVePhim(vePhim);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VePhimDto> getVePhimById(@PathVariable Integer id) {
        VePhimDto vePhimDto = vePhimService.getVePhimDtoById(id);
        return ResponseEntity.ok(vePhimDto);
    }
}
