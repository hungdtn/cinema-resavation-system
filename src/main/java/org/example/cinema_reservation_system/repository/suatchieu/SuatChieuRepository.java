package org.example.cinema_reservation_system.repository.suatchieu;

import org.example.cinema_reservation_system.entity.SuatChieu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuatChieuRepository extends JpaRepository<SuatChieu, Integer> {
    List<SuatChieu> findByPhimIdPhim(Integer idPhim);
    List<SuatChieu> findByPhongChieuIdPhongChieu(Integer idPhongChieu);
}
