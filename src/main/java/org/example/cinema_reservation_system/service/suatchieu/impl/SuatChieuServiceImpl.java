package org.example.cinema_reservation_system.service.suatchieu.impl;

// Service triển khai các chức năng xử lý logic cho Suất Chiếu
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cinema_reservation_system.dto.suatchieudto.*;
import org.example.cinema_reservation_system.entity.SuatChieu;
import org.example.cinema_reservation_system.exception.BusinessException;
import org.example.cinema_reservation_system.mapper.suatchieu.SuatChieuModelMapper;
import org.example.cinema_reservation_system.repository.phimrepository.PhimRepository;
import org.example.cinema_reservation_system.repository.phongchieurepository.PhongChieuRepo;
import org.example.cinema_reservation_system.repository.suatchieu.SuatChieuRepository;
import org.example.cinema_reservation_system.service.suatchieu.SuatChieuService;
import org.example.cinema_reservation_system.utils.Enum.TrangThaiSuatChieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SuatChieuServiceImpl implements SuatChieuService {

    private final SuatChieuRepository repo;
    private final PhimRepository phimRepo;
    private final PhongChieuRepo phongRepo;
    private final SuatChieuModelMapper mapper;

    // Tạo mới một suất chiếu
    @Override
    public SuatChieuResponseDTO create(SuatChieuRequestDTO req) {
        validateShowtimeSchedule(req); // kiểm tra giờ bắt đầu và ngày chiếu hợp lệ
        validateTimeConflict(req.getIdPhongChieu(), req.getNgayChieu(), req.getThoiGianBatDau(), req.getThoiGianKetThuc(), null); // kiểm tra xung đột lịch
        SuatChieu e = mapper.toEntity(req);
        e.setPhim(phimRepo.findById(req.getIdPhim()).orElseThrow(() -> new BusinessException("Phim không tồn tại")));
        e.setPhongChieu(phongRepo.findById(req.getIdPhongChieu()).orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại")));
        return mapper.toResponse(repo.save(e));
    }

    // Cập nhật suất chiếu nếu chưa quá thời gian
    @Override
    public SuatChieuResponseDTO update(Integer id, SuatChieuRequestDTO req) {
        SuatChieu exist = repo.findById(id).orElseThrow(() -> new BusinessException("Không tìm thấy suất chiếu"));
        if (!canUpdateShowtime(id)) throw new BusinessException("Không thể cập nhật");
        validateShowtimeSchedule(req);
        validateTimeConflict(req.getIdPhongChieu(), req.getNgayChieu(), req.getThoiGianBatDau(), req.getThoiGianKetThuc(), id);
        exist.setTenSuatChieu(req.getTenSuatChieu());
        exist.setNgayChieu(req.getNgayChieu());
        exist.setGioChieu(req.getGioChieu());
        exist.setThoiGianBatDau(req.getThoiGianBatDau());
        exist.setThoiGianKetThuc(req.getThoiGianKetThuc());
        exist.setTrangThai(req.getTrangThai());
        return mapper.toResponse(repo.save(exist));
    }

    // Xóa suất chiếu nếu chưa diễn ra
    @Override
    public void delete(Integer id) {
        if (!canDeleteShowtime(id)) throw new BusinessException("Không thể xóa");
        repo.deleteById(id);
    }

    // Tìm theo ID suất chiếu
    @Override
    public SuatChieuResponseDTO findById(Integer id) {
        return mapper.toResponse(repo.findById(id).orElseThrow(() -> new BusinessException("Không tìm thấy")));
    }

    // Lấy tất cả suất chiếu phân trang
    @Override
    public Page<SuatChieuSummaryDTO> findAll(Pageable pageable) {
        return repo.findAll(pageable).map(mapper::toSummary);
    }

    // Cập nhật trạng thái suất chiếu (ví dụ: huỷ, đã lên lịch)
    @Override
    public SuatChieuResponseDTO updateStatus(SuatChieuStatusUpdateDTO dto) {
        SuatChieu e = repo.findById(dto.getIdSuatChieu()).orElseThrow(() -> new BusinessException("Không tìm thấy"));
        e.setTrangThai(dto.getTrangThai());
        return mapper.toResponse(repo.save(e));
    }

    // Kích hoạt suất chiếu
    @Override
    public void activateShowtime(Integer id) {
        updateStatus(new SuatChieuStatusUpdateDTO(id, TrangThaiSuatChieu.da_len_lich));
    }

    // Hủy suất chiếu
    @Override
    public void deactivateShowtime(Integer id) {
        updateStatus(new SuatChieuStatusUpdateDTO(id, TrangThaiSuatChieu.da_huy));
    }

    // Lấy suất chiếu theo phim
    @Override
    public List<SuatChieuSummaryDTO> findByPhimId(Integer phimId) {
        return repo.findByPhim_IdPhim(phimId).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    @Override
    public Page<SuatChieuSummaryDTO> findByPhimId(Integer phimId, Pageable pageable) {
        return repo.findByPhim_IdPhim(phimId, pageable).map(mapper::toSummary);
    }

    // Lấy suất chiếu theo phòng
    @Override
    public List<SuatChieuSummaryDTO> findByPhongChieuId(Integer phongId) {
        return repo.findByPhongChieu_Id(phongId).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    @Override
    public Page<SuatChieuSummaryDTO> findByPhongChieuId(Integer phongId, Pageable pageable) {
        return repo.findByPhongChieu_Id(phongId, pageable).map(mapper::toSummary);
    }

    // Lấy suất chiếu theo ngày chiếu
    @Override
    public List<SuatChieuSummaryDTO> findByNgayChieu(LocalDate ngayChieu) {
        return repo.findByNgayChieu(ngayChieu).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    @Override
    public Page<SuatChieuSummaryDTO> findByNgayChieu(LocalDate ngayChieu, Pageable pageable) {
        return repo.findByNgayChieu(ngayChieu, pageable).map(mapper::toSummary);
    }

    @Override
    public List<SuatChieuSummaryDTO> findByNgayChieuBetween(LocalDate startDate, LocalDate endDate) {
        return repo.findByNgayChieuBetween(startDate, endDate).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    // Lấy theo trạng thái chiếu
    @Override
    public List<SuatChieuSummaryDTO> findByTrangThai(TrangThaiSuatChieu trangThai) {
        return repo.findByTrangThai(trangThai).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    @Override
    public Page<SuatChieuSummaryDTO> findByTrangThai(TrangThaiSuatChieu trangThai, Pageable pageable) {
        return repo.findByTrangThai(trangThai, pageable).map(mapper::toSummary);
    }

    // Suất chiếu có thể đặt (đã lên lịch, từ hôm nay trở đi)
    @Override
    public List<SuatChieuSummaryDTO> findAvailableShowtimes() {
        return repo.findAvailableShowtimes(LocalDate.now()).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    // Suất chiếu khả dụng theo phim + ngày
    @Override
    public List<SuatChieuSummaryDTO> findAvailableShowtimesByPhimAndDate(Integer phimId, LocalDate ngayChieu) {
        return repo.findAvailableByPhimAndDate(phimId, ngayChieu).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    // Lấy lịch chiếu phim trong khoảng ngày
    @Override
    public List<SuatChieuSummaryDTO> findShowtimesByPhimAndDateRange(Integer phimId, LocalDate startDate, LocalDate endDate) {
        return repo.findByPhimAndNgayChieuBetween(phimId, startDate, endDate).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    // Suất chiếu đang hoạt động theo phim
    @Override
    public List<SuatChieuSummaryDTO> findActiveShowtimesByPhimId(Integer phimId) {
        return repo.findActiveShowtimesByPhimId(phimId).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    // Kiểm tra xem suất chiếu có thể xóa hay không
    @Override
    public boolean canDeleteShowtime(Integer id) {
        SuatChieu e = repo.findById(id).orElseThrow(() -> new BusinessException("Không tìm thấy"));
        return !e.getNgayChieu().isBefore(LocalDate.now());
    }

    // Kiểm tra xem suất chiếu có thể cập nhật hay không
    @Override
    public boolean canUpdateShowtime(Integer id) {
        SuatChieu e = repo.findById(id).orElseThrow(() -> new BusinessException("Không tìm thấy"));
        return !e.getNgayChieu().isBefore(LocalDate.now());
    }

    // Kiểm tra logic thời gian suất chiếu
    @Override
    public void validateShowtimeSchedule(SuatChieuRequestDTO req) {
        if (req.getThoiGianBatDau().isAfter(req.getThoiGianKetThuc()))
            throw new BusinessException("Start time must be before end time");
        if (req.getNgayChieu().isBefore(LocalDate.now()))
            throw new BusinessException("Ngày chiếu không được trong quá khứ");
    }

    // Kiểm tra có trùng khung giờ trong cùng phòng không
    @Override
    public void validateTimeConflict(Integer phongChieuId, LocalDate ngay, LocalTime start, LocalTime end, Integer excludeId) {
        List<SuatChieu> conflicts = repo.findTimeConflicts(phongChieuId, ngay, start, end);
        if (excludeId != null) {
            conflicts.removeIf(sc -> sc.getIdSuatChieu().equals(excludeId));
        }
        if (!conflicts.isEmpty()) {
            throw new BusinessException("Xung đột lịch chiếu");
        }
    }

    // Thống kê
    @Override
    public long countByPhimId(Integer phimId) {
        return repo.countByPhim_IdPhim(phimId);
    }

    @Override
    public long countByPhongChieuId(Integer phongChieuId) {
        return repo.countByPhongChieu_Id(phongChieuId);
    }

    @Override
    public long countByNgayChieu(LocalDate ngayChieu) {
        return repo.countByNgayChieu(ngayChieu);
    }

    @Override
    public long countByTrangThai(TrangThaiSuatChieu trangThai) {
        return repo.countByTrangThai(trangThai);
    }

    // Lấy các suất chiếu sắp tới trong N ngày
    @Override
    public List<SuatChieuSummaryDTO> findUpcomingShowtimes(int days) {
        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(days);
        return repo.findByNgayChieuBetween(today, end).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    // Lấy suất chiếu hôm nay
    @Override
    public List<SuatChieuSummaryDTO> findTodayShowtimes() {
        return repo.findByNgayChieu(LocalDate.now()).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    // Hủy các suất chiếu đã qua ngày chiếu
    @Override
    public void expireOldShowtimes() {
        List<SuatChieu> expired = repo.findByNgayChieuBefore(LocalDate.now());
        expired.forEach(sc -> sc.setTrangThai(TrangThaiSuatChieu.hoan_thanh));
        repo.saveAll(expired);
    }

    // Kiểm tra khung giờ khả dụng khi tạo mới
    @Override
    public boolean isTimeSlotAvailable(Integer roomId, LocalDate ngay, LocalTime start, LocalTime end) {
        return isTimeSlotAvailableForUpdate(null, roomId, ngay, start, end);
    }

    // Kiểm tra khung giờ khả dụng khi cập nhật (bỏ qua chính nó)
    @Override
    public boolean isTimeSlotAvailableForUpdate(Integer id, Integer roomId, LocalDate ngay, LocalTime start, LocalTime end) {
        List<SuatChieu> c = repo.findTimeConflicts(roomId, ngay, start, end);
        if (id != null) c.removeIf(sc -> sc.getIdSuatChieu().equals(id));
        return c.isEmpty();
    }

    // Tìm kiếm nâng cao theo từ khóa, ngày và trạng thái
    @Override
    public Page<SuatChieuSummaryDTO> searchShowtimes(String keyword, LocalDate from, LocalDate to, TrangThaiSuatChieu t, Pageable pageable) {
        return repo.searchShowtimes(keyword, from, to, t, pageable).map(mapper::toSummary);
    }
}