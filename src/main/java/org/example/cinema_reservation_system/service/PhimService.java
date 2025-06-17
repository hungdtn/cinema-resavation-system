package org.example.cinema_reservation_system.service;

import org.example.cinema_reservation_system.dto.phimdto.PhimRequestDto;
import org.example.cinema_reservation_system.dto.phimdto.PhimResponseDto;
import org.example.cinema_reservation_system.utils.Enum;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@Service
public interface PhimService {
    List<PhimResponseDto> getAll();
    PhimResponseDto getById(Integer id);
    PhimResponseDto create(PhimRequestDto dto, MultipartFile poster, MultipartFile banner);
    PhimResponseDto update(Integer id, PhimRequestDto dto, MultipartFile poster, MultipartFile banner);
    Map<String, Object> delete(Integer id);
    List<PhimResponseDto> filter(String theLoai, Enum.TrangThaiPhim trangThai);
}
