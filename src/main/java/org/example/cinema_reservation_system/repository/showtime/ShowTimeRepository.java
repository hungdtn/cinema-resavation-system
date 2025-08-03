package org.example.cinema_reservation_system.repository.showtime;

import org.example.cinema_reservation_system.entity.ShowTime;
import org.example.cinema_reservation_system.utils.enums.TrangThaiSuatChieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ShowTimeRepository extends JpaRepository<ShowTime, Integer> {

    List<ShowTime> findByPhim_IdPhim(Integer idPhim);
    Page<ShowTime> findByPhim_IdPhim(Integer idPhim, Pageable pageable);

    List<ShowTime> findByPhongChieu_IdPhongChieu(Integer id);
    Page<ShowTime> findByPhongChieu_IdPhongChieu(Integer id, Pageable pageable);

    List<ShowTime> findByNgayChieu(LocalDate ngay);
    Page<ShowTime> findByNgayChieu(LocalDate ngay, Pageable pageable);

    List<ShowTime> findByNgayChieuBetween(LocalDate start, LocalDate end);
    List<ShowTime> findByNgayChieuBefore(LocalDate date);

    List<ShowTime> findByTrangThai(TrangThaiSuatChieu trangThai);
    Page<ShowTime> findByTrangThai(TrangThaiSuatChieu trangThai, Pageable pageable);

    long countByPhim_IdPhim(Integer idPhim);
    long countByPhongChieu_IdPhongChieu(Integer id);
    long countByNgayChieu(LocalDate ngay);
    long countByTrangThai(TrangThaiSuatChieu trangThai);

    @Query("""
        SELECT s FROM ShowTime s
        WHERE s.phongChieu.idPhongChieu = :roomId
          AND s.ngayChieu = :ngay
          AND ((s.thoiGianBatDau < :end AND s.thoiGianKetThuc > :start))
    """)
    List<ShowTime> findTimeConflicts(@Param("roomId") Integer roomId,
                                     @Param("ngay") LocalDate ngay,
                                     @Param("start") LocalTime start,
                                     @Param("end") LocalTime end);

    @Query("""
        SELECT s FROM ShowTime s
        WHERE s.ngayChieu >= :today AND s.trangThai = org.example.cinema_reservation_system.utils.enums.TrangThaiSuatChieu.DA_LEN_LICH
    """)
    List<ShowTime> findAvailableShowtimes(@Param("today") LocalDate today);

    @Query("""
        SELECT s FROM ShowTime s
        WHERE s.phim.idPhim = :phimId AND s.ngayChieu = :ngayChieu
          AND s.trangThai = org.example.cinema_reservation_system.utils.enums.TrangThaiSuatChieu.DA_LEN_LICH
    """)
    List<ShowTime> findAvailableByPhimAndDate(@Param("phimId") Integer phimId,
                                              @Param("ngayChieu") LocalDate ngayChieu);

    @Query("""
        SELECT s FROM ShowTime s
        WHERE s.phim.idPhim = :phimId AND s.ngayChieu BETWEEN :start AND :end
    """)
    List<ShowTime> findByPhimAndNgayChieuBetween(@Param("phimId") Integer phimId,
                                                 @Param("start") LocalDate start,
                                                 @Param("end") LocalDate end);

    @Query("""
        SELECT s FROM ShowTime s
        WHERE s.phim.idPhim = :phimId AND s.trangThai = org.example.cinema_reservation_system.utils.enums.TrangThaiSuatChieu.DA_LEN_LICH
    """)
    List<ShowTime> findActiveShowtimesByPhimId(@Param("phimId") Integer phimId);

    @Query("""
        SELECT s FROM ShowTime s
        WHERE (:keyword IS NULL OR LOWER(s.tenSuatChieu) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:fromDate IS NULL OR s.ngayChieu >= :fromDate)
          AND (:toDate IS NULL OR s.ngayChieu <= :toDate)
          AND (:trangThai IS NULL OR s.trangThai = :trangThai)
    """)
    Page<ShowTime> searchShowtimes(@Param("keyword") String keyword,
                                   @Param("fromDate") LocalDate fromDate,
                                   @Param("toDate") LocalDate toDate,
                                   @Param("trangThai") TrangThaiSuatChieu trangThai,
                                   Pageable pageable);
}
