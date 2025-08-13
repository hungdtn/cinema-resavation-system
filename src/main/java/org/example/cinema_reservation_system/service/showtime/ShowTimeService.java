package org.example.cinema_reservation_system.service.showtime;

import org.example.cinema_reservation_system.dto.showtimedto.*;
import org.example.cinema_reservation_system.utils.enums.TrangThaiSuatChieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ShowTimeService {
    ShowTimeResponseDTO create(ShowTimeRequestDTO requestDTO);
    ShowTimeResponseDTO update(Integer id, ShowTimeRequestDTO requestDTO);
    void delete(Integer id);
    ShowTimeResponseDTO findById(Integer id);
    Page<ShowTimeSummaryDTO> findAll(Pageable pageable);

    ShowTimeResponseDTO updateStatus(ShowTimeStatusUpdateDTO statusUpdateDTO);
    void activateShowtime(Integer id);
    void deactivateShowtime(Integer id);

    List<ShowTimeSummaryDTO> findByPhimId(Integer phimId);
    Page<ShowTimeSummaryDTO> findByPhimId(Integer phimId, Pageable pageable);
    List<ShowTimeSummaryDTO> findActiveShowtimesByPhimId(Integer phimId);

    List<ShowTimeSummaryDTO> findByPhongChieuId(Integer phongChieuId);
    Page<ShowTimeSummaryDTO> findByPhongChieuId(Integer phongChieuId, Pageable pageable);

    List<ShowTimeSummaryDTO> findByNgayChieu(LocalDate ngayChieu);
    Page<ShowTimeSummaryDTO> findByNgayChieu(LocalDate ngayChieu, Pageable pageable);
    List<ShowTimeSummaryDTO> findByNgayChieuBetween(LocalDate startDate, LocalDate endDate);

    List<ShowTimeSummaryDTO> findByTrangThai(TrangThaiSuatChieu trangThai);
    Page<ShowTimeSummaryDTO> findByTrangThai(TrangThaiSuatChieu trangThai, Pageable pageable);

    List<ShowTimeSummaryDTO> findAvailableShowtimes();
    List<ShowTimeSummaryDTO> findAvailableShowtimesByPhimAndDate(Integer phimId, LocalDate ngayChieu);
    List<ShowTimeSummaryDTO> findShowtimesByPhimAndDateRange(Integer phimId, LocalDate startDate, LocalDate endDate);

    boolean isTimeSlotAvailable(Integer phongChieuId, LocalDate ngayChieu, LocalTime thoiGianBatDau, LocalTime thoiGianKetThuc);
    boolean isTimeSlotAvailableForUpdate(Integer suatChieuId, Integer phongChieuId, LocalDate ngayChieu, LocalTime thoiGianBatDau, LocalTime thoiGianKetThuc);

    boolean canDeleteShowtime(Integer id);
    boolean canUpdateShowtime(Integer id);

    void validateShowtimeSchedule(ShowTimeRequestDTO requestDTO);
    void validateTimeConflict(Integer phongChieuId, LocalDate ngayChieu, LocalTime startTime, LocalTime endTime, Integer excludeId);

    long countByPhimId(Integer phimId);
    long countByPhongChieuId(Integer phongChieuId);
    long countByNgayChieu(LocalDate ngayChieu);
    long countByTrangThai(TrangThaiSuatChieu trangThai);

    List<ShowTimeSummaryDTO> findUpcomingShowtimes(int days);
    List<ShowTimeSummaryDTO> findTodayShowtimes();
    void expireOldShowtimes();

    Page<ShowTimeSummaryDTO> searchShowtimes(String keyword, LocalDate fromDate, LocalDate toDate, TrangThaiSuatChieu trangThai, Pageable pageable);

    ShowTimeResDTO getShowtimes(Long movieId, Long cinemaId);
}
