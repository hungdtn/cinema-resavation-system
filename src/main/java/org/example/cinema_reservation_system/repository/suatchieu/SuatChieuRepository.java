package org.example.cinema_reservation_system.repository.suatchieu;

import org.example.cinema_reservation_system.entity.SuatChieu;
import org.example.cinema_reservation_system.utils.Enum.TrangThaiSuatChieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SuatChieuRepository extends JpaRepository<SuatChieu, Integer> {

    List<SuatChieu> findByPhim_IdPhim(Integer idPhim);
    Page<SuatChieu> findByPhim_IdPhim(Integer idPhim, Pageable pageable);

    List<SuatChieu> findByPhongChieu_Id(Integer id);
    Page<SuatChieu> findByPhongChieu_Id(Integer id, Pageable pageable);

    List<SuatChieu> findByNgayChieu(LocalDate ngay);
    Page<SuatChieu> findByNgayChieu(LocalDate ngay, Pageable pageable);

    List<SuatChieu> findByNgayChieuBetween(LocalDate start, LocalDate end);
    List<SuatChieu> findByNgayChieuBefore(LocalDate date);

    List<SuatChieu> findByTrangThai(TrangThaiSuatChieu trangThai);
    Page<SuatChieu> findByTrangThai(TrangThaiSuatChieu trangThai, Pageable pageable);

    long countByPhim_IdPhim(Integer idPhim);
    long countByPhongChieu_Id(Integer id);
    long countByNgayChieu(LocalDate ngay);
    long countByTrangThai(TrangThaiSuatChieu trangThai);

    @Query("""
        SELECT s FROM SuatChieu s
        WHERE s.phongChieu.id = :roomId
          AND s.ngayChieu = :ngay
          AND ((s.thoiGianBatDau < :end AND s.thoiGianKetThuc > :start))
    """)
    List<SuatChieu> findTimeConflicts(@Param("roomId") Integer roomId,
                                      @Param("ngay") LocalDate ngay,
                                      @Param("start") LocalTime start,
                                      @Param("end") LocalTime end);

    @Query("""
        SELECT s FROM SuatChieu s
        WHERE s.ngayChieu >= :today AND s.trangThai = org.example.cinema_reservation_system.utils.Enum.TrangThaiSuatChieu.da_len_lich
    """)
    List<SuatChieu> findAvailableShowtimes(@Param("today") LocalDate today);

    @Query("""
        SELECT s FROM SuatChieu s
        WHERE s.phim.idPhim = :phimId AND s.ngayChieu = :ngayChieu
          AND s.trangThai = org.example.cinema_reservation_system.utils.Enum.TrangThaiSuatChieu.da_len_lich
    """)
    List<SuatChieu> findAvailableByPhimAndDate(@Param("phimId") Integer phimId,
                                               @Param("ngayChieu") LocalDate ngayChieu);

    @Query("""
        SELECT s FROM SuatChieu s
        WHERE s.phim.idPhim = :phimId AND s.ngayChieu BETWEEN :start AND :end
    """)
    List<SuatChieu> findByPhimAndNgayChieuBetween(@Param("phimId") Integer phimId,
                                                  @Param("start") LocalDate start,
                                                  @Param("end") LocalDate end);

    @Query("""
        SELECT s FROM SuatChieu s
        WHERE s.phim.idPhim = :phimId AND s.trangThai = org.example.cinema_reservation_system.utils.Enum.TrangThaiSuatChieu.da_len_lich
    """)
    List<SuatChieu> findActiveShowtimesByPhimId(@Param("phimId") Integer phimId);

    @Query("""
        SELECT s FROM SuatChieu s
        WHERE (:keyword IS NULL OR LOWER(s.tenSuatChieu) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:fromDate IS NULL OR s.ngayChieu >= :fromDate)
          AND (:toDate IS NULL OR s.ngayChieu <= :toDate)
          AND (:trangThai IS NULL OR s.trangThai = :trangThai)
    """)
    Page<SuatChieu> searchShowtimes(@Param("keyword") String keyword,
                                    @Param("fromDate") LocalDate fromDate,
                                    @Param("toDate") LocalDate toDate,
                                    @Param("trangThai") TrangThaiSuatChieu trangThai,
                                    Pageable pageable);
}
