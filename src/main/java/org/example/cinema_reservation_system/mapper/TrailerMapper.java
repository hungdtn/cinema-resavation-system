package org.example.cinema_reservation_system.mapper;

import org.example.cinema_reservation_system.dto.moviedto.TrailerRequestDto;
import org.example.cinema_reservation_system.dto.moviedto.TrailerResponseDto;
import org.example.cinema_reservation_system.entity.Trailer;
import org.springframework.stereotype.Component;


@Component
public class TrailerMapper {
    public TrailerResponseDto toDto(Trailer trailer) {
        TrailerResponseDto dto = new TrailerResponseDto();
        dto.setIdTrailer(trailer.getIdTrailer());
        dto.setUrl(trailer.getUrl());

        if (trailer.getPhim() != null) {
            dto.setIdPhim(trailer.getPhim().getIdPhim());
            dto.setTenPhim(trailer.getPhim().getTenPhim());
        }

        return dto;
    }

    public Trailer toEntity(TrailerRequestDto dto) {
        Trailer trailer = new Trailer();
        trailer.setUrl(dto.getUrl());
        return trailer;
    }
}
