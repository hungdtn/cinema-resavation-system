package org.example.cinema_reservation_system.service.suatchieu;


import org.example.cinema_reservation_system.dto.suatchieudto.SuatChieuRequestDTO;
import org.example.cinema_reservation_system.dto.suatchieudto.SuatChieuResponseDTO;
import org.example.cinema_reservation_system.dto.suatchieudto.SuatChieuStatusUpdateDTO;
import org.example.cinema_reservation_system.dto.suatchieudto.SuatChieuSummaryDTO;
import org.example.cinema_reservation_system.utils.Enum.TrangThaiSuatChieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SuatChieuService {

    // CRUD Operations
    SuatChieuResponseDTO create(SuatChieuRequestDTO requestDTO);

    SuatChieuResponseDTO update(Integer id, SuatChieuRequestDTO requestDTO);

    void delete(Integer id);

    SuatChieuResponseDTO findById(Integer id);

    Page<SuatChieuSummaryDTO> findAll(Pageable pageable);

    // Status Management
    SuatChieuResponseDTO updateStatus(SuatChieuStatusUpdateDTO statusUpdateDTO);

    void activateShowtime(Integer id);

    void deactivateShowtime(Integer id);

    // Query by Film
    List<SuatChieuSummaryDTO> findByPhimId(Integer phimId);

    Page<SuatChieuSummaryDTO> findByPhimId(Integer phimId, Pageable pageable);

    List<SuatChieuSummaryDTO> findActiveShowtimesByPhimId(Integer phimId);

    // Query by Cinema Room
    List<SuatChieuSummaryDTO> findByPhongChieuId(Integer phongChieuId);

    Page<SuatChieuSummaryDTO> findByPhongChieuId(Integer phongChieuId, Pageable pageable);

    // Query by Date
    List<SuatChieuSummaryDTO> findByNgayChieu(LocalDate ngayChieu);

    Page<SuatChieuSummaryDTO> findByNgayChieu(LocalDate ngayChieu, Pageable pageable);

    List<SuatChieuSummaryDTO> findByNgayChieuBetween(LocalDate startDate, LocalDate endDate);

    // Query by Status
    List<SuatChieuSummaryDTO> findByTrangThai(TrangThaiSuatChieu trangThai);

    Page<SuatChieuSummaryDTO> findByTrangThai(TrangThaiSuatChieu trangThai, Pageable pageable);

    // Combined Queries for Customer Booking
    List<SuatChieuSummaryDTO> findAvailableShowtimes();

    List<SuatChieuSummaryDTO> findAvailableShowtimesByPhimAndDate(Integer phimId, LocalDate ngayChieu);

    List<SuatChieuSummaryDTO> findShowtimesByPhimAndDateRange(Integer phimId, LocalDate startDate, LocalDate endDate);

    // Business Logic Methods
    boolean isTimeSlotAvailable(Integer phongChieuId, LocalDate ngayChieu,
                                LocalTime thoiGianBatDau, LocalTime thoiGianKetThuc);

    boolean isTimeSlotAvailableForUpdate(Integer suatChieuId, Integer phongChieuId,
                                         LocalDate ngayChieu, LocalTime thoiGianBatDau, LocalTime thoiGianKetThuc);

    boolean canDeleteShowtime(Integer id);

    boolean canUpdateShowtime(Integer id);

    // Validation Methods
    void validateShowtimeSchedule(SuatChieuRequestDTO requestDTO);

    void validateTimeConflict(Integer phongChieuId, LocalDate ngayChieu,
                              LocalTime startTime, LocalTime endTime, Integer excludeId);

    // Statistics and Reports
    long countByPhimId(Integer phimId);

    long countByPhongChieuId(Integer phongChieuId);

    long countByNgayChieu(LocalDate ngayChieu);

    long countByTrangThai(TrangThaiSuatChieu trangThai);

    // Utility Methods
    List<SuatChieuSummaryDTO> findUpcomingShowtimes(int days);

    List<SuatChieuSummaryDTO> findTodayShowtimes();

    void expireOldShowtimes();

    // Search and Filter
    Page<SuatChieuSummaryDTO> searchShowtimes(String keyword, LocalDate fromDate,
                                              LocalDate toDate, TrangThaiSuatChieu trangThai, Pageable pageable);

}
