package org.example.cinema_reservation_system.controller.suatchieu;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.suatchieudto.*;
import org.example.cinema_reservation_system.service.suatchieu.SuatChieuService;
import org.example.cinema_reservation_system.utils.Enum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/suat-chieu")
@RequiredArgsConstructor
public class SuatChieuController {
    private final SuatChieuService svc;

    @GetMapping
    public Page<SuatChieuSummaryDTO> all(Pageable p) {
        return svc.findAll(p);
    }

    @GetMapping("/{id}")
    public SuatChieuResponseDTO one(@PathVariable Integer id) {
        return svc.findById(id);
    }

    @PostMapping
    public SuatChieuResponseDTO create(@Valid @RequestBody SuatChieuRequestDTO req) {
        return svc.create(req);
    }

    @PutMapping("/{id}")
    public SuatChieuResponseDTO update(@PathVariable Integer id, @Valid @RequestBody SuatChieuRequestDTO req) {
        return svc.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        svc.delete(id);
    }

    @PutMapping("/status")
    public SuatChieuResponseDTO status(@Valid @RequestBody SuatChieuStatusUpdateDTO dto) {
        return svc.updateStatus(dto);
    }

    @PostMapping("/{id}/activate")
    public void activate(@PathVariable Integer id) { svc.activateShowtime(id); }
    @PostMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Integer id) { svc.deactivateShowtime(id); }

    @GetMapping("/phim/{phimId}")
    public List<SuatChieuSummaryDTO> byPhim(@PathVariable Integer phimId) {
        return svc.findByPhimId(phimId);
    }

    @GetMapping("/phong/{phongId}")
    public List<SuatChieuSummaryDTO> byPhong(@PathVariable Integer phongId) {
        return svc.findByPhongChieuId(phongId);
    }

    @GetMapping("/ngay/{ngay}")
    public List<SuatChieuSummaryDTO> byDate(@PathVariable LocalDate ngay) {
        return svc.findByNgayChieu(ngay);
    }

    @GetMapping("/available")
    public List<SuatChieuSummaryDTO> available() {
        return svc.findAvailableShowtimes();
    }

    @GetMapping("/search")
    public Page<SuatChieuSummaryDTO> search(
            @RequestParam(required=false) String kw,
            @RequestParam(required=false) LocalDate from,
            @RequestParam(required=false) LocalDate to,
            @RequestParam(required=false) Enum.TrangThaiSuatChieu status,
            Pageable p) {
        return svc.searchShowtimes(kw, from, to, status, p);
    }
}