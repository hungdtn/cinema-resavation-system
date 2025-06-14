//package org.example.cinema_reservation_system.service.suatchieu.impl;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.cinema_reservation_system.dto.suatchieudto.*;
//import org.example.cinema_reservation_system.entity.*;
//import org.example.cinema_reservation_system.exception.BusinessException;
//import org.example.cinema_reservation_system.mapper.suatchieu.SuatChieuModelMapper;
//import org.example.cinema_reservation_system.repository.suatchieu.SuatChieuRepository;
//import org.example.cinema_reservation_system.repository.phim.PhimRepository;
//import org.example.cinema_reservation_system.repository.phongchieu.PhongChieuRepository;
//import org.example.cinema_reservation_system.service.suatchieu.SuatChieuService;
//import org.example.cinema_reservation_system.utils.Enum.TrangThaiSuatChieu;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class SuatChieuServiceImpl implements SuatChieuService {
//    private final SuatChieuRepository repo;
//    private final PhimRepository phimRepo;
//    private final PhongChieuRepository phongRepo;
//    private final SuatChieuModelMapper mapper;
//
//    @Override
//    public SuatChieuResponseDTO create(SuatChieuRequestDTO req) {
//        log.info("Creating showtime {}", req.getTenSuatChieu());
//        validateShowtimeSchedule(req);
//        validateTimeConflict(req.getIdPhongChieu(), req.getNgayChieu(), req.getThoiGianBatDau(), req.getThoiGianKetThuc(), null);
//
//        SuatChieu e = mapper.toEntity(req);
//        e.setPhim(phimRepo.findById(req.getIdPhim()).orElseThrow(() -> new BusinessException("Phim không tồn tại")));
//        e.setPhongChieu(phongRepo.findById(req.getIdPhongChieu()).orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại")));
//
//        SuatChieu saved = repo.save(e);
//        return mapper.toResponse(saved);
//    }
//
//    @Override
//    public SuatChieuResponseDTO update(Integer id, SuatChieuRequestDTO req) {
//        SuatChieu exist = repo.findById(id).orElseThrow(() -> new BusinessException("Không tìm thấy suất chiếu"));
//        if (!canUpdateShowtime(id)) throw new BusinessException("Không thể cập nhật");
//        validateShowtimeSchedule(req);
//        validateTimeConflict(req.getIdPhongChieu(), req.getNgayChieu(), req.getThoiGianBatDau(), req.getThoiGianKetThuc(), id);
//
//        mapper.toEntity(req).setIdSuatChieu(id);
//        exist.setTenSuatChieu(req.getTenSuatChieu());
//        exist.setNgayChieu(req.getNgayChieu());
//        exist.setGioChieu(req.getGioChieu());
//        exist.setThoiGianBatDau(req.getThoiGianBatDau());
//        exist.setThoiGianKetThuc(req.getThoiGianKetThuc());
//        exist.setTrangThai(req.getTrangThai());
//
//        SuatChieu updated = repo.save(exist);
//        return mapper.toResponse(updated);
//    }
//
//    @Override
//    public void delete(Integer id) {
//        if (!canDeleteShowtime(id)) throw new BusinessException("Không thể xóa");
//        repo.deleteById(id);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public SuatChieuResponseDTO findById(Integer id) {
//        SuatChieu e = repo.findById(id).orElseThrow(() -> new BusinessException("Không tìm thấy"));
//        return mapper.toResponse(e);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Page<SuatChieuSummaryDTO> findAll(Pageable p) {
//        return repo.findAll(p).map(mapper::toSummary);
//    }
//
//    @Override
//    public SuatChieuResponseDTO updateStatus(SuatChieuStatusUpdateDTO dto) {
//        SuatChieu e = repo.findById(dto.getIdSuatChieu()).orElseThrow(() -> new BusinessException("Không tìm thấy"));
//        e.setTrangThai(dto.getTrangThai());
//        return mapper.toResponse(repo.save(e));
//    }
//
//    @Override public void activateShowtime(Integer id) { updateStatus(new SuatChieuStatusUpdateDTO(id, TrangThaiSuatChieu.da_len_lich)); }
//    @Override public void deactivateShowtime(Integer id) { updateStatus(new SuatChieuStatusUpdateDTO(id, TrangThaiSuatChieu.tam_dung)); }
//
//    @Override @Transactional(readOnly = true)
//    public List<SuatChieuSummaryDTO> findByPhimId(Integer phimId) {
//        return repo.findByPhim_IdPhim(phimId).stream().map(mapper::toSummary).collect(Collectors.toList());
//    }
//    @Override public Page<SuatChieuSummaryDTO> findByPhimId(Integer phimId, Pageable p) {
//        return repo.findByPhim_IdPhim(phimId, p).map(mapper::toSummary);
//    }
//    @Override public List<SuatChieuSummaryDTO> findActiveShowtimesByPhimId(Integer phimId) {
//        return repo.findByPhim_IdPhimAndTrangThai(phimId, TrangThaiSuatChieu.da_len_lich).stream().map(mapper::toSummary).collect(Collectors.toList());
//    }
//
//    @Override @Transactional(readOnly = true)
//    public List<SuatChieuSummaryDTO> findByPhongChieuId(Integer phongId) {
//        return repo.findByPhongChieu_IdPhongChieu(phongId).stream().map(mapper::toSummary).collect(Collectors.toList());
//    }
//    @Override public Page<SuatChieuSummaryDTO> findByPhongChieuId(Integer phongId, Pageable p) {
//        return repo.findByPhongChieu_IdPhongChieu(phongId, p).map(mapper::toSummary);
//    }
//
//    @Override @Transactional(readOnly = true)
//    public List<SuatChieuSummaryDTO> findByNgayChieu(LocalDate ngay) {
//        return repo.findByNgayChieu(ngay).stream().map(mapper::toSummary).collect(Collectors.toList());
//    }
//    @Override public Page<SuatChieuSummaryDTO> findByNgayChieu(LocalDate ngay, Pageable p) {
//        return repo.findByNgayChieu(ngay, p).map(mapper::toSummary);
//    }
//
//    @Override public List<SuatChieuSummaryDTO> findByNgayChieuBetween(LocalDate s, LocalDate e) {
//        return repo.findByNgayChieuBetween(s, e).stream().map(mapper::toSummary).collect(Collectors.toList());
//    }
//    @Override @Transactional(readOnly = true)
//    public List<SuatChieuSummaryDTO> findByTrangThai(TrangThaiSuatChieu t) {
//        return repo.findByTrangThai(t).stream().map(mapper::toSummary).collect(Collectors.toList());
//    }
//    @Override public Page<SuatChieuSummaryDTO> findByTrangThai(TrangThaiSuatChieu t, Pageable p) {
//        return repo.findByTrangThai(t, p).map(mapper::toSummary);
//    }
//
//    @Override @Transactional(readOnly = true)
//    public List<SuatChieuSummaryDTO> findAvailableShowtimes() {
//        return repo.findByTrangThaiAndNgayChieuGreaterThanEqual(TrangThaiSuatChieu.da_len_lich, LocalDate.now()).stream().map(mapper::toSummary).collect(Collectors.toList());
//    }
//    @Override public List<SuatChieuSummaryDTO> findAvailableShowtimesByPhimAndDate(Integer phimId, LocalDate ngay) {
//        return repo.findByPhim_IdPhimAndNgayChieuAndTrangThai(phimId, ngay, TrangThaiSuatChieu.da_len_lich).stream().map(mapper::toSummary).collect(Collectors.toList());
//    }
//    @Override public List<SuatChieuSummaryDTO> findShowtimesByPhimAndDateRange(Integer phimId, LocalDate s, LocalDate e) {
//        return repo.findByPhim_IdPhimAndNgayChieuBetween(phimId, s, e).stream().map(mapper::toSummary).collect(Collectors.toList());
//    }
//
//    @Override @Transactional(readOnly = true)
//    public boolean isTimeSlotAvailable(Integer roomId, LocalDate ngay, LocalTime start, LocalTime end) {
//        return isTimeSlotAvailableForUpdate(null, roomId, ngay, start, end);
//    }
//    @Override @Transactional(readOnly = true)
//    public boolean isTimeSlotAvailableForUpdate(Integer id, Integer roomId, LocalDate ngay, LocalTime start, LocalTime end) {
//        List<SuatChieu> c = repo.findTimeConflicts(roomId, ngay, start, end);
//        if (id != null) c.removeIf(sc -> sc.getIdSuatChieu().equals(id));
//        return c.isEmpty();
//    }
//
//    @Override @Transactional(readOnly = true)
//    public boolean canDeleteShowtime(Integer id) {
//        SuatChieu e = repo.findById(id).orElseThrow(() -> new BusinessException("Không tìm thấy"));
//        return !e.getNgayChieu().isBefore(LocalDate.now());
//    }
//    @Override @Transactional(readOnly = true)
//    public boolean canUpdateShowtime(Integer id) {
//        SuatChieu e = repo.findById(id).orElseThrow(() -> new BusinessException("Không tìm thấy"));
//        return !e.getNgayChieu().isBefore(LocalDate.now());
//    }
//
//    @Override public void validateShowtimeSchedule(SuatChieuRequestDTO req) {
//        if (req.getThoiGianBatDau().isAfter(req.getThoiGianKetThuc())) {
//            throw new BusinessException("Start must be before end");
//        }
//        if (req.getNgayChieu().isBefore(LocalDate.now())) {
//            throw new BusinessException("Ngày chiếu không được trong quá khứ");
//        }
//    }
//
//    @Override public void validateTimeConflict(Integer roomId, LocalDate ngay, LocalTime start, LocalTime end, Integer excludeId) {
//        if (!isTimeSlotAvailableForUpdate(excludeId, roomId, ngay, start, end)) {
//            throw new BusinessException("Xung đột lịch chiếu");
//        }
//    }
//
//    @Override @Transactional(readOnly = true) public long countByPhimId(Integer phimId) { return repo.countByPhim_IdPhim(phimId); }
//    @Override @Transactional(readOnly = true) public long countByPhongChieuId(Integer phongId) { return repo.countByPhongChieu_IdPhongChieu(phongId); }
//    @Override @Transactional(readOnly = true) public long countByNgayChieu(LocalDate ngay) { return repo.countByNgayChieu(ngay); }
//    @Override @Transactional(readOnly = true) public long countByTrangThai(TrangThaiSuatChieu t) { return repo.countByTrangThai(t); }
//
//    @Override @Transactional(readOnly = true)
//    public List<SuatChieuSummaryDTO> findUpcomingShowtimes(int days) {
//        return repo.findByNgayChieuBetween(LocalDate.now(), LocalDate.now().plusDays(days)).stream().map(mapper::toSummary).collect(Collectors.toList());
//    }
//
//    @Override public List<SuatChieuSummaryDTO> findTodayShowtimes() { return findByNgayChieu(LocalDate.now()); }
//
//    @Override public void expireOldShowtimes() {
//        repo.findByNgayChieuBeforeAndTrangThai(LocalDate.now().minusDays(1), TrangThaiSuatChieu.da_len_lich)
//                .forEach(sc -> sc.setTrangThai(TrangThaiSuatChieu.da_ket_thuc));
//        // bulk save if needed
//    }
//
//    @Override @Transactional(readOnly = true)
//    public Page<SuatChieuSummaryDTO> searchShowtimes(String kw, LocalDate from, LocalDate to, TrangThaiSuatChieu t, Pageable p) {
//        return repo.searchShowtimes(kw, from, to, t, p).map(mapper::toSummary);
//    }
//}