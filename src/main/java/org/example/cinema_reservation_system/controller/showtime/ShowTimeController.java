package org.example.cinema_reservation_system.controller.showtime;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.showtimedto.*;
import org.example.cinema_reservation_system.service.showtime.ShowTimeService;
import org.example.cinema_reservation_system.utils.enums.TrangThaiSuatChieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/suat-chieu")
@RequiredArgsConstructor
public class ShowTimeController {
    private final ShowTimeService svc;

    @GetMapping
    public Page<ShowTimeSummaryDTO> all(Pageable p) {
        return svc.findAll(p);
    }

    @GetMapping("/{id}")
    public ShowTimeResponseDTO one(@PathVariable Integer id) {
        return svc.findById(id);
    }

    @PostMapping
    public ShowTimeResponseDTO create(@Valid @RequestBody ShowTimeRequestDTO req) {
        return svc.create(req);
    }

    @PutMapping("/{id}")
    public ShowTimeResponseDTO update(@PathVariable Integer id, @Valid @RequestBody ShowTimeRequestDTO req) {
        return svc.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        svc.delete(id);
    }

    @PutMapping("/status")
    public ShowTimeResponseDTO status(@Valid @RequestBody ShowTimeStatusUpdateDTO dto) {
        return svc.updateStatus(dto);
    }

    @PostMapping("/{id}/activate")
    public void activate(@PathVariable Integer id) { svc.activateShowtime(id); }
    @PostMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Integer id) { svc.deactivateShowtime(id); }

    @GetMapping("/phim/{phimId}")
    public List<ShowTimeSummaryDTO> byPhim(@PathVariable Integer phimId) {
        return svc.findByPhimId(phimId);
    }

    @GetMapping("/phong/{phongId}")
    public List<ShowTimeSummaryDTO> byPhong(@PathVariable Integer phongId) {
        return svc.findByPhongChieuId(phongId);
    }

    @GetMapping("/ngay/{ngay}")
    public List<ShowTimeSummaryDTO> byDate(@PathVariable LocalDate ngay) {
        return svc.findByNgayChieu(ngay);
    }

    @GetMapping("/available")
    public List<ShowTimeSummaryDTO> available() {
        return svc.findAvailableShowtimes();
    }

    @GetMapping("/search")
    public Page<ShowTimeSummaryDTO> search(
            @RequestParam(required=false) String kw,
            @RequestParam(required=false) LocalDate from,
            @RequestParam(required=false) LocalDate to,
            @RequestParam(required=false) TrangThaiSuatChieu status,
            Pageable p) {
        return svc.searchShowtimes(kw, from, to, status, p);
    }
}