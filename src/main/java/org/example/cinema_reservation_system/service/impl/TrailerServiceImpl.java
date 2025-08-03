package org.example.cinema_reservation_system.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.moviedto.TrailerRequestDto;
import org.example.cinema_reservation_system.dto.moviedto.TrailerResponseDto;
import org.example.cinema_reservation_system.entity.Movie;
import org.example.cinema_reservation_system.entity.Trailer;
import org.example.cinema_reservation_system.exception.ResourceNotFoundException;
import org.example.cinema_reservation_system.mapper.TrailerMapper;
import org.example.cinema_reservation_system.repository.movie.MovieRepository;
import org.example.cinema_reservation_system.repository.trailer.TrailerRepository;
import org.example.cinema_reservation_system.service.TrailerService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrailerServiceImpl implements TrailerService {
    private final TrailerRepository trailerRepo;
    private final MovieRepository phimRepo;
    private final TrailerMapper trailerMapper;

    @Override
    public List<TrailerResponseDto> getAll() {
        return trailerRepo.findAll().stream().map(trailerMapper::toDto).toList();
    }

    @Override
    public TrailerResponseDto getById(Integer id) {
        Trailer trailer = trailerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trailer với ID: " + id));
        return trailerMapper.toDto(trailer);
    }

    @Override
    public TrailerResponseDto create(TrailerRequestDto dto) {
        Movie phim = phimRepo.findById(dto.getIdPhim()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim với ID: " + dto.getIdPhim()));
        Trailer trailer = trailerMapper.toEntity(dto);
        trailer.setPhim(phim);
        return trailerMapper.toDto(trailerRepo.save(trailer));
    }

    @Override
    public TrailerResponseDto update(Integer id, TrailerRequestDto dto) {
        Trailer trailer = trailerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trailer với ID: " + id));
        Movie phim = phimRepo.findById(dto.getIdPhim()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim với ID: " + dto.getIdPhim()));
        trailer.setUrl(dto.getUrl());
        trailer.setPhim(phim);
        return trailerMapper.toDto(trailerRepo.save(trailer));
    }

    @Override
    public Map<String, Object> delete(Integer id) {
        Trailer trailer = trailerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy trailer với ID: " + id));

        trailerRepo.delete(trailer);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Xóa trailer thành công");
        response.put("idTrailer", trailer.getIdTrailer());
        response.put("url", trailer.getUrl());

        return response;
    }

}
