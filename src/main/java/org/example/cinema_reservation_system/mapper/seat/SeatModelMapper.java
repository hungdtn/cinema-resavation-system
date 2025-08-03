package org.example.cinema_reservation_system.mapper.seat;

import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.seatdto.SeatDTO;
import org.example.cinema_reservation_system.dto.seatdto.SeatingChartDTO;
import org.example.cinema_reservation_system.entity.Seat;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatModelMapper {
    private final ModelMapper mapper;

    public SeatDTO toDTO(Seat e) {
        SeatDTO dto = mapper.map(e, SeatDTO.class);
        dto.setIdPhongChieu(e.getPhongChieu().getIdPhongChieu());
        return dto;
    }

    public Seat toEntity(SeatDTO dto) {
        Seat e = mapper.map(dto, Seat.class);
        e.setPhongChieu(null);
        e.setIdGheNgoi(null);
        return e;
    }

    public SeatingChartDTO.GheInSoDoDTO toSoDoGhe(Seat e) {
        return mapper.map(e, SeatingChartDTO.GheInSoDoDTO.class);
    }
}
