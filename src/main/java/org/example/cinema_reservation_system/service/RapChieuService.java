package org.example.cinema_reservation_system.service;

import org.example.cinema_reservation_system.dto.rapchieudto.RapChieuRequestDto;
import org.example.cinema_reservation_system.dto.rapchieudto.RapChieuResponseDto;

import java.util.List;

public interface RapChieuService {
     List<RapChieuResponseDto> findAll();
    RapChieuResponseDto findById(Integer id);
    RapChieuResponseDto save(RapChieuRequestDto dto);
    RapChieuResponseDto update(Integer id, RapChieuRequestDto dto);
    void delete(Integer id);
    List<RapChieuResponseDto> search(String keyword, String trangThaiRaw, String diaChi);
}
