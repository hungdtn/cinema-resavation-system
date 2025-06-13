package org.example.cinema_reservation_system.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.rapchieudto.RapChieuRequestDto;
import org.example.cinema_reservation_system.dto.rapchieudto.RapChieuResponseDto;
import org.example.cinema_reservation_system.entity.RapChieu;
import org.example.cinema_reservation_system.exception.ResourceAlreadyExistsException;
import org.example.cinema_reservation_system.exception.ResourceNotFoundException;
import org.example.cinema_reservation_system.repository.rapchieurepository.RapChieuRepository;
import org.example.cinema_reservation_system.service.RapChieuService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RapChieuServiceImpl implements RapChieuService {
    private final RapChieuRepository repo;
    private final ModelMapper mapper;

    @Override
    public List<RapChieuResponseDto> findAll() {
        return repo.findAll().stream().map(r -> mapper.map(r, RapChieuResponseDto.class)).toList();
    }

    @Override
    public RapChieuResponseDto findById(Integer id) {
        return mapper.map(
                repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy")),
                RapChieuResponseDto.class
        );
    }

//    @Override
//    public RapChieuResponseDto save(RapChieuRequestDto dto) {
//        RapChieu entity = mapper.map(dto, RapChieu.class);
//        return mapper.map(repo.save(entity), RapChieuResponseDto.class);
//    }
//
//    @Override
//    public RapChieuResponseDto update(Integer id, RapChieuRequestDto dto) {
//        RapChieu r = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy"));
//        r.setTenRapChieu(dto.getTenRapChieu());
//        r.setDiaChi(dto.getDiaChi());
//        r.setTrangThaiRapChieu(dto.getTrangThaiRapChieu());
//        r.setSoDienThoai(dto.getSoDienThoai());
//        return mapper.map(repo.save(r), RapChieuResponseDto.class);
//    }
@Override
@Transactional
public RapChieuResponseDto save(RapChieuRequestDto dto) {
    // Kiểm tra tên trùng
    if (repo.existsByTenRapChieuIgnoreCase(dto.getTenRapChieu())) {
        throw new ResourceNotFoundException("Tên rạp chiếu đã tồn tại: " + dto.getTenRapChieu());
    }

    // Gọi native query để insert
    repo.insertRapChieu(
            dto.getTenRapChieu(),
            dto.getDiaChi(),
            dto.getSoDienThoai(),
            dto.getTrangThaiRapChieu().name()
    );

    // Lấy bản ghi mới nhất theo tên
    RapChieu saved = repo.findTopByTenRapChieuOrderByIdRapChieuDesc(dto.getTenRapChieu())
            .orElseThrow(() -> new RuntimeException("Không thể lấy rạp chiếu vừa thêm"));

    return mapper.map(saved, RapChieuResponseDto.class);
}

    @Override
    @Transactional
    public RapChieuResponseDto update(Integer id, RapChieuRequestDto dto) {
        RapChieu rc = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy rạp chiếu với ID: " + id));

        if (!rc.getTenRapChieu().equalsIgnoreCase(dto.getTenRapChieu())
                && repo.existsByTenRapChieuIgnoreCase(dto.getTenRapChieu())) {
            throw new ResourceAlreadyExistsException("Tên rạp chiếu đã tồn tại: " + dto.getTenRapChieu());
        }

        repo.updateRapChieu(
                id,
                dto.getTenRapChieu(),
                dto.getDiaChi(),
                dto.getSoDienThoai(),
                dto.getTrangThaiRapChieu().name()
        );

        RapChieu updated = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không thể lấy thông tin sau khi cập nhật"));

        return mapper.map(updated, RapChieuResponseDto.class);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy rạp chiếu để xóa");
        }
        repo.deleteById(id);
    }

    @Override
    public List<RapChieuResponseDto> search(String keyword, String trangThaiRaw, String diaChi) {
        String normalizedKeyword = keyword != null ? removeAccent(keyword) : null;
        String normalizedDiaChi = diaChi != null ? removeAccent(diaChi) : null;

        return repo.findAll().stream()
                .filter(r -> {
                    // Chuẩn hóa cả tên và địa chỉ
                    String ten = removeAccent(r.getTenRapChieu());
                    String dia = removeAccent(r.getDiaChi());

                    boolean matchKeyword = normalizedKeyword == null || ten.contains(normalizedKeyword);
                    boolean matchDiaChi = normalizedDiaChi == null || dia.contains(normalizedDiaChi);

                    return matchKeyword && matchDiaChi;
                })
                .filter(r -> {
                    if (trangThaiRaw == null) return true;
                    try {
                        return r.getTrangThaiRapChieu().name().equalsIgnoreCase(trangThaiRaw);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .map(r -> mapper.map(r, RapChieuResponseDto.class))
                .toList();
    }


    private String removeAccent(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

}
