package org.example.cinema_reservation_system.mapper.suatchieu;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.example.cinema_reservation_system.entity.SuatChieu;
import org.example.cinema_reservation_system.dto.suatchieudto.*;

@Component
@RequiredArgsConstructor
public class SuatChieuModelMapper {
    private final ModelMapper mapper;

    public SuatChieu toEntity(SuatChieuRequestDTO dto) {
        return mapper.map(dto, SuatChieu.class);
    }

    public SuatChieuResponseDTO toResponse(SuatChieu e) {
        SuatChieuResponseDTO r = mapper.map(e, SuatChieuResponseDTO.class);
        r.setTenPhim(e.getPhim().getTenPhim());
        r.setThoiLuongPhim(e.getPhim().getThoiLuong());
        r.setTenPhongChieu(e.getPhongChieu().getTenPhongChieu());
        return r;
    }

    public SuatChieuSummaryDTO toSummary(SuatChieu e) {
        SuatChieuSummaryDTO s = mapper.map(e, SuatChieuSummaryDTO.class);
        s.setTenPhim(e.getPhim().getTenPhim());
        s.setTenPhongChieu(e.getPhongChieu().getTenPhongChieu());
        return s;
    }
}