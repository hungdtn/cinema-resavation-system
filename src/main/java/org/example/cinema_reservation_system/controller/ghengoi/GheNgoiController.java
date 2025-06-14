package org.example.cinema_reservation_system.controller.ghengoi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.ghengoidto.DatGheRequestDTO;
import org.example.cinema_reservation_system.dto.ghengoidto.GheNgoiDTO;
import org.example.cinema_reservation_system.dto.ghengoidto.SoDoGheDTO;
import org.example.cinema_reservation_system.service.ghengoi.GheNgoiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ghengoi")
@RequiredArgsConstructor
public class GheNgoiController {

    private final GheNgoiService gheNgoiService;

    @PostMapping
    public ResponseEntity<GheNgoiDTO> createSeat(@Valid @RequestBody GheNgoiDTO gheNgoiDTO) {
        GheNgoiDTO responseDTO = gheNgoiService.create(gheNgoiDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GheNgoiDTO> updateSeat(@PathVariable Integer id, @Valid @RequestBody GheNgoiDTO gheNgoiDTO) {
        GheNgoiDTO responseDTO = gheNgoiService.update(id, gheNgoiDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Integer id) {
        gheNgoiService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GheNgoiDTO> getSeatById(@PathVariable Integer id) {
        GheNgoiDTO responseDTO = gheNgoiService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<GheNgoiDTO>> getAllSeats(Pageable pageable) {
        Page<GheNgoiDTO> seats = gheNgoiService.findAll(pageable);
        return ResponseEntity.ok(seats);
    }

    @GetMapping("/phongchieu/{phongChieuId}")
    public ResponseEntity<List<GheNgoiDTO>> getSeatsByPhongChieu(@PathVariable Integer phongChieuId) {
        List<GheNgoiDTO> seats = gheNgoiService.findByPhongChieu(phongChieuId);
        return ResponseEntity.ok(seats);
    }

    @GetMapping("/phongchieu/{phongChieuId}/sodoghe")
    public ResponseEntity<SoDoGheDTO> getSeatMap(@PathVariable Integer phongChieuId) {
        SoDoGheDTO seatMap = gheNgoiService.getSoDoGhe(phongChieuId);
        return ResponseEntity.ok(seatMap);
    }

    @GetMapping("/phongchieu/{phongChieuId}/available")
    public ResponseEntity<List<GheNgoiDTO>> getAvailableSeats(@PathVariable Integer phongChieuId) {
        List<GheNgoiDTO> availableSeats = gheNgoiService.findGheTrong(phongChieuId);
        return ResponseEntity.ok(availableSeats);
    }

    @PostMapping("/datghe")
    public ResponseEntity<List<GheNgoiDTO>> bookSeats(@Valid @RequestBody DatGheRequestDTO request) {
        List<GheNgoiDTO> bookedSeats = gheNgoiService.datGhe(request);
        return ResponseEntity.ok(bookedSeats);
    }

    @PostMapping("/xacnhandatghe")
    public ResponseEntity<Void> confirmBooking(@RequestBody List<Integer> danhSachIdGheNgoi) {
        gheNgoiService.xacNhanDatGhe(danhSachIdGheNgoi);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/huydatghe")
    public ResponseEntity<Void> cancelBooking(@RequestBody List<Integer> danhSachIdGheNgoi) {
        gheNgoiService.huyDatGhe(danhSachIdGheNgoi);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk/phongchieu/{phongChieuId}")
    public ResponseEntity<List<GheNgoiDTO>> createBulkSeats(
            @PathVariable Integer phongChieuId,
            @RequestParam String loaiGhe,
            @RequestParam String hangBatDau,
            @RequestParam int soHang,
            @RequestParam int soGheMoiHang) {
        List<GheNgoiDTO> createdSeats = gheNgoiService.createBulkGhe(phongChieuId, loaiGhe, hangBatDau, soHang, soGheMoiHang);
        return new ResponseEntity<>(createdSeats, HttpStatus.CREATED);
    }

    @DeleteMapping("/phongchieu/{phongChieuId}")
    public ResponseEntity<Void> deleteSeatsByPhongChieu(@PathVariable Integer phongChieuId) {
        gheNgoiService.deleteByPhongChieu(phongChieuId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/thongke/phongchieu/{phongChieuId}")
    public ResponseEntity<Map<String, Long>> getSeatStatistics(@PathVariable Integer phongChieuId) {
        Map<String, Long> statistics = gheNgoiService.getThongKeGhe(phongChieuId);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/{id}/available")
    public ResponseEntity<Boolean> isSeatAvailable(@PathVariable Integer id) {
        boolean available = gheNgoiService.isGheAvailable(id);
        return ResponseEntity.ok(available);
    }
}