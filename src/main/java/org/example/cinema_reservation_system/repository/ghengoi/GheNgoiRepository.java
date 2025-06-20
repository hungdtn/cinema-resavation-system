package org.example.cinema_reservation_system.repository.ghengoi;

// Repository Interface (unchanged)

import org.example.cinema_reservation_system.entity.GheNgoi;
import org.example.cinema_reservation_system.utils.Enum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GheNgoiRepository extends JpaRepository<GheNgoi, Integer> {

    List<GheNgoi> findByPhongChieuId(Integer idPhongChieu);
    List<GheNgoi> findByPhongChieuIdOrderByHangGheAscSoGheAsc(Integer idPhongChieu);
    List<GheNgoi> findByPhongChieuIdAndTrangThaiOrderByHangGheAscSoGheAsc(Integer idPhongChieu, Enum.TrangThaiGheNgoi trangThai);

    boolean existsByPhongChieuIdAndHangGheAndSoGhe(Integer idPhongChieu, String hangGhe, String soGhe);
    boolean existsByPhongChieuIdAndHangGheAndSoGheAndIdGheNgoiNot(Integer idPhongChieu, String hangGhe, String soGhe, Integer idGheNgoi);

    long countByPhongChieuId(Integer idPhongChieu);
    long countByPhongChieuIdAndTrangThai(Integer idPhongChieu, Enum.TrangThaiGheNgoi trangThai);

    void deleteByPhongChieuId(Integer idPhongChieu);
}
