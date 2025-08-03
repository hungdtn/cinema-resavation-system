package org.example.cinema_reservation_system.mapper.showtime;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.example.cinema_reservation_system.entity.ShowTime;
import org.example.cinema_reservation_system.dto.showtimedto.*;

@Component
@RequiredArgsConstructor
public class ShowTimeModelMapper {
    private final ModelMapper mapper;

    public ShowTime toEntity(ShowTimeRequestDTO dto) {
        return mapper.map(dto, ShowTime.class);
    }

    public ShowTimeResponseDTO toResponse(ShowTime e) {
        ShowTimeResponseDTO r = mapper.map(e, ShowTimeResponseDTO.class);
        r.setTenPhim(e.getPhim().getTenPhim());
        r.setThoiLuongPhim(e.getPhim().getThoiLuong());
        r.setTenPhongChieu(e.getPhongChieu().getTenPhongChieu());
        return r;
    }

    public ShowTimeSummaryDTO toSummary(ShowTime e) {
        ShowTimeSummaryDTO s = mapper.map(e, ShowTimeSummaryDTO.class);
        s.setTenPhim(e.getPhim().getTenPhim());
        s.setTenPhongChieu(e.getPhongChieu().getTenPhongChieu());
        return s;
    }
}