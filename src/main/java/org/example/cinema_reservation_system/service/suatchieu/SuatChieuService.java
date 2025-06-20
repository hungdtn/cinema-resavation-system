package org.example.cinema_reservation_system.service.suatchieu;

import org.example.cinema_reservation_system.dto.suatchieudto.*;
import org.example.cinema_reservation_system.utils.Enum.TrangThaiSuatChieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SuatChieuService {
    SuatChieuResponseDTO create(SuatChieuRequestDTO requestDTO);
    SuatChieuResponseDTO update(Integer id, SuatChieuRequestDTO requestDTO);
    void delete(Integer id);
    SuatChieuResponseDTO findById(Integer id);
    Page<SuatChieuSummaryDTO> findAll(Pageable pageable);

    SuatChieuResponseDTO updateStatus(SuatChieuStatusUpdateDTO statusUpdateDTO);
    void activateShowtime(Integer id);
    void deactivateShowtime(Integer id);

    List<SuatChieuSummaryDTO> findByPhimId(Integer phimId);
    Page<SuatChieuSummaryDTO> findByPhimId(Integer phimId, Pageable pageable);
    List<SuatChieuSummaryDTO> findActiveShowtimesByPhimId(Integer phimId);

    List<SuatChieuSummaryDTO> findByPhongChieuId(Integer phongChieuId);
    Page<SuatChieuSummaryDTO> findByPhongChieuId(Integer phongChieuId, Pageable pageable);

    List<SuatChieuSummaryDTO> findByNgayChieu(LocalDate ngayChieu);
    Page<SuatChieuSummaryDTO> findByNgayChieu(LocalDate ngayChieu, Pageable pageable);
    List<SuatChieuSummaryDTO> findByNgayChieuBetween(LocalDate startDate, LocalDate endDate);

    List<SuatChieuSummaryDTO> findByTrangThai(TrangThaiSuatChieu trangThai);
    Page<SuatChieuSummaryDTO> findByTrangThai(TrangThaiSuatChieu trangThai, Pageable pageable);

    List<SuatChieuSummaryDTO> findAvailableShowtimes();
    List<SuatChieuSummaryDTO> findAvailableShowtimesByPhimAndDate(Integer phimId, LocalDate ngayChieu);
    List<SuatChieuSummaryDTO> findShowtimesByPhimAndDateRange(Integer phimId, LocalDate startDate, LocalDate endDate);

    boolean isTimeSlotAvailable(Integer phongChieuId, LocalDate ngayChieu, LocalTime thoiGianBatDau, LocalTime thoiGianKetThuc);
    boolean isTimeSlotAvailableForUpdate(Integer suatChieuId, Integer phongChieuId, LocalDate ngayChieu, LocalTime thoiGianBatDau, LocalTime thoiGianKetThuc);

    boolean canDeleteShowtime(Integer id);
    boolean canUpdateShowtime(Integer id);

    void validateShowtimeSchedule(SuatChieuRequestDTO requestDTO);
    void validateTimeConflict(Integer phongChieuId, LocalDate ngayChieu, LocalTime startTime, LocalTime endTime, Integer excludeId);

    long countByPhimId(Integer phimId);
    long countByPhongChieuId(Integer phongChieuId);
    long countByNgayChieu(LocalDate ngayChieu);
    long countByTrangThai(TrangThaiSuatChieu trangThai);

    List<SuatChieuSummaryDTO> findUpcomingShowtimes(int days);
    List<SuatChieuSummaryDTO> findTodayShowtimes();
    void expireOldShowtimes();

    Page<SuatChieuSummaryDTO> searchShowtimes(String keyword, LocalDate fromDate, LocalDate toDate, TrangThaiSuatChieu trangThai, Pageable pageable);
}
