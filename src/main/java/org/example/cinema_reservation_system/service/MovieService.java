package org.example.cinema_reservation_system.service;

import org.example.cinema_reservation_system.dto.moviedto.MovieRequestDto;
import org.example.cinema_reservation_system.dto.moviedto.MovieResponseDto;
import org.example.cinema_reservation_system.utils.enums.TrangThaiPhim;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface MovieService {
    List<MovieResponseDto> getAll();
    MovieResponseDto getById(Integer id);
    MovieResponseDto create(MovieRequestDto dto, MultipartFile poster, MultipartFile banner);
    MovieResponseDto update(Integer id, MovieRequestDto dto, MultipartFile poster, MultipartFile banner);
    Map<String, Object> delete(Integer id);
    List<MovieResponseDto> filter(String theLoai, TrangThaiPhim trangThai);
}
