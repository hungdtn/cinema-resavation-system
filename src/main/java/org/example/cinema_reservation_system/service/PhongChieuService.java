package org.example.cinema_reservation_system.service;

import org.example.cinema_reservation_system.dto.phongchieudto.PhongChieuRequestDto;
import org.example.cinema_reservation_system.dto.phongchieudto.PhongChieuResponseDto;
import org.example.cinema_reservation_system.utils.Enum;


import java.util.List;

public interface PhongChieuService {
    List<PhongChieuResponseDto> findAllDto();
    PhongChieuResponseDto findByIdDto(Integer id);
    PhongChieuResponseDto savePhongChieu(PhongChieuRequestDto dto);
    PhongChieuResponseDto updatePhongChieu(Integer id, PhongChieuRequestDto dto);
    void changeTrangThai(Integer id, Enum.TrangThaiPhongChieu trangThai);

    List<PhongChieuResponseDto> filterPhongChieu(
            Integer idRap,
            String trangThaiRaw,
            String tenPhongChieu,
            Double dienTichMin,
            Double dienTichMax,
            String sortBy,
            String order
    );
}

