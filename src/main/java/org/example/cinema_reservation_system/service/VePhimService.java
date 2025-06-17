package org.example.cinema_reservation_system.service;

import org.example.cinema_reservation_system.dto.VePhimDto;
import org.example.cinema_reservation_system.entity.VePhim;

import java.util.List;

public interface VePhimService {
    List<VePhimDto> getAllVePhim();
    List<VePhim> getVePhim();
    VePhim createVePhim(VePhim vePhim);
    VePhimDto getVePhimDtoById(Integer id);
}
