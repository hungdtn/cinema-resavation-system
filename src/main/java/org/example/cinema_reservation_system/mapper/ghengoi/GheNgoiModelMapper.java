package org.example.cinema_reservation_system.mapper.ghengoi;

import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.ghengoidto.GheNgoiDTO;
import org.example.cinema_reservation_system.dto.ghengoidto.SoDoGheDTO;
import org.example.cinema_reservation_system.entity.GheNgoi;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GheNgoiModelMapper {
    private final ModelMapper mapper;

    public GheNgoiDTO toDTO(GheNgoi e) {
        GheNgoiDTO dto = mapper.map(e, GheNgoiDTO.class);
        dto.setIdPhongChieu(e.getPhongChieu().getId());
        return dto;
    }

    public GheNgoi toEntity(GheNgoiDTO dto) {
        GheNgoi e = mapper.map(dto, GheNgoi.class);
        e.setPhongChieu(null);
        e.setIdGheNgoi(null);
        return e;
    }

    public SoDoGheDTO.GheInSoDoDTO toSoDoGhe(GheNgoi e) {
        return mapper.map(e, SoDoGheDTO.GheInSoDoDTO.class);
    }
}
