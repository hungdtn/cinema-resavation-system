package org.example.cinema_reservation_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.moviedto.TrailerRequestDto;
import org.example.cinema_reservation_system.dto.moviedto.TrailerResponseDto;
import org.example.cinema_reservation_system.service.TrailerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trailer")
@RequiredArgsConstructor

public class TrailerController {
    private final TrailerService trailerService;

    @GetMapping
    public List<TrailerResponseDto> getAll() {
        return trailerService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrailerResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(trailerService.getById(id));
    }

    //{
    //    "url": "https://www.youtube.com/watch?v=7LH-TIcPqks",
    //    "idPhim": 23
    //}
    @PostMapping
    public ResponseEntity<TrailerResponseDto> create(@RequestBody @Valid TrailerRequestDto dto) {
        return ResponseEntity.ok(trailerService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrailerResponseDto> update(@PathVariable Integer id, @RequestBody @Valid TrailerRequestDto dto) {
        return ResponseEntity.ok(trailerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(trailerService.delete(id));
    }
}
