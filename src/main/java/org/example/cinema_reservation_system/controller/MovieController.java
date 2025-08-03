package org.example.cinema_reservation_system.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.moviedto.MovieRequestDto;
import org.example.cinema_reservation_system.dto.moviedto.MovieResponseDto;
import org.example.cinema_reservation_system.service.MovieService;
import org.example.cinema_reservation_system.utils.enums.TrangThaiPhim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/phim")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MovieController {

    private final MovieService phimService;

    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping
    public ResponseEntity<List<MovieResponseDto>> getAll() {
        return ResponseEntity.ok(phimService.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(phimService.getById(id));
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart("phim") String phimJson,
                                    @RequestPart("poster") MultipartFile poster,
                                    @RequestPart("banner") MultipartFile banner) {
        try {
            System.out.println("DEBUG phimJson: " + phimJson);
            MovieRequestDto dto = objectMapper.readValue(phimJson, MovieRequestDto.class);
            System.out.println("DEBUG dto: " + dto);
            Object result = phimService.create(dto, poster, banner);
            System.out.println("DEBUG phimService.create result: " + result);
            return ResponseEntity.ok(result);
        } catch (InvalidFormatException e) {
            String field = e.getPath().isEmpty() ? "Không xác định" : e.getPath().get(0).getFieldName();
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Trường '" + field + "' có giá trị không hợp lệ. Vui lòng chọn đúng enum: NGUNG_CHIEU, DANG_CHIEU, SAP_CHIEU."
            ));
        } catch (Exception e) {
            e.printStackTrace();  // Thêm để in stacktrace lỗi
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Lỗi hệ thống: " + e.getMessage()
            ));
        }
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestPart("phim") String phimJson,
                                    @RequestPart(value = "poster", required = false) MultipartFile poster,
                                    @RequestPart(value = "banner", required = false) MultipartFile banner) {
        try {
            MovieRequestDto dto = objectMapper.readValue(phimJson, MovieRequestDto.class);
            return ResponseEntity.ok(phimService.update(id, dto, poster, banner));
        } catch (InvalidFormatException e) {
            String field = e.getPath().isEmpty() ? "Không xác định" : e.getPath().get(0).getFieldName();
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Trường '" + field + "' có giá trị không hợp lệ. Vui lòng chọn đúng enum: NGUNG_CHIEU, DANG_CHIEU, SAP_CHIEU."
            ));
        } catch (JsonParseException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Lỗi định dạng JSON. Vui lòng kiểm tra lại cấu trúc."
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Lỗi hệ thống: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(phimService.delete(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<MovieResponseDto>> filter(@RequestParam(required = false) String theLoai,
                                                         @RequestParam(required = false) TrangThaiPhim trangThai) {
        return ResponseEntity.ok(phimService.filter(theLoai, trangThai));
    }

}
