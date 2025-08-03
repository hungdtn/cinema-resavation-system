package org.example.cinema_reservation_system.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.theaterdto.TheaterRequestDto;
import org.example.cinema_reservation_system.dto.theaterdto.TheaterResponseDto;
import org.example.cinema_reservation_system.entity.Theater;
import org.example.cinema_reservation_system.exception.ResourceAlreadyExistsException;
import org.example.cinema_reservation_system.exception.ResourceNotFoundException;
import org.example.cinema_reservation_system.repository.theater.TheaterRepository;
import org.example.cinema_reservation_system.service.CinemaService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TheaterServiceImpl implements CinemaService {

    private final TheaterRepository repo;
    private final ModelMapper mapper;

    @Override
    public List<TheaterResponseDto> findAll() {
        return repo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public TheaterResponseDto findById(Integer id) {
        return mapToDto(getByIdOrThrow(id));
    }

    @Override
    public TheaterResponseDto save(TheaterRequestDto dto) {
        validateNameUnique(dto.getTenRapChieu(), null);

        repo.insertRapChieu(
                dto.getTenRapChieu(),
                dto.getDiaChi(),
                dto.getSoDienThoai(),
                dto.getTrangThaiRapChieu().name()
        );

        Theater saved = repo.findTopByTenRapChieuOrderByIdRapChieuDesc(dto.getTenRapChieu())
                .orElseThrow(() -> new RuntimeException("Không thể lấy rạp chiếu vừa thêm"));

        return mapToDto(saved);
    }

    @Override
    public TheaterResponseDto update(Integer id, TheaterRequestDto dto) {
        Theater existing = getByIdOrThrow(id);

        validateNameUnique(dto.getTenRapChieu(), existing.getTenRapChieu());

        repo.updateRapChieu(
                id,
                dto.getTenRapChieu(),
                dto.getDiaChi(),
                dto.getSoDienThoai(),
                dto.getTrangThaiRapChieu().name()
        );

        return mapToDto(getByIdOrThrow(id));
    }

    @Override
    public void delete(Integer id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy rạp chiếu để xóa");
        }
        repo.deleteById(id);
    }

    @Override
    public List<TheaterResponseDto> search(String keyword, String trangThaiRaw, String diaChi) {
        Predicate<Theater> filter = buildFilter(
                normalize(keyword),
                normalize(diaChi),
                trangThaiRaw
        );

        return repo.findAll().stream()
                .filter(filter)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /** ========== PRIVATE HELPERS ========== */

    private Theater getByIdOrThrow(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy rạp chiếu với ID: " + id));
    }

    private void validateNameUnique(String newName, String oldName) {
        if (newName == null) return;

        boolean isDifferent = (oldName == null) || !oldName.equalsIgnoreCase(newName);
        if (isDifferent && repo.existsByTenRapChieuIgnoreCase(newName)) {
            throw new ResourceAlreadyExistsException("Tên rạp chiếu đã tồn tại: " + newName);
        }
    }

    private Predicate<Theater> buildFilter(String keywordNorm, String diaChiNorm, String trangThaiRaw) {
        return theater -> {
            if (keywordNorm != null && !normalize(theater.getTenRapChieu()).contains(keywordNorm)) return false;
            if (diaChiNorm != null && !normalize(theater.getDiaChi()).contains(diaChiNorm)) return false;
            if (trangThaiRaw != null && !trangThaiRaw.isBlank()) {
                try {
                    return theater.getTrangThai().name().equalsIgnoreCase(trangThaiRaw);
                } catch (Exception e) {
                    return false;
                }
            }
            return true;
        };
    }

    private String normalize(String input) {
        if (input == null) return null;
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

    private TheaterResponseDto mapToDto(Theater entity) {
        return mapper.map(entity, TheaterResponseDto.class);
    }
}
