package org.example.cinema_reservation_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.cinematicketdto.CinemaTicketRequestDto;
import org.example.cinema_reservation_system.dto.cinematicketdto.CinemaTicketResponseDto;
import org.example.cinema_reservation_system.service.CinemaTicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class CinemaTicketController {
    private final CinemaTicketService vePhimService;

    @GetMapping("/history")
    public ResponseEntity<List<CinemaTicketResponseDto>> findAll() {
            return ResponseEntity.ok(vePhimService.getAllVePhim());
    }

    @PostMapping("/new")
    public ResponseEntity<CinemaTicketResponseDto> createVePhim(@Valid @RequestBody CinemaTicketRequestDto dto) {
        CinemaTicketResponseDto response = vePhimService.createVePhim(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CinemaTicketResponseDto> getVePhimById(@Valid @PathVariable Integer id) {
        CinemaTicketResponseDto vePhimDto = vePhimService.getVePhimDtoById(id);
        return ResponseEntity.ok(vePhimDto);
    }

}
