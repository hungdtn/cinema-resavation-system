package org.example.cinema_reservation_system.controller.theater;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.theaterdto.TheaterRequestDto;
import org.example.cinema_reservation_system.dto.theaterdto.TheaterResponseDto;
import org.example.cinema_reservation_system.service.CinemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rap_chieu")
@RequiredArgsConstructor
public class TheaterController {

    private final CinemaService service;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TheaterRequestDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @Valid @RequestBody TheaterRequestDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok("Đã xoá thành công rạp chiếu có ID: "+id);
    }

    // Tìm kiếm
 //   GET http://localhost:8080/api/rap_chieu/search?keyword=CGV&trangThai=HOAT_DONG&diaChi=Hà Nội
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) String diaChi
    ) {
        List<TheaterResponseDto> result = service.search(keyword, trangThai, diaChi);

        return result.isEmpty()
                ? ResponseEntity.status(204).body("Không tìm thấy rạp chiếu phù hợp.")
                : ResponseEntity.ok(result);
    }


}
