package org.example.cinema_reservation_system.service;

import org.example.cinema_reservation_system.dto.phimdto.TrailerRequestDto;
import org.example.cinema_reservation_system.dto.phimdto.TrailerResponseDto;

import java.util.List;
import java.util.Map;

public interface TrailerService {
        List<TrailerResponseDto> getAll();
        TrailerResponseDto getById(Integer id);
        TrailerResponseDto create(TrailerRequestDto dto);
        TrailerResponseDto update(Integer id, TrailerRequestDto dto);
        Map<String, Object> delete(Integer id);

}
