package org.example.cinema_reservation_system.service;

import org.example.cinema_reservation_system.dto.theaterdto.TheaterRequestDto;
import org.example.cinema_reservation_system.dto.theaterdto.TheaterResponseDto;

import java.util.List;

public interface CinemaService {
    List<TheaterResponseDto> findAll();

    TheaterResponseDto findById(Integer id);

    TheaterResponseDto save(TheaterRequestDto dto);

    TheaterResponseDto update(Integer id, TheaterRequestDto dto);

    void delete(Integer id);

    List<TheaterResponseDto> search(String keyword, String trangThaiRaw, String diaChi);
}
