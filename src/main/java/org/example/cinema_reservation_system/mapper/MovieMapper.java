package org.example.cinema_reservation_system.mapper;

import org.example.cinema_reservation_system.dto.moviedto.MovieRequestDto;
import org.example.cinema_reservation_system.dto.moviedto.MovieResponseDto;
import org.example.cinema_reservation_system.entity.*;
import org.example.cinema_reservation_system.utils.enums.LoaiHinhAnh;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MovieMapper {

    public Movie toEntity(MovieRequestDto dto) {
        Movie phim = new Movie();
        phim.setTenPhim(dto.getTenPhim());
        phim.setMoTa(dto.getMoTa());
        phim.setThoiLuong(dto.getThoiLuong());
        phim.setNgayPhatHanh(dto.getNgayPhatHanh());
        phim.setTrangThai(dto.getTrangThai());
        phim.setDinhDang(dto.getDinhDang());
        return phim;
    }

    public MovieResponseDto toDto(Movie phim) {
        MovieResponseDto dto = new MovieResponseDto();
        dto.setIdPhim(phim.getIdPhim());
        dto.setTenPhim(phim.getTenPhim());
        dto.setMoTa(phim.getMoTa());
        dto.setThoiLuong(phim.getThoiLuong());
        dto.setNgayPhatHanh(phim.getNgayPhatHanh());
        dto.setDinhDang(phim.getDinhDang());
        dto.setTrangThai(phim.getTrangThai());
        dto.setNgayTao(phim.getNgayTao() != null ? phim.getNgayTao() : null);
        dto.setPosterUrl(getImageUrl(phim, LoaiHinhAnh.POSTER));
        dto.setBannerUrl(getImageUrl(phim, LoaiHinhAnh.BANNER));
        dto.setTrailerUrl(phim.getTrailer() != null ? phim.getTrailer().getUrl() : null);

        dto.setDaoDien(phim.getDaoDienList().stream().map(Director::getTenDaoDien).collect(Collectors.toList()));
        dto.setDienVien(phim.getDienVienList().stream().map(Actor::getTenDienVien).collect(Collectors.toList()));
        dto.setTheLoai(phim.getTheLoaiList().stream().map(Genre::getTenTheLoai).collect(Collectors.toList()));

        return dto;
    }

    private String getImageUrl(Movie phim, LoaiHinhAnh loai) {
        Set<Image> hinhAnhs = phim.getHinhAnhs();
        if (hinhAnhs == null || hinhAnhs.isEmpty()) return null;

        return hinhAnhs.stream()
                .filter(img -> loai.equals(img.getLoai()))
                .findFirst()
                .map(Image::getUrl)
                .orElse(null);
    }

}
