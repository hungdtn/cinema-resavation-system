package org.example.cinema_reservation_system.service;

import org.example.cinema_reservation_system.dto.roomdto.RoomRequestDto;
import org.example.cinema_reservation_system.dto.roomdto.RoomResponseDto;
import org.example.cinema_reservation_system.utils.enums.TrangThaiPhongChieu;


import java.util.List;

public interface RoomService {
    List<RoomResponseDto> findAllDto();
    RoomResponseDto findByIdDto(Integer id);
    RoomResponseDto savePhongChieu(RoomRequestDto dto);
    RoomResponseDto updatePhongChieu(Integer id, RoomRequestDto dto);
    void changeTrangThai(Integer id, TrangThaiPhongChieu trangThai);

    List<RoomResponseDto> filterPhongChieu(
            Integer idRap,
            String trangThaiRaw,
            String tenPhongChieu,
            Double dienTichMin,
            Double dienTichMax,
            String sortBy,
            String order
    );
}

