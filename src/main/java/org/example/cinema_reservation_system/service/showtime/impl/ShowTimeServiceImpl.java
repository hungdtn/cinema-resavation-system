package org.example.cinema_reservation_system.service.showtime.impl;

// Service triển khai các chức năng xử lý logic cho Suất Chiếu
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cinema_reservation_system.dto.showtimedto.*;
import org.example.cinema_reservation_system.entity.ShowTime;
import org.example.cinema_reservation_system.exception.BusinessException;
import org.example.cinema_reservation_system.mapper.showtime.ShowTimeModelMapper;
import org.example.cinema_reservation_system.repository.movie.MovieRepository;
import org.example.cinema_reservation_system.repository.room.RoomRepository;
import org.example.cinema_reservation_system.repository.showtime.ShowTimeRepository;
import org.example.cinema_reservation_system.service.showtime.ShowTimeService;
import org.example.cinema_reservation_system.utils.enums.TrangThaiSuatChieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShowTimeServiceImpl implements ShowTimeService {

    private final ShowTimeRepository repo;
    private final MovieRepository phimRepo;
    private final RoomRepository phongRepo;
    private final ShowTimeModelMapper mapper;
    private final ObjectMapper objectMapper;

    // Tạo mới một suất chiếu
    @Override
    public ShowTimeResponseDTO create(ShowTimeRequestDTO req) {
        validateShowtimeSchedule(req); // kiểm tra giờ bắt đầu và ngày chiếu hợp lệ
        validateTimeConflict(req.getIdPhongChieu(), req.getNgayChieu(), req.getThoiGianBatDau(), req.getThoiGianKetThuc(), null); // kiểm tra xung đột lịch
        ShowTime e = mapper.toEntity(req);
        e.setPhim(phimRepo.findById(req.getIdPhim()).orElseThrow(() -> new BusinessException("Phim không tồn tại")));
        e.setPhongChieu(phongRepo.findById(req.getIdPhongChieu()).orElseThrow(() -> new BusinessException("Phòng chiếu không tồn tại")));
        return mapper.toResponse(repo.save(e));
    }

    // Cập nhật suất chiếu nếu chưa quá thời gian
    @Override
    public ShowTimeResponseDTO update(Integer id, ShowTimeRequestDTO req) {
        ShowTime exist = repo.findById(id).orElseThrow(() -> new BusinessException("Không tìm thấy suất chiếu"));
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
    public ShowTimeResponseDTO findById(Integer id) {
        return mapper.toResponse(repo.findById(id).orElseThrow(() -> new BusinessException("Không tìm thấy")));
    }

    // Lấy tất cả suất chiếu phân trang
    @Override
    public Page<ShowTimeSummaryDTO> findAll(Pageable pageable) {
        return repo.findAll(pageable).map(mapper::toSummary);
    }

    // Cập nhật trạng thái suất chiếu (ví dụ: huỷ, đã lên lịch)
    @Override
    public ShowTimeResponseDTO updateStatus(ShowTimeStatusUpdateDTO dto) {
        ShowTime e = repo.findById(dto.getIdSuatChieu()).orElseThrow(() -> new BusinessException("Không tìm thấy"));
        e.setTrangThai(dto.getTrangThai());
        return mapper.toResponse(repo.save(e));
    }

    // Kích hoạt suất chiếu
    @Override
    public void activateShowtime(Integer id) {
        updateStatus(new ShowTimeStatusUpdateDTO(id, TrangThaiSuatChieu.DA_LEN_LICH));
    }

    // Hủy suất chiếu
    @Override
    public void deactivateShowtime(Integer id) {
        updateStatus(new ShowTimeStatusUpdateDTO(id, TrangThaiSuatChieu.DA_HUY));
    }

    // Lấy suất chiếu theo phim
    @Override
    public List<ShowTimeSummaryDTO> findByPhimId(Integer phimId) {
        return repo.findByPhim_IdPhim(phimId).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    @Override
    public Page<ShowTimeSummaryDTO> findByPhimId(Integer phimId, Pageable pageable) {
        return repo.findByPhim_IdPhim(phimId, pageable).map(mapper::toSummary);
    }

    // Lấy suất chiếu theo phòng
    @Override
    public List<ShowTimeSummaryDTO> findByPhongChieuId(Integer phongId) {
        return repo.findByPhongChieu_IdPhongChieu(phongId).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    @Override
    public Page<ShowTimeSummaryDTO> findByPhongChieuId(Integer phongId, Pageable pageable) {
        return repo.findByPhongChieu_IdPhongChieu(phongId, pageable).map(mapper::toSummary);
    }

    // Lấy suất chiếu theo ngày chiếu
    @Override
    public List<ShowTimeSummaryDTO> findByNgayChieu(LocalDate ngayChieu) {
        return repo.findByNgayChieu(ngayChieu).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    @Override
    public Page<ShowTimeSummaryDTO> findByNgayChieu(LocalDate ngayChieu, Pageable pageable) {
        return repo.findByNgayChieu(ngayChieu, pageable).map(mapper::toSummary);
    }

    @Override
    public List<ShowTimeSummaryDTO> findByNgayChieuBetween(LocalDate startDate, LocalDate endDate) {
        return repo.findByNgayChieuBetween(startDate, endDate).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    // Lấy theo trạng thái chiếu
    @Override
    public List<ShowTimeSummaryDTO> findByTrangThai(TrangThaiSuatChieu trangThai) {
        return repo.findByTrangThai(trangThai).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    @Override
    public Page<ShowTimeSummaryDTO> findByTrangThai(TrangThaiSuatChieu trangThai, Pageable pageable) {
        return repo.findByTrangThai(trangThai, pageable).map(mapper::toSummary);
    }

    // Suất chiếu có thể đặt (đã lên lịch, từ hôm nay trở đi)
    @Override
    public List<ShowTimeSummaryDTO> findAvailableShowtimes() {
        return repo.findAvailableShowtimes(LocalDate.now()).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    // Suất chiếu khả dụng theo phim + ngày
    @Override
    public List<ShowTimeSummaryDTO> findAvailableShowtimesByPhimAndDate(Integer phimId, LocalDate ngayChieu) {
        return repo.findAvailableByPhimAndDate(phimId, ngayChieu).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    // Lấy lịch chiếu phim trong khoảng ngày
    @Override
    public List<ShowTimeSummaryDTO> findShowtimesByPhimAndDateRange(Integer phimId, LocalDate startDate, LocalDate endDate) {
        return repo.findByPhimAndNgayChieuBetween(phimId, startDate, endDate).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    // Suất chiếu đang hoạt động theo phim
    @Override
    public List<ShowTimeSummaryDTO> findActiveShowtimesByPhimId(Integer phimId) {
        return repo.findActiveShowtimesByPhimId(phimId).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    // Kiểm tra xem suất chiếu có thể xóa hay không
    @Override
    public boolean canDeleteShowtime(Integer id) {
        ShowTime e = repo.findById(id).orElseThrow(() -> new BusinessException("Không tìm thấy"));
        return !e.getNgayChieu().isBefore(LocalDate.now());
    }

    // Kiểm tra xem suất chiếu có thể cập nhật hay không
    @Override
    public boolean canUpdateShowtime(Integer id) {
        ShowTime e = repo.findById(id).orElseThrow(() -> new BusinessException("Không tìm thấy"));
        return !e.getNgayChieu().isBefore(LocalDate.now());
    }

    // Kiểm tra logic thời gian suất chiếu
    @Override
    public void validateShowtimeSchedule(ShowTimeRequestDTO req) {
        if (req.getThoiGianBatDau().isAfter(req.getThoiGianKetThuc()))
            throw new BusinessException("Start time must be before end time");
        if (req.getNgayChieu().isBefore(LocalDate.now()))
            throw new BusinessException("Ngày chiếu không được trong quá khứ");
    }

    // Kiểm tra có trùng khung giờ trong cùng phòng không
    @Override
    public void validateTimeConflict(Integer phongChieuId, LocalDate ngay, LocalTime start, LocalTime end, Integer excludeId) {
        List<ShowTime> conflicts = repo.findTimeConflicts(phongChieuId, ngay, start, end);
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
        return repo.countByPhongChieu_IdPhongChieu(phongChieuId);
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
    public List<ShowTimeSummaryDTO> findUpcomingShowtimes(int days) {
        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(days);
        return repo.findByNgayChieuBetween(today, end).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    // Lấy suất chiếu hôm nay
    @Override
    public List<ShowTimeSummaryDTO> findTodayShowtimes() {
        return repo.findByNgayChieu(LocalDate.now()).stream().map(mapper::toSummary).collect(Collectors.toList());
    }

    // Hủy các suất chiếu đã qua ngày chiếu
    @Override
    public void expireOldShowtimes() {
        List<ShowTime> expired = repo.findByNgayChieuBefore(LocalDate.now());
        expired.forEach(sc -> sc.setTrangThai(TrangThaiSuatChieu.HOAN_THANH));
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
        List<ShowTime> c = repo.findTimeConflicts(roomId, ngay, start, end);
        if (id != null) c.removeIf(sc -> sc.getIdSuatChieu().equals(id));
        return c.isEmpty();
    }

    // Tìm kiếm nâng cao theo từ khóa, ngày và trạng thái
    @Override
    public Page<ShowTimeSummaryDTO> searchShowtimes(String keyword, LocalDate from, LocalDate to, TrangThaiSuatChieu t, Pageable pageable) {
        return repo.searchShowtimes(keyword, from, to, t, pageable).map(mapper::toSummary);
    }

    @Override
    public ShowTimeResDTO getShowtimes(Long movieId, Long cinemaId) {
        Map<String, Object> rawResult = repo.getShowtimeData(movieId, cinemaId);

        ShowTimeResDTO res = new ShowTimeResDTO();
        res.setSuccess(true);
        res.setMessage("Lấy danh sách suất chiếu thành công");

        ShowTimeResDTO.DataContent dataContent = new ShowTimeResDTO.DataContent();

        try {
            // Parse available dates
            String availableDatesJson = (String) rawResult.get("available_dates");
            List<ShowTimeResDTO.AvailableDate> availableDates =
                    objectMapper.readValue(availableDatesJson,
                            new TypeReference<List<ShowTimeResDTO.AvailableDate>>() {
                            });
            dataContent.setAvailableDates(availableDates);

            // Parse showtimes
            String showtimesJson = (String) rawResult.get("showtimes");
            List<ShowTimeResDTO.ShowtimeInfo> showtimes =
                    objectMapper.readValue(showtimesJson,
                            new TypeReference<List<ShowTimeResDTO.ShowtimeInfo>>() {
                            });
            dataContent.setShowtimes(showtimes);

            // Movie & cinema info
            if (!showtimes.isEmpty()) {
                ShowTimeResDTO.ShowtimeInfo first = showtimes.get(0);

                ShowTimeResDTO.MovieInfo movie = new ShowTimeResDTO.MovieInfo();
                movie.setId(first.getId());
                movie.setName(first.getFormat());
                movie.setPosterUrl(first.getPosterUrl());

                ShowTimeResDTO.CinemaInfo cinema = new ShowTimeResDTO.CinemaInfo();
                cinema.setId(first.getCinemaId());
                cinema.setName(first.getCinemaName());

                dataContent.setMovie(movie);
                dataContent.setCinema(cinema);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi parse JSON từ DB", e);
        }

        res.setData(dataContent);
        return res;
    }
}