package org.example.cinema_reservation_system.mapper;

import org.example.cinema_reservation_system.dto.phimdto.PhimRequestDto;
import org.example.cinema_reservation_system.dto.phimdto.PhimResponseDto;
import org.example.cinema_reservation_system.entity.*;
import org.example.cinema_reservation_system.utils.LoaiHinhAnh;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PhimMapper {

    public Phim toEntity(PhimRequestDto dto) {
        Phim phim = new Phim();
        phim.setTenPhim(dto.getTenPhim());
        phim.setMoTa(dto.getMoTa());
        phim.setThoiLuong(dto.getThoiLuong());
        phim.setNgayPhatHanh(dto.getNgayPhatHanh());
        phim.setTrangThai(dto.getTrangThai());
        phim.setDinhDang(dto.getDinhDang());
        return phim;
    }

    public PhimResponseDto toDto(Phim phim) {
        PhimResponseDto dto = new PhimResponseDto();
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

        dto.setDaoDien(phim.getDaoDienList().stream().map(DaoDien::getTenDaoDien).collect(Collectors.toList()));
        dto.setDienVien(phim.getDienVienList().stream().map(DienVien::getTenDienVien).collect(Collectors.toList()));
        dto.setTheLoai(phim.getTheLoaiList().stream().map(TheLoai::getTenTheLoai).collect(Collectors.toList()));

        return dto;
    }

    private String getImageUrl(Phim phim, LoaiHinhAnh loai) {
        Set<HinhAnh> hinhAnhs = phim.getHinhAnhs();
        if (hinhAnhs == null || hinhAnhs.isEmpty()) return null;

        return new ArrayList<>(hinhAnhs).stream()
                .filter(img -> loai.equals(img.getLoaiHinhAnh()))
                .findFirst()
                .map(HinhAnh::getUrl)
                .orElse(null);
    }
}
