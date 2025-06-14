package org.example.cinema_reservation_system.service.ghengoi.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cinema_reservation_system.dto.ghengoidto.DatGheRequestDTO;
import org.example.cinema_reservation_system.dto.ghengoidto.GheNgoiDTO;
import org.example.cinema_reservation_system.dto.ghengoidto.SoDoGheDTO;
import org.example.cinema_reservation_system.entity.GheNgoi;
import org.example.cinema_reservation_system.entity.PhongChieu;
import org.example.cinema_reservation_system.exception.BusinessException;
import org.example.cinema_reservation_system.mapper.ghengoi.GheNgoiModelMapper;
import org.example.cinema_reservation_system.repository.ghengoi.GheNgoiRepository;
import org.example.cinema_reservation_system.repository.phongchieurepository.PhongChieuRepo;
import org.example.cinema_reservation_system.service.ghengoi.GheNgoiService;
import org.example.cinema_reservation_system.utils.Enum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GheNgoiServiceImpl implements GheNgoiService {

    private final GheNgoiRepository gheNgoiRepository;
    private final PhongChieuRepo phongChieuRepository;
    private final GheNgoiModelMapper gheNgoiMapper;

    @Override
    public GheNgoiDTO create(GheNgoiDTO dto) {
        log.info("Creating seat {}{} in room {}", dto.getHangGhe(), dto.getSoGhe(), dto.getIdPhongChieu());
        PhongChieu phong = phongChieuRepository.findById(dto.getIdPhongChieu())
                .orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại"));
        if (gheNgoiRepository.existsByPhongChieuIdAndHangGheAndSoGhe(
                phong.getId(), dto.getHangGhe(), dto.getSoGhe())) {
            throw new BusinessException("Vị trí ghế đã tồn tại");
        }
        GheNgoi entity = gheNgoiMapper.toEntity(dto);
        entity.setPhongChieu(phong);
        entity.setTrangThai(Enum.TrangThaiGheNgoi.con_trong);
        GheNgoi saved = gheNgoiRepository.save(entity);
        return gheNgoiMapper.toDTO(saved);
    }

    @Override
    public GheNgoiDTO update(Integer id, GheNgoiDTO dto) {
        log.info("Updating seat ID {}", id);
        GheNgoi existing = gheNgoiRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Ghế không tồn tại"));
        if (!existing.getHangGhe().equals(dto.getHangGhe()) ||
                !existing.getSoGhe().equals(dto.getSoGhe())) {
            if (gheNgoiRepository.existsByPhongChieuIdAndHangGheAndSoGheAndIdGheNgoiNot(
                    dto.getIdPhongChieu(), dto.getHangGhe(), dto.getSoGhe(), id)) {
                throw new BusinessException("Vị trí ghế đã tồn tại");
            }
        }
        existing.setHangGhe(dto.getHangGhe());
        existing.setSoGhe(dto.getSoGhe());
        existing.setLoaiGhe(dto.getLoaiGhe());
        if (dto.getTrangThai() != null) {
            existing.setTrangThai(dto.getTrangThai());
        }
        GheNgoi updated = gheNgoiRepository.save(existing);
        return gheNgoiMapper.toDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public GheNgoiDTO findById(Integer id) {
        GheNgoi e = gheNgoiRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Ghế không tồn tại"));
        return gheNgoiMapper.toDTO(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GheNgoiDTO> findAll(Pageable pageable) {
        return gheNgoiRepository.findAll(pageable)
                .map(gheNgoiMapper::toDTO);
    }

    @Override
    public void delete(Integer id) {
        GheNgoi e = gheNgoiRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Ghế không tồn tại"));
        if (e.getTrangThai() == Enum.TrangThaiGheNgoi.da_dat) {
            throw new BusinessException("Không thể xóa ghế đã đặt");
        }
        gheNgoiRepository.delete(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GheNgoiDTO> findByPhongChieu(Integer roomId) {
        phongChieuRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại"));
        return gheNgoiRepository.findByPhongChieuIdOrderByHangGheAscSoGheAsc(roomId)
                .stream()
                .map(gheNgoiMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SoDoGheDTO getSoDoGhe(Integer roomId) {
        PhongChieu phong = phongChieuRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại"));
        List<SoDoGheDTO.GheInSoDoDTO> layout = gheNgoiRepository
                .findByPhongChieuIdOrderByHangGheAscSoGheAsc(roomId)
                .stream()
                .map(gheNgoiMapper::toSoDoGhe)
                .collect(Collectors.toList());
        return SoDoGheDTO.builder()
                .idPhongChieu(roomId)
                .tenPhongChieu(phong.getTenPhongChieu())
                .danhSachGhe(layout)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GheNgoiDTO> findGheTrong(Integer roomId) {
        phongChieuRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại"));
        return gheNgoiRepository
                .findByPhongChieuIdAndTrangThaiOrderByHangGheAscSoGheAsc(
                        roomId, Enum.TrangThaiGheNgoi.con_trong)
                .stream()
                .map(gheNgoiMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GheNgoiDTO> datGhe(DatGheRequestDTO req) {
        if (req.getDanhSachIdGheNgoi() == null || req.getDanhSachIdGheNgoi().isEmpty()) {
            throw new BusinessException("Danh sách ghế không được để trống");
        }
        List<GheNgoi> seats = gheNgoiRepository.findAllById(req.getDanhSachIdGheNgoi());
        if (seats.size() != req.getDanhSachIdGheNgoi().size()) {
            throw new BusinessException("Một số ghế không tồn tại");
        }
        List<GheNgoi> unavailable = seats.stream()
                .filter(g -> g.getTrangThai() != Enum.TrangThaiGheNgoi.con_trong)
                .collect(Collectors.toList());
        if (!unavailable.isEmpty()) {
            String pos = unavailable.stream()
                    .map(g -> g.getHangGhe() + g.getSoGhe())
                    .collect(Collectors.joining(", "));
            throw new BusinessException("Ghế không khả dụng: " + pos);
        }
        seats.forEach(g -> g.setTrangThai(Enum.TrangThaiGheNgoi.dang_su_dung));
        List<GheNgoi> booked = gheNgoiRepository.saveAll(seats);
        return booked.stream()
                .map(gheNgoiMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void xacNhanDatGhe(List<Integer> ids) {
        List<GheNgoi> seats = gheNgoiRepository.findAllById(ids);
        List<GheNgoi> choosing = seats.stream()
                .filter(g -> g.getTrangThai() == Enum.TrangThaiGheNgoi.dang_su_dung)
                .collect(Collectors.toList());
        if (choosing.isEmpty()) {
            throw new BusinessException("Không có ghế để xác nhận");
        }
        choosing.forEach(g -> g.setTrangThai(Enum.TrangThaiGheNgoi.da_dat));
        gheNgoiRepository.saveAll(choosing);
    }

    @Override
    public void huyDatGhe(List<Integer> ids) {
        List<GheNgoi> seats = gheNgoiRepository.findAllById(ids);
        seats.forEach(g -> {
            if (g.getTrangThai() == Enum.TrangThaiGheNgoi.dang_su_dung ||
                    g.getTrangThai() == Enum.TrangThaiGheNgoi.da_dat) {
                g.setTrangThai(Enum.TrangThaiGheNgoi.con_trong);
            }
        });
        gheNgoiRepository.saveAll(seats);
    }

    @Override
    public List<GheNgoiDTO> createBulkGhe(Integer roomId, String loaiGhe,
                                          String startRow, int numRows, int seatsPerRow) {
        PhongChieu phong = phongChieuRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại"));
        if (startRow == null || startRow.isEmpty()) {
            throw new BusinessException("Hàng bắt đầu không được để trống");
        }
        if (numRows <= 0 || numRows > 26) {
            throw new BusinessException("Số hàng phải từ 1-26");
        }
        if (seatsPerRow <= 0 || seatsPerRow > 50) {
            throw new BusinessException("Số ghế mỗi hàng phải từ 1-50");
        }
        char rowChar = startRow.charAt(0);
        if (rowChar < 'A' || rowChar > 'Z') {
            throw new BusinessException("Hàng bắt đầu phải là chữ hoa A-Z");
        }
        List<GheNgoi> toCreate = new ArrayList<>();
        for (int r = 0; r < numRows; r++) {
            char current = (char) (rowChar + r);
            for (int s = 1; s <= seatsPerRow; s++) {
                String hang = String.valueOf(current);
                String so = String.valueOf(s);
                if (!gheNgoiRepository.existsByPhongChieuIdAndHangGheAndSoGhe(
                        roomId, hang, so)) {
                    GheNgoi g = new GheNgoi();
                    g.setPhongChieu(phong);
                    g.setHangGhe(hang);
                    g.setSoGhe(so);
                    g.setLoaiGhe(loaiGhe);
                    g.setTrangThai(Enum.TrangThaiGheNgoi.con_trong);
                    toCreate.add(g);
                }
            }
        }
        if (toCreate.isEmpty()) {
            throw new BusinessException("Tất cả ghế đã tồn tại");
        }
        List<GheNgoi> saved = gheNgoiRepository.saveAll(toCreate);
        return saved.stream()
                .map(gheNgoiMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByPhongChieu(Integer roomId) {
        phongChieuRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại"));
        long booked = gheNgoiRepository.countByPhongChieuIdAndTrangThai(
                roomId, Enum.TrangThaiGheNgoi.da_dat);
        if (booked > 0) {
            throw new BusinessException("Không thể xóa khi có ghế đã đặt");
        }
        gheNgoiRepository.deleteByPhongChieuId(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getThongKeGhe(Integer roomId) {
        phongChieuRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại"));
        List<GheNgoi> all = gheNgoiRepository.findByPhongChieuId(roomId);
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("tongSoGhe", (long) all.size());
        stats.put("soGheTrong", all.stream()
                .filter(g -> g.getTrangThai() == Enum.TrangThaiGheNgoi.con_trong)
                .count());
        stats.put("soGheDangChon", all.stream()
                .filter(g -> g.getTrangThai() == Enum.TrangThaiGheNgoi.dang_su_dung)
                .count());
        stats.put("soGheDaDat", all.stream()
                .filter(g -> g.getTrangThai() == Enum.TrangThaiGheNgoi.da_dat)
                .count());
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isGheAvailable(Integer idGheNgoi) {
        return gheNgoiRepository.findById(idGheNgoi)
                .map(g -> g.getTrangThai() == Enum.TrangThaiGheNgoi.con_trong)
                .orElse(false);
    }
}
