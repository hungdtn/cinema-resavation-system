package org.example.cinema_reservation_system.service.seat.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cinema_reservation_system.dto.seatdto.BookSeatRequestDTO;
import org.example.cinema_reservation_system.dto.seatdto.SeatDTO;
import org.example.cinema_reservation_system.dto.seatdto.SeatingChartDTO;
import org.example.cinema_reservation_system.entity.Seat;
import org.example.cinema_reservation_system.entity.Room;
import org.example.cinema_reservation_system.exception.BusinessException;
import org.example.cinema_reservation_system.mapper.seat.SeatModelMapper;
import org.example.cinema_reservation_system.repository.seat.SeatRepository;
import org.example.cinema_reservation_system.repository.room.RoomRepository;
import org.example.cinema_reservation_system.service.seat.SeatService;
import org.example.cinema_reservation_system.utils.enums.TrangThaiGheNgoi;
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
public class SeatServiceImpl implements SeatService {

    private final SeatRepository gheNgoiRepository;
    private final RoomRepository phongChieuRepository;
    private final SeatModelMapper gheNgoiMapper;

    @Override
    public SeatDTO create(SeatDTO dto) {
        log.info("Creating seat {}{} in room {}", dto.getHangGhe(), dto.getSoGhe(), dto.getIdPhongChieu());
        Room phong = phongChieuRepository.findById(dto.getIdPhongChieu())
                .orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại"));
        if (gheNgoiRepository.existsByPhongChieuIdPhongChieuAndHangGheAndSoGhe(
                phong.getIdPhongChieu(), dto.getHangGhe(), dto.getSoGhe())) {
            throw new BusinessException("Vị trí ghế đã tồn tại");
        }
        Seat entity = gheNgoiMapper.toEntity(dto);
        entity.setPhongChieu(phong);
        entity.setTrangThai(TrangThaiGheNgoi.CON_TRONG);
        Seat saved = gheNgoiRepository.save(entity);
        return gheNgoiMapper.toDTO(saved);
    }

    @Override
    public SeatDTO update(Integer id, SeatDTO dto) {
        log.info("Updating seat ID {}", id);
        Seat existing = gheNgoiRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Ghế không tồn tại"));
        if (!existing.getHangGhe().equals(dto.getHangGhe()) ||
                !existing.getSoGhe().equals(dto.getSoGhe())) {
            if (gheNgoiRepository.existsByPhongChieuIdPhongChieuAndHangGheAndSoGheAndIdGheNgoiNot(
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
        Seat updated = gheNgoiRepository.save(existing);
        return gheNgoiMapper.toDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public SeatDTO findById(Integer id) {
        Seat e = gheNgoiRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Ghế không tồn tại"));
        return gheNgoiMapper.toDTO(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SeatDTO> findAll(Pageable pageable) {
        return gheNgoiRepository.findAll(pageable)
                .map(gheNgoiMapper::toDTO);
    }

    @Override
    public void delete(Integer id) {
        Seat e = gheNgoiRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Ghế không tồn tại"));
        if (e.getTrangThai() == TrangThaiGheNgoi.DA_DAT) {
            throw new BusinessException("Không thể xóa ghế đã đặt");
        }
        gheNgoiRepository.delete(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatDTO> findByPhongChieu(Integer roomId) {
        phongChieuRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại"));
        return gheNgoiRepository.findByPhongChieuIdPhongChieuOrderByHangGheAscSoGheAsc(roomId)
                .stream()
                .map(gheNgoiMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SeatingChartDTO getSoDoGhe(Integer roomId) {
        Room phong = phongChieuRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại"));
        List<SeatingChartDTO.GheInSoDoDTO> layout = gheNgoiRepository
                .findByPhongChieuIdPhongChieuOrderByHangGheAscSoGheAsc(roomId)
                .stream()
                .map(gheNgoiMapper::toSoDoGhe)
                .collect(Collectors.toList());
        return SeatingChartDTO.builder()
                .idPhongChieu(roomId)
                .tenPhongChieu(phong.getTenPhongChieu())
                .danhSachGhe(layout)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatDTO> findGheTrong(Integer roomId) {
        phongChieuRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại"));
        return gheNgoiRepository
                .findByPhongChieuIdPhongChieuAndTrangThaiOrderByHangGheAscSoGheAsc(
                        roomId, TrangThaiGheNgoi.CON_TRONG)
                .stream()
                .map(gheNgoiMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeatDTO> datGhe(BookSeatRequestDTO req) {
        if (req.getDanhSachIdGheNgoi() == null || req.getDanhSachIdGheNgoi().isEmpty()) {
            throw new BusinessException("Danh sách ghế không được để trống");
        }
        List<Seat> seats = gheNgoiRepository.findAllById(req.getDanhSachIdGheNgoi());
        if (seats.size() != req.getDanhSachIdGheNgoi().size()) {
            throw new BusinessException("Một số ghế không tồn tại");
        }
        List<Seat> unavailable = seats.stream()
                .filter(g -> g.getTrangThai() != TrangThaiGheNgoi.CON_TRONG)
                .collect(Collectors.toList());
        if (!unavailable.isEmpty()) {
            String pos = unavailable.stream()
                    .map(g -> g.getHangGhe() + g.getSoGhe())
                    .collect(Collectors.joining(", "));
            throw new BusinessException("Ghế không khả dụng: " + pos);
        }
        seats.forEach(g -> g.setTrangThai(TrangThaiGheNgoi.DANG_SU_DUNG));
        List<Seat> booked = gheNgoiRepository.saveAll(seats);
        return booked.stream()
                .map(gheNgoiMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void xacNhanDatGhe(List<Integer> ids) {
        List<Seat> seats = gheNgoiRepository.findAllById(ids);
        List<Seat> choosing = seats.stream()
                .filter(g -> g.getTrangThai() == TrangThaiGheNgoi.DANG_SU_DUNG)
                .collect(Collectors.toList());
        if (choosing.isEmpty()) {
            throw new BusinessException("Không có ghế để xác nhận");
        }
        choosing.forEach(g -> g.setTrangThai(TrangThaiGheNgoi.DA_DAT));
        gheNgoiRepository.saveAll(choosing);
    }

    @Override
    public void huyDatGhe(List<Integer> ids) {
        List<Seat> seats = gheNgoiRepository.findAllById(ids);
        seats.forEach(g -> {
            if (g.getTrangThai() == TrangThaiGheNgoi.DANG_SU_DUNG ||
                    g.getTrangThai() == TrangThaiGheNgoi.DA_DAT) {
                g.setTrangThai(TrangThaiGheNgoi.CON_TRONG);
            }
        });
        gheNgoiRepository.saveAll(seats);
    }

    @Override
    public List<SeatDTO> createBulkGhe(Integer roomId, String loaiGhe,
                                       String startRow, int numRows, int seatsPerRow) {
        Room phong = phongChieuRepository.findById(roomId)
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
        List<Seat> toCreate = new ArrayList<>();
        for (int r = 0; r < numRows; r++) {
            char current = (char) (rowChar + r);
            for (int s = 1; s <= seatsPerRow; s++) {
                String hang = String.valueOf(current);
                String so = String.valueOf(s);
                if (!gheNgoiRepository.existsByPhongChieuIdPhongChieuAndHangGheAndSoGhe(
                        roomId, hang, so)) {
                    Seat g = new Seat();
                    g.setPhongChieu(phong);
                    g.setHangGhe(hang);
                    g.setSoGhe(so);
                    g.setLoaiGhe(loaiGhe);
                    g.setTrangThai(TrangThaiGheNgoi.CON_TRONG);
                    toCreate.add(g);
                }
            }
        }
        if (toCreate.isEmpty()) {
            throw new BusinessException("Tất cả ghế đã tồn tại");
        }
        List<Seat> saved = gheNgoiRepository.saveAll(toCreate);
        return saved.stream()
                .map(gheNgoiMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByPhongChieu(Integer roomId) {
        phongChieuRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại"));
        long booked = gheNgoiRepository.countByPhongChieuIdPhongChieuAndTrangThai(
                roomId, TrangThaiGheNgoi.DA_DAT);
        if (booked > 0) {
            throw new BusinessException("Không thể xóa khi có ghế đã đặt");
        }
        gheNgoiRepository.deleteByPhongChieuIdPhongChieu(roomId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getThongKeGhe(Integer roomId) {
        phongChieuRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại"));
        List<Seat> all = gheNgoiRepository.findByPhongChieuIdPhongChieu(roomId);
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("tongSoGhe", (long) all.size());
        stats.put("soGheTrong", all.stream()
                .filter(g -> g.getTrangThai() == TrangThaiGheNgoi.CON_TRONG)
                .count());
        stats.put("soGheDangChon", all.stream()
                .filter(g -> g.getTrangThai() == TrangThaiGheNgoi.DANG_SU_DUNG)
                .count());
        stats.put("soGheDaDat", all.stream()
                .filter(g -> g.getTrangThai() == TrangThaiGheNgoi.DA_DAT)
                .count());
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isGheAvailable(Integer idGheNgoi) {
        return gheNgoiRepository.findById(idGheNgoi)
                .map(g -> g.getTrangThai() == TrangThaiGheNgoi.CON_TRONG)
                .orElse(false);
    }
}
