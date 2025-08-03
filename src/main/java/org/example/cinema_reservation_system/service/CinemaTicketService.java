package org.example.cinema_reservation_system.service;

import org.example.cinema_reservation_system.dto.cinematicketdto.CinemaTicketResponseDto;
import org.example.cinema_reservation_system.dto.cinematicketdto.CinemaTicketRequestDto;

import java.util.List;

public interface CinemaTicketService {
    List<CinemaTicketResponseDto> getAllVePhim();
    CinemaTicketResponseDto createVePhim(CinemaTicketRequestDto vePhimRequestDto);
    CinemaTicketResponseDto getVePhimDtoById(Integer id);
}
