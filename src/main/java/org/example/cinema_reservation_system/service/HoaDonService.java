package org.example.cinema_reservation_system.service;

import org.example.cinema_reservation_system.dto.HoaDonDto;
import org.example.cinema_reservation_system.entity.HoaDon;

import java.util.List;

public interface HoaDonService {
    List<HoaDonDto> getAllHoaDon();
    HoaDon createHoaDon(HoaDon hoaDon);
}
