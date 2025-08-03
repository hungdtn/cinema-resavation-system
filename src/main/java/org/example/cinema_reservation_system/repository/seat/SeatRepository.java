package org.example.cinema_reservation_system.repository.seat;

// Repository Interface (unchanged)

import org.example.cinema_reservation_system.entity.Seat;
import org.example.cinema_reservation_system.utils.enums.TrangThaiGheNgoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

    List<Seat> findByPhongChieuIdPhongChieu(Integer idPhongChieu);
    List<Seat> findByPhongChieuIdPhongChieuOrderByHangGheAscSoGheAsc(Integer idPhongChieu);
    List<Seat> findByPhongChieuIdPhongChieuAndTrangThaiOrderByHangGheAscSoGheAsc(Integer idPhongChieu, TrangThaiGheNgoi trangThai);

    boolean existsByPhongChieuIdPhongChieuAndHangGheAndSoGhe(Integer idPhongChieu, String hangGhe, String soGhe);
    boolean existsByPhongChieuIdPhongChieuAndHangGheAndSoGheAndIdGheNgoiNot(Integer idPhongChieu, String hangGhe, String soGhe, Integer idGheNgoi);

    long countByPhongChieuIdPhongChieu(Integer idPhongChieu);
    long countByPhongChieuIdPhongChieuAndTrangThai(Integer idPhongChieu, TrangThaiGheNgoi trangThai);

    void deleteByPhongChieuIdPhongChieu(Integer idPhongChieu);
}
