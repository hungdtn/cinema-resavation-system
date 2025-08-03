package org.example.cinema_reservation_system.controller.room;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.roomdto.RoomRequestDto;
import org.example.cinema_reservation_system.dto.roomdto.RoomResponseDto;
import org.example.cinema_reservation_system.service.RoomService;
import org.example.cinema_reservation_system.utils.enums.TrangThaiPhongChieu;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/phong_chieu")
@RequiredArgsConstructor

public class RoomController {
    private final RoomService phongChieuService;

    // Lấy tất cả phòng chiếu
    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> getAll() {
        List<RoomResponseDto> list = phongChieuService.findAllDto();
        return ResponseEntity.ok(list);
    }

    // Lấy phòng chiếu theo ID
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDto> getById(@PathVariable Integer id) {
        RoomResponseDto dto = phongChieuService.findByIdDto(id);
        return ResponseEntity.ok(dto);
    }

    // Thêm mới phòng chiếu
    @PostMapping
    public ResponseEntity<RoomResponseDto> create(@Valid @RequestBody RoomRequestDto dto) {
        RoomResponseDto result = phongChieuService.savePhongChieu(dto);
        return ResponseEntity.ok(result);
    }

    // Cập nhật phòng chiếu
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> update(@PathVariable Integer id,
                                                  @Valid @RequestBody RoomRequestDto dto) {
        RoomResponseDto result = phongChieuService.updatePhongChieu(id, dto);
        return ResponseEntity.ok(result);
    }

    // Thay đổi trạng thái phòng chiếu (disable hoặc cập nhật trạng thái bất kỳ)
    // http://localhost:8080/api/phong_chieu/17/status?trangThai=KHONG_HOAT_DONG
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable Integer id,
                                               @RequestParam("trangThai") String trangThaiRaw) {
        try {
            TrangThaiPhongChieu trangThai = TrangThaiPhongChieu.valueOf(trangThaiRaw.toUpperCase());
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
        List<RoomResponseDto> result = phongChieuService
                .filterPhongChieu(idRap, trangThai, tenPhongChieu, dienTichMin, dienTichMax, sortBy, order);

        if (result.isEmpty()) {
            return ResponseEntity.status(404).body("Không tìm thấy phòng chiếu phù hợp.");
        }
        return ResponseEntity.ok(result);

    }
}
