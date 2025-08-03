package org.example.cinema_reservation_system.controller.seat;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.seatdto.BookSeatRequestDTO;
import org.example.cinema_reservation_system.dto.seatdto.SeatDTO;
import org.example.cinema_reservation_system.dto.seatdto.SeatingChartDTO;
import org.example.cinema_reservation_system.service.seat.SeatService;
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
public class SeatController {

    private final SeatService gheNgoiService;

    @PostMapping
    public ResponseEntity<SeatDTO> createSeat(@Valid @RequestBody SeatDTO gheNgoiDTO) {
        SeatDTO responseDTO = gheNgoiService.create(gheNgoiDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatDTO> updateSeat(@PathVariable Integer id, @Valid @RequestBody SeatDTO gheNgoiDTO) {
        SeatDTO responseDTO = gheNgoiService.update(id, gheNgoiDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Integer id) {
        gheNgoiService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatDTO> getSeatById(@PathVariable Integer id) {
        SeatDTO responseDTO = gheNgoiService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<SeatDTO>> getAllSeats(Pageable pageable) {
        Page<SeatDTO> seats = gheNgoiService.findAll(pageable);
        return ResponseEntity.ok(seats);
    }

    @GetMapping("/phongchieu/{phongChieuId}")
    public ResponseEntity<List<SeatDTO>> getSeatsByPhongChieu(@PathVariable Integer phongChieuId) {
        List<SeatDTO> seats = gheNgoiService.findByPhongChieu(phongChieuId);
        return ResponseEntity.ok(seats);
    }

    @GetMapping("/phongchieu/{phongChieuId}/sodoghe")
    public ResponseEntity<SeatingChartDTO> getSeatMap(@PathVariable Integer phongChieuId) {
        SeatingChartDTO seatMap = gheNgoiService.getSoDoGhe(phongChieuId);
        return ResponseEntity.ok(seatMap);
    }

    @GetMapping("/phongchieu/{phongChieuId}/available")
    public ResponseEntity<List<SeatDTO>> getAvailableSeats(@PathVariable Integer phongChieuId) {
        List<SeatDTO> availableSeats = gheNgoiService.findGheTrong(phongChieuId);
        return ResponseEntity.ok(availableSeats);
    }

    @PostMapping("/datghe")
    public ResponseEntity<List<SeatDTO>> bookSeats(@Valid @RequestBody BookSeatRequestDTO request) {
        List<SeatDTO> bookedSeats = gheNgoiService.datGhe(request);
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
    public ResponseEntity<List<SeatDTO>> createBulkSeats(
            @PathVariable Integer phongChieuId,
            @RequestParam String loaiGhe,
            @RequestParam String hangBatDau,
            @RequestParam int soHang,
            @RequestParam int soGheMoiHang) {
        List<SeatDTO> createdSeats = gheNgoiService.createBulkGhe(phongChieuId, loaiGhe, hangBatDau, soHang, soGheMoiHang);
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