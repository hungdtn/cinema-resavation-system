package org.example.cinema_reservation_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.phongchieudto.PhongChieuRequestDto;
import org.example.cinema_reservation_system.dto.phongchieudto.PhongChieuResponseDto;
import org.example.cinema_reservation_system.service.PhongChieuService;
import org.example.cinema_reservation_system.utils.Enum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/phong_chieu")
@RequiredArgsConstructor

public class PhongChieuController {
    private final PhongChieuService phongChieuService;

    // Lấy tất cả phòng chiếu
    @GetMapping
    public ResponseEntity<List<PhongChieuResponseDto>> getAll() {
        List<PhongChieuResponseDto> list = phongChieuService.findAllDto();
        return ResponseEntity.ok(list);
    }

    // Lấy phòng chiếu theo ID
    @GetMapping("/{id}")
    public ResponseEntity<PhongChieuResponseDto> getById(@PathVariable Integer id) {
        PhongChieuResponseDto dto = phongChieuService.findByIdDto(id);
        return ResponseEntity.ok(dto);
    }

    // Thêm mới phòng chiếu
    @PostMapping
    public ResponseEntity<PhongChieuResponseDto> create(@Valid @RequestBody PhongChieuRequestDto dto) {
        PhongChieuResponseDto result = phongChieuService.savePhongChieu(dto);
        return ResponseEntity.ok(result);
    }

    // Cập nhật phòng chiếu
    @PutMapping("/{id}")
    public ResponseEntity<PhongChieuResponseDto> update(@PathVariable Integer id,
                                                        @Valid @RequestBody PhongChieuRequestDto dto) {
        PhongChieuResponseDto result = phongChieuService.updatePhongChieu(id, dto);
        return ResponseEntity.ok(result);
    }

    // Thay đổi trạng thái phòng chiếu (disable hoặc cập nhật trạng thái bất kỳ)
    // http://localhost:8080/api/phong_chieu/17/status?trangThai=KHONG_HOAT_DONG
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable Integer id,
                                               @RequestParam("trangThai") String trangThaiRaw) {
        try {
            Enum.TrangThaiPhongChieu trangThai = Enum.TrangThaiPhongChieu.valueOf(trangThaiRaw.toUpperCase());
            phongChieuService.changeTrangThai(id, trangThai);
            return ResponseEntity.ok("Đã cập nhật trạng thái phòng chiếu thành công.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Trạng thái không hợp lệ. Vui lòng nhập: HOAT_DONG, BAO_TRI, KHONG_HOAT_DONG.");
        }
    }

    // Tìm kiếm / lọc
    //GET /api/phong_chieu/filter?dienTichMin=100&dienTichMax=150
    //GET /api/phong_chieu/filter?idRap=1&sortBy=ten&order=desc
    //GET /api/phong_chieu/filter?trangThai=BAO_TRI
    //GET /api/phong_chieu/filter?tenPhongChieu=phòng&sortBy=dienTich
    @GetMapping("/filter")
    public ResponseEntity<?> filter(
            @RequestParam(required = false) Integer idRap,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) String tenPhongChieu,
            @RequestParam(required = false) Double dienTichMin,
            @RequestParam(required = false) Double dienTichMax,
            @RequestParam(required = false) String sortBy,   // "ten", "dienTich", "trangThai"
            @RequestParam(defaultValue = "asc") String order // "asc", "desc"
    ) {
        List<PhongChieuResponseDto> result = phongChieuService
                .filterPhongChieu(idRap, trangThai, tenPhongChieu, dienTichMin, dienTichMax, sortBy, order);

        if (result.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy phòng chiếu phù hợp.");
        }
        return ResponseEntity.ok(result);

    }
}
